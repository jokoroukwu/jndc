package io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalLong;

import java.util.Optional;

public class TimeVariantNumber extends SequenceTimeVariantNumber {
    TimeVariantNumber(long value) {
        super(value);
    }

    public static DescriptiveOptionalLong tryReadTimeVariantNumber(NdcCharBuffer ndcCharBuffer) {
        final Optional<String> optionalError = ndcCharBuffer.trySkipFieldSeparator();
        if (optionalError.isPresent()) {
            return DescriptiveOptionalLong.empty(() -> "no field separator before field: " + optionalError.get());
        }
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            return ndcCharBuffer.tryReadHexLong(8);
        }
        //  field is empty
        return DescriptiveOptionalLong.of(-1);
    }

    @Override
    public boolean isTimeVariant() {
        return true;
    }

    @Override
    public String toNdcString() {
        return Longs.toZeroPaddedHexString(value, 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeVariantNumber that = (TimeVariantNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
