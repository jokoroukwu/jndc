package io.github.jokoroukwu.jndc.central.datacommand.customisationdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum MessageId implements NdcComponent {
    SCREEN_KEYBOARD_DATA('1', "Screen and/or Keyboard Data ('1')"),
    STATE_TABLE('2', "State Table ('2')"),
    CONFIG_DATA('3', "Configuration Data ('3')"),
    ENHANCED_CONFIG_DATA('A', "Enhanced Configuration Data ('A')"),
    FIT_DATA('5', "FIT Data ('5')"),
    CONFIG_ID_NUMBER('6', "Configuration ID number ('6')"),
    DATE_AND_TIME('C', "Date and Time ('C')");

    private final char value;
    private final String displayedName;

    MessageId(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<MessageId> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(SCREEN_KEYBOARD_DATA);
            case '2':
                return DescriptiveOptional.of(STATE_TABLE);
            case '3':
                return DescriptiveOptional.of(CONFIG_DATA);
            case '5':
                return DescriptiveOptional.of(FIT_DATA);
            case '6':
                return DescriptiveOptional.of(CONFIG_ID_NUMBER);
            case 'A':
                return DescriptiveOptional.of(ENHANCED_CONFIG_DATA);
            case 'C':
                return DescriptiveOptional.of(DATE_AND_TIME);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("'%c' is not a valid 'Data Command Message Identifier'", value));
            }
        }
    }

    public static DescriptiveOptional<MessageId> forValue(int value) {
        return forValue((char) value);
    }


    public char value() {
        return value;
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }
}
