package io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalLong;

import java.util.Optional;

public class SequenceNumber extends SequenceTimeVariantNumber {

    SequenceNumber(long value) {
        super(value);
    }

    public static DescriptiveOptionalLong tryReadSequenceNumber(NdcCharBuffer ndcCharBuffer) {
        final Optional<String> optionalError = ndcCharBuffer.trySkipFieldSeparator();
        if (optionalError.isPresent()) {
            return DescriptiveOptionalLong.empty(() -> "no field separator before field: " + optionalError.get());
        }
        if (!ndcCharBuffer.hasFollowingFieldSeparator()) {
            return ndcCharBuffer.tryReadLong(3);
        }
        return DescriptiveOptionalLong.of(-1);
    }

    @Override
    public boolean isTimeVariant() {
        return false;
    }

    @Override
    public String toNdcString() {
        return Longs.toZeroPaddedString(value, 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceNumber that = (SequenceNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
