package io.github.jokoroukwu.jndc.central.terminalcommand.commandcode;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum CommandCode {
    START_UP("1", "Go in‐service ('1')"),
    SHUT_DOWN("2", "Go out‐of‐service ('2')"),
    SEND_CONFIG_ID("3", "Send configuration ID ('3')"),
    SEND_SUPPLY_COUNTERS("4", "Send supply counters ('4')"),
    SEND_TALLY_INFO("5", "Send tally information ('5')"),
    SEND_ERROR_LOG_INFO("6", "Send error log information ('6')"),
    SEND_CONFIG_INFO("7", "Send configuration information ('7')"),
    SEND_DATE_TIME_INFO("8", "Send date and time information ('8')"),
    DISCONNECT("F", "Disconnect ('F')"),
    NO_OP("G", "No‐Op ('G')");

    private final String value;
    private final String displayedName;

    CommandCode(String value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<CommandCode> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(START_UP);
            case '2':
                return DescriptiveOptional.of(SHUT_DOWN);
            case '3':
                return DescriptiveOptional.of(SEND_CONFIG_ID);
            case '4':
                return DescriptiveOptional.of(SEND_SUPPLY_COUNTERS);
            case '5':
                return DescriptiveOptional.of(SEND_TALLY_INFO);
            case '6':
                return DescriptiveOptional.of(SEND_ERROR_LOG_INFO);
            case '7':
                return DescriptiveOptional.of(SEND_CONFIG_INFO);
            case '8':
                return DescriptiveOptional.of(SEND_DATE_TIME_INFO);
            case 'F':
                return DescriptiveOptional.of(DISCONNECT);
            case 'G':
                return DescriptiveOptional.of(NO_OP);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Terminal Command Code'", value));
            }
        }
    }

    public static DescriptiveOptional<CommandCode> forValue(int value) {
        return forValue((char) value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
