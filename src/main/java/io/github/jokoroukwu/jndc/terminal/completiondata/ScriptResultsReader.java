package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.BerTlvReader;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptResultsReader implements NdcComponentReader<List<ScriptResult>> {
    public static final ScriptResultsReader DEFAULT = new ScriptResultsReader(BerTlvReaderBase.INSTANCE);
    private final BerTlvReader berTlvReader;

    public ScriptResultsReader(BerTlvReader berTlvReader) {
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "berTlvReader");
    }

    public ScriptResultsReader() {
        this(BerTlvReaderBase.INSTANCE);
    }

    @Override
    public List<ScriptResult> readComponent(NdcCharBuffer ndcCharBuffer) {
        //  sub-field may be omitted
        if (!ndcCharBuffer.hasRemaining() || ndcCharBuffer.hasFollowingFieldSeparator()) {
            return List.of();
        }
        return readScriptResults(ndcCharBuffer);
    }

    private List<ScriptResult> readScriptResults(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<ScriptResult> scriptResults = new ArrayList<>();
        do {
            final ScriptResult scriptResult = readScriptResult(ndcCharBuffer);
            scriptResults.add(scriptResult);
        } while (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasFollowingFieldSeparator());

        scriptResults.trimToSize();

        return Collections.unmodifiableList(scriptResults);
    }

    private ScriptResult readScriptResult(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError("'Completion Data': 'Result of issuer script processing'",
                        errorMessage, ndcCharBuffer));

        final ProcessingResult processingResult = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ProcessingResult::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName("'Completion Data': 'Result of issuer script processing'",
                        errorMessage, ndcCharBuffer));

        final int scriptCommandSequence = ndcCharBuffer.tryReadHexInt(1)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName("'Completion Data': 'Sequence number of script command'",
                        errorMessage, ndcCharBuffer));

        final BerTlv<String> scriptId = readScriptId(ndcCharBuffer);
        return new ScriptResult(processingResult, scriptCommandSequence, scriptId, null);
    }

    private BerTlv<String> readScriptId(NdcCharBuffer ndcCharBuffer) {
        final int scriptIdTag = berTlvReader.tryReadTag(ndcCharBuffer)
                .filter(this::isExpectedTag, tag -> () -> String.format("expected tag 9F18 but was %X", tag))
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName("'Completion Data': Script Identifier (tag 0x9F18)",
                        errorMessage, ndcCharBuffer));

        final int length = berTlvReader.tryReadLength(ndcCharBuffer)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withFieldName("'Completion Data': Script Identifier length", errorMessage, ndcCharBuffer));

        final String value = ndcCharBuffer.tryReadCharSequence(length * 2)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withFieldName("'Completion Data': Script Identifier value", errorMessage, ndcCharBuffer));
        return new HexStringBerTlv(scriptIdTag, value);
    }

    private boolean isExpectedTag(int tag) {
        return tag == 0x9F18;
    }
}
