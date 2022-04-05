package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.LinkedHashMap;
import java.util.List;

import static io.github.jokoroukwu.jndc.tlv.ResponseFormat2.NAME;

public class ResponseFormat2Reader implements NdcComponentReader<DescriptiveOptional<ResponseFormat2>> {
    private final List<NdcComponentReader<DescriptiveOptional<BerTlv<String>>>> tagReaders;
    private final BerTlvReader bertTvReader;

    public ResponseFormat2Reader(List<NdcComponentReader<DescriptiveOptional<BerTlv<String>>>> tagReaders, BerTlvReader bertTvReader) {
        this.tagReaders = ObjectUtils.validateNotNull(tagReaders, "expectedTags");
        this.bertTvReader = ObjectUtils.validateNotNull(bertTvReader, "berTlvReader");
    }

    public ResponseFormat2Reader(List<NdcComponentReader<DescriptiveOptional<BerTlv<String>>>> tagReaders) {
        this(tagReaders, BerTlvReaderBase.INSTANCE);
    }

    @Override
    public DescriptiveOptional<ResponseFormat2> readComponent(NdcCharBuffer buffer) {
        ObjectUtils.validateNotNull(buffer, "buffer");
        return bertTvReader.tryReadTag(buffer)
                .filter(this::isResponseFormatTag, actual -> ()
                        -> String.format("expected '%s' but was: %d", NAME, actual))
                .flatMapToObject(tag -> bertTvReader.tryReadLength(buffer)
                        .mapDescription(errorMessage
                                -> String.format("failed to read '%s' length: %s", NAME, errorMessage))
                        .flatMapToObject(length -> toResponseFormat2(length, buffer)));
    }

    private boolean isResponseFormatTag(int value) {
        return value == ResponseFormat2.TAG;
    }

    private DescriptiveOptional<ResponseFormat2> toResponseFormat2(int length, NdcCharBuffer buffer) {
        return readValueBuffer(buffer, length)
                .flatMap(this::readNestedTLVs)
                .map(nestedTLVs -> new ResponseFormat2(length, nestedTLVs));
    }

    private DescriptiveOptional<NdcCharBuffer> readValueBuffer(NdcCharBuffer buffer, int length) {
        return buffer.tryReadCharSequence(length * 2)
                .map(NdcCharBuffer::wrap)
                .wrapDescription(errorMessage -> String.format("failed to read %s value: %s", NAME, errorMessage));
    }

    private DescriptiveOptional<LinkedHashMap<Integer, BerTlv<String>>> readNestedTLVs(NdcCharBuffer buffer) {
        final LinkedHashMap<Integer, BerTlv<String>> nestedTlvs = new LinkedHashMap<>(tagReaders.size());

        for (NdcComponentReader<DescriptiveOptional<BerTlv<String>>> tagReader : tagReaders) {
            final DescriptiveOptional<BerTlv<String>> optionalNextTlv = tagReader.readComponent(buffer);
            if (optionalNextTlv.isPresent()) {
                final BerTlv<String> nextTlv = optionalNextTlv.get();
                nestedTlvs.put(nextTlv.getTag(), nextTlv);
            } else {
                return DescriptiveOptional.empty(optionalNextTlv::description);
            }
        }
        if (buffer.hasRemaining()) {
            return DescriptiveOptional.empty(()
                    -> String.format("unexpected data in '%s' value: %s", NAME, buffer.subBuffer()));
        }
        return DescriptiveOptional.of(nestedTlvs);
    }
}
