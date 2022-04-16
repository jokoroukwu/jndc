package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum StatusDescriptor implements NdcComponent {
    DEVICE_FAULT('8'),
    READY('9'),
    COMMAND_REJECT('A'),
    READY_B('B'),
    SPECIFIC_COMMAND_REJECT('C'),
    TERMINAL_STATE('F');

    private final char value;

    StatusDescriptor(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<StatusDescriptor> forValue(char value) {
        switch (value) {
            case '8':
                return DescriptiveOptional.of(DEVICE_FAULT);
            case '9':
                return DescriptiveOptional.of(READY);
            case 'A':
                return DescriptiveOptional.of(COMMAND_REJECT);
            case 'B':
                return DescriptiveOptional.of(READY_B);
            case 'C':
                return DescriptiveOptional.of(SPECIFIC_COMMAND_REJECT);
            case 'F':
                return DescriptiveOptional.of(TERMINAL_STATE);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Status Descriptor'", value));
            }
        }
    }

    public static DescriptiveOptional<StatusDescriptor> forValue(int value) {
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
        return name() + " ('" + value + "')";
    }
}
