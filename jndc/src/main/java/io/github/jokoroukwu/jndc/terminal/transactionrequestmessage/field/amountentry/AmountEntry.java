package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry;

import io.github.jokoroukwu.jndc.NdcComponent;

import java.util.StringJoiner;

public abstract class AmountEntry implements NdcComponent {
    protected final long value;

    AmountEntry(long value) {
        this.value = validateValue(value);
    }

    AmountEntry(long value, Void unused) {
        this.value = value;
    }

    public static AmountEntry base(long value) {
        return new BaseAmountEntry(value);
    }

    public static AmountEntry extended(long value) {
        return new ExtendedAmountEntry(value);
    }

    public long getValue() {
        return value;
    }

    protected abstract long validateValue(long value);

    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("value: " + value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountEntry that = (AmountEntry) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }
}
