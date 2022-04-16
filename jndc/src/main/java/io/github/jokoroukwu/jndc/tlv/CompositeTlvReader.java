package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;

import java.util.LinkedHashMap;

public class CompositeTlvReader<T> implements NdcComponentReader<DescriptiveOptional<CompositeTlv<T>>> {
    private final NdcComponentReader<DescriptiveOptional<BerTlv<T>>> nestedTlvReader;
    private final BerTlvReader berTlvReader;
    private final int tag;

    public CompositeTlvReader(NdcComponentReader<DescriptiveOptional<BerTlv<T>>> nestedTlvReader, BerTlvReader berTlvReader, int tag) {
        this.nestedTlvReader = ObjectUtils.validateNotNull(nestedTlvReader, "nestedTlvReader");
        this.berTlvReader = ObjectUtils.validateNotNull(berTlvReader, "berTlvReader");
        this.tag = tag;
    }

    @Override
    public DescriptiveOptional<CompositeTlv<T>> readComponent(NdcCharBuffer ndcCharBuffer) {
        final DescriptiveOptionalInt optionalTag = berTlvReader.tryReadTag(ndcCharBuffer)
                .filter(this::isExpectedTag, value -> () -> String.format("expected tag '%#X' but was '%#X'", tag, value));
        if (optionalTag.isEmpty()) {
            return DescriptiveOptional.empty(optionalTag.descriptionSupplier());
        }
        final DescriptiveOptionalInt optionalLength = berTlvReader.tryReadLength(ndcCharBuffer);
        if (optionalLength.isPresent()) {
            return DescriptiveOptional.empty(optionalLength.descriptionSupplier());
        }

        return ndcCharBuffer.tryReadCharSequence(optionalLength.get() * 2)
                .map(NdcCharBuffer::wrap)
                .flatMap(this::readNestedTlvs)
                .map(nestedTlvs -> new CompositeTlv<>(tag, optionalLength.get(), nestedTlvs));
    }

    private DescriptiveOptional<LinkedHashMap<Integer, BerTlv<T>>> readNestedTlvs(NdcCharBuffer ndcCharBuffer) {
        final LinkedHashMap<Integer, BerTlv<T>> nestedTlvs = new LinkedHashMap<>();
        while (ndcCharBuffer.hasFieldDataRemaining()) {
            final DescriptiveOptional<BerTlv<T>> optionalTlv = nestedTlvReader.readComponent(ndcCharBuffer);
            if (optionalTlv.isEmpty()) {
                return DescriptiveOptional.empty(optionalTlv.descriptionSupplier());
            }
            final BerTlv<T> tlv = optionalTlv.get();
            nestedTlvs.put(tlv.getTag(), tlv);
        }
        return DescriptiveOptional.of(nestedTlvs);
    }

    private boolean isExpectedTag(int tag) {
        return this.tag < 0 || tag == this.tag;
    }
}
