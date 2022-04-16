package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum TerminalMessageSubClass implements NdcComponent {
    TRANSACTION_REQUEST_MESSAGE('1', "Transaction Request message ('1')"),
    STATUS_MESSAGE('2', "Status Message ('2')"),
    ENCRYPTOR_INITIALISATION_DATA('3', "Encryptor Initialisation Data ('3')");

    private final char value;
    private final String ndcString;
    private final String displayedName;

    TerminalMessageSubClass(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
        ndcString = String.valueOf(value);
    }

    public static DescriptiveOptional<TerminalMessageSubClass> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(TRANSACTION_REQUEST_MESSAGE);
            case '2':
                return DescriptiveOptional.of(STATUS_MESSAGE);
            case '3':
                return DescriptiveOptional.of(ENCRYPTOR_INITIALISATION_DATA);
            default: {
                return DescriptiveOptional.empty(() -> String.format("'%c' does not match any Terminal Message Subclass", value));
            }
        }
    }

    public static DescriptiveOptional<TerminalMessageSubClass> forValue(int value) {
        return forValue((char) value);
    }


    public char value() {
        return value;
    }

    @Override
    public String toNdcString() {
        return ndcString;
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
