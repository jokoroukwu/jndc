package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum ProcessingResult implements NdcComponent {
    NOT_PERFORMED('0'),
    FAILURE('1'),
    SUCCESS('2');

    private final char value;

    ProcessingResult(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<ProcessingResult> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NOT_PERFORMED);
            case '1':
                return DescriptiveOptional.of(FAILURE);
            case '2':
                return DescriptiveOptional.of(SUCCESS);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("'%c' is not a valid 'Issuer Script Processing Result' value", value));
            }
        }
    }

    public static DescriptiveOptional<ProcessingResult> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }
}
