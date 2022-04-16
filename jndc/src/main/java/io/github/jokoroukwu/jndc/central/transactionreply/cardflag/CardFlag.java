package io.github.jokoroukwu.jndc.central.transactionreply.cardflag;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum CardFlag implements NdcComponent {
    RETURN('0', "Return card during the Close state ('0')"),
    RETAIN('1', "Retain card during the Close state ('1')"),
    RETURN_ON_PROCESSING('4', "Return card while processing the transaction reply ('4')");

    private final char value;
    private final String displayedName;

    CardFlag(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<CardFlag> forValue(char value) {
        switch (value) {
            case '0':
                return DescriptiveOptional.of(RETURN);
            case '1':
                return DescriptiveOptional.of(RETAIN);
            case '4':
                return DescriptiveOptional.of(RETURN_ON_PROCESSING);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%c' does not match any 'Card Return/Retain Flag'", value));
            }
        }
    }

    public static DescriptiveOptional<CardFlag> forValue(int value) {
        return forValue((char) value);
    }


    public char getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return Character.toString(value);
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
