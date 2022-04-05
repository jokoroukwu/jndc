package io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum TargetBufferId {
    NO_BUFFER('0'),
    BUFFER_B('1'),
    BUFFER_C('2'),
    AMOUNT_BUFFER('3');

    private final char value;

    TargetBufferId(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<TargetBufferId> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NO_BUFFER);
            case '1':
                return DescriptiveOptional.of(BUFFER_B);
            case '2':
                return DescriptiveOptional.of(BUFFER_C);
            case '3':
                return DescriptiveOptional.of(AMOUNT_BUFFER);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Target Buffer Identifier'", value));
            }
        }
    }

    public static DescriptiveOptional<TargetBufferId> forValue(int value) {
        return forValue((char) value);
    }


    public char getValue() {
        return value;
    }


}
