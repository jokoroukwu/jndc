package io.github.jokoroukwu.jndc.central;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum CentralMessageClass implements NdcComponent {
    TERMINAL_COMMAND('1', "Terminal Command ('1')"),
    DATA_COMMAND('3', "Data Command ('3')"),
    TRANSACTION_REPLY_COMMAND('4', "Transaction Reply Command ('4')"),
    EMV_CONFIGURATION('8', "EMV Configuration ('8')");

    private final char value;
    private final String displayedName;

    CentralMessageClass(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<CentralMessageClass> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(TERMINAL_COMMAND);
            case '3':
                return DescriptiveOptional.of(DATA_COMMAND);
            case '4':
                return DescriptiveOptional.of(TRANSACTION_REPLY_COMMAND);
            case '8':
                return DescriptiveOptional.of(EMV_CONFIGURATION);
            default: {
                final String errorMessageTemplate = "value '%s' is not a valid Central Message class";
                return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, value));
            }
        }
    }

    public static DescriptiveOptional<CentralMessageClass> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return Character.toString(value);
    }
}
