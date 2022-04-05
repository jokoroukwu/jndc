package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum SuppliesStatus implements NdcComponent {
    NO_NEW_STATE('0'),
    GOOD_STATE('1'),
    MEDIA_LOW('2'),
    MEDIA_OUT('3'),
    OVERFILL('4');

    private final char value;

    SuppliesStatus(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<SuppliesStatus> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NO_NEW_STATE);
            case '1':
                return DescriptiveOptional.of(GOOD_STATE);
            case '2':
                return DescriptiveOptional.of(MEDIA_LOW);
            case '3':
                return DescriptiveOptional.of(MEDIA_OUT);
            case '4':
                return DescriptiveOptional.of(OVERFILL);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Supplies Status'", value));
            }
        }
    }

    public static DescriptiveOptional<SuppliesStatus> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }


    @Override
    public String toString() {
        return String.format("%s ('%c')", name(), value);
    }
}
