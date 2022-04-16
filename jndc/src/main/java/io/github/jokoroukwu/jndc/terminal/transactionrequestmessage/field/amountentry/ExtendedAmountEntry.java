package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry;

import io.github.jokoroukwu.jndc.util.Longs;

public final class ExtendedAmountEntry extends AmountEntry {

    public ExtendedAmountEntry(long value) {
        super(value);
    }

    ExtendedAmountEntry(long value, Void unused) {
        super(value, unused);
    }

    @Override
    public String toNdcString() {
        return Longs.toZeroPaddedString(value, 12);
    }

    @Override
    protected long validateValue(long value) {
        return Longs.validateRange(value, 0, 999_999_999_999L, "'Amount Entry'");
    }

}
