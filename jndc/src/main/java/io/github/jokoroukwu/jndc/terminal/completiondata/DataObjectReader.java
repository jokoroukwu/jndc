package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTLVReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.LinkedHashMap;

public class DataObjectReader implements NdcComponentReader<LinkedHashMap<Integer, BerTlv<String>>> {
    public static final DataObjectReader DEFAULT = new DataObjectReader(HexStringBerTLVReader.DEFAULT);
    private final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> berTlvReader;

    public DataObjectReader(NdcComponentReader<DescriptiveOptional<BerTlv<String>>> berTlvReader) {
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "berTlvReader");
    }

    public DataObjectReader() {
        this(HexStringBerTLVReader.DEFAULT);
    }

    @Override
    public LinkedHashMap<Integer, BerTlv<String>> readComponent(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasRemaining() || ndcCharBuffer.hasFollowingFieldSeparator()) {
            //  this sub-field may be omitted
            return new LinkedHashMap<>(0, 0);
        }
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> onFieldParseError(errorMessage, ndcCharBuffer));

        final LinkedHashMap<Integer, BerTlv<String>> dataObjects = new LinkedHashMap<>();
        do {
            berTlvReader.readComponent(ndcCharBuffer)
                    .resolve(tlv -> dataObjects.put(tlv.getTag(), tlv),
                            errorMessage -> onFieldParseError(errorMessage, ndcCharBuffer));

        } while (ndcCharBuffer.hasFieldDataRemaining());

        return dataObjects;
    }

    private void onFieldParseError(String errorMessage, NdcCharBuffer ndcCharBuffer) {
        throw NdcMessageParseException.withFieldName("'Completion Data': 'Central requested ICC data objects'",
                errorMessage, ndcCharBuffer);
    }
}
