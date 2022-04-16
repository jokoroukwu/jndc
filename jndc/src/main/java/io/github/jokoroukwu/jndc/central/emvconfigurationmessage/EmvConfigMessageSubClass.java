package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum EmvConfigMessageSubClass implements NdcComponent {
    CURRENCY('1', "ICC Currency Data Objects Table ('1')"),
    TRANSACTION('2', "ICC Transaction Data Objects Table ('2')"),
    LANGUAGE_SUPPORT('3', "ICC Language Support Table ('3')"),
    TERMINAL_DATA_OBJECTS('4', "ICC Terminal Data Objects Table ('4')"),
    TERMINAL_ACCEPTABLE_AIDS('5', "ICC Terminal Acceptable AIDs Table ('5')");

    private final char value;
    private final String displayedName;

    EmvConfigMessageSubClass(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<EmvConfigMessageSubClass> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(CURRENCY);
            case '2':
                return DescriptiveOptional.of(TRANSACTION);
            case '3':
                return DescriptiveOptional.of(LANGUAGE_SUPPORT);
            case '4':
                return DescriptiveOptional.of(TERMINAL_DATA_OBJECTS);
            case '5':
                return DescriptiveOptional.of(TERMINAL_ACCEPTABLE_AIDS);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("'%c' does not match any 'EMV Configuration Message Sub-Class'", value));
            }
        }
    }

    public static DescriptiveOptional<EmvConfigMessageSubClass> forValue(int value) {
        return forValue((char) value);
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return Character.toString(value);
    }

    public char value() {
        return value;
    }
}
