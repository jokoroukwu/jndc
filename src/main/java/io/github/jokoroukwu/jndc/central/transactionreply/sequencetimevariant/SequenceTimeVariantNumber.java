package io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Longs;

import java.util.StringJoiner;

public abstract class SequenceTimeVariantNumber implements NdcComponent {
    protected final long value;

    SequenceTimeVariantNumber(long value) {
        this.value = value;
    }

    public static SequenceTimeVariantNumber timeVariant(long value) {
        Longs.validateHexRange(value, 0, 0xFF_FF_FF_FFL, "Time variant Number");
        return new TimeVariantNumber(value);
    }

    public static SequenceTimeVariantNumber sequence(long value) {
        Longs.validateMaxValue(value, 999, "Message Sequence Number");
        return new SequenceNumber(value);
    }

    public long getValue() {
        return value;
    }

    public abstract boolean isTimeVariant();

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("value: " + value)
                .toString();
    }
}
