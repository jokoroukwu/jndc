package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry;

import io.github.jokoroukwu.jndc.util.Longs;

public final class BaseAmountEntry extends AmountEntry {

    public BaseAmountEntry(long value) {
        super(value);
    }

    BaseAmountEntry(long value, Void unused) {
        super(value, unused);
    }

    @Override
    public String toNdcString() {
        return Longs.toZeroPaddedString(value, 8);
    }

    @Override
    protected long validateValue(long value) {
        return Longs.validateRange(value, 0, 99999999, "'Amount Entry'");
    }
}
