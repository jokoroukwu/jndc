package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum ErrorSeverity implements NdcComponent {
    NO_ERROR('0'),
    ROUTINE('1'),
    WARNING('2'),
    SUSPEND('3'),
    FATAL('4');

    private final char value;
    private final String displayedName;

    ErrorSeverity(char value) {
        this.value = value;
        displayedName = String.format("%s ('%c')", name(), value);
    }

    public static DescriptiveOptional<ErrorSeverity> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(NO_ERROR);
            case '1':
                return DescriptiveOptional.of(ROUTINE);
            case '2':
                return DescriptiveOptional.of(WARNING);
            case '3':
                return DescriptiveOptional.of(SUSPEND);
            case '4':
                return DescriptiveOptional.of(FATAL);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Error Severity'", value));
            }
        }
    }

    public static DescriptiveOptional<ErrorSeverity> forValue(int value) {
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
        return displayedName;
    }
}
