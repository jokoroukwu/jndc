package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class CompletionDataReader implements NdcComponentReader<Optional<CompletionData>> {
    public static CompletionDataReader DEFAULT = new CompletionDataReader(ScriptResultsReader.DEFAULT, DataObjectReader.DEFAULT);
    private final NdcComponentReader<List<ScriptResult>> scriptResultsReader;
    private final NdcComponentReader<LinkedHashMap<Integer, BerTlv<String>>> dataObjectsReader;

    public CompletionDataReader(NdcComponentReader<List<ScriptResult>> scriptResultsReader,
                                NdcComponentReader<LinkedHashMap<Integer, BerTlv<String>>> dataObjectsReader) {
        this.scriptResultsReader = ObjectUtils.validateNotNull(scriptResultsReader, "scriptResultsReader");
        this.dataObjectsReader = ObjectUtils.validateNotNull(dataObjectsReader, "dataObjects");
    }


    @Override
    public Optional<CompletionData> readComponent(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasFollowingFieldSeparator()) {
            ndcCharBuffer.skipGroupSeparator();
            // at least smart card identifier should be present
            final String smartCardId = readSmartCardId(ndcCharBuffer);
            final LinkedHashMap<Integer, BerTlv<String>> iccDataObjects = dataObjectsReader.readComponent(ndcCharBuffer);
            final List<ScriptResult> scriptResults = scriptResultsReader.readComponent(ndcCharBuffer);
            return Optional.of(new CompletionData(smartCardId, iccDataObjects, scriptResults));
        }
        return Optional.empty();
    }

    private String readSmartCardId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withFieldName("'Completion Data': 'Smart card data identifier'", errorMessage, ndcCharBuffer));
    }
}
