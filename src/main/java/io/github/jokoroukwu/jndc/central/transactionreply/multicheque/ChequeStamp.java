package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum ChequeStamp {
    NO_STAMP('0'),
    STAMP('1');

    private final char value;

    ChequeStamp(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<ChequeStamp> forValue(char value) {
        if (value == NO_STAMP.value) {
            return DescriptiveOptional.of(NO_STAMP);
        }
        if (value == STAMP.value) {
            return DescriptiveOptional.of(STAMP);
        }
        return DescriptiveOptional.empty(() -> String.format("'%c' is not a valid 'Cheque Stamp' value", value));
    }

    public static DescriptiveOptional<ChequeStamp> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
    }

}
