package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

/**
 * Identifies which terminal command has
 * been received.
 */
public enum TerminalStateMessageId implements NdcComponent {
    SEND_CONFIG_INFO('1', "Send configuration information ('1')"),

    SEND_SUPPLY_COUNTERS_BASIC('2', "Basic Send Supply Counters ('2')"),

    SEND_TALLY_INFO('3', "Send tally information ('3')"),

    SEND_ERROR_LOG_INFO('4', "Send error log information ('4')"),

    SEND_DATE_TIME_INFO('5', "Send date/time information ('5')"),

    SEND_CONFIG_ID('6', "Send configuration ID ('6')"),

    SEND_SUPPLY_COUNTERS_EXTENDED('7', "Extended Send Supply Counters ('7')"),

    RETRIEVE_HALLMARK_KEY('F', "EKC retrieve hallmark key ('F')"),

    HARDWARE_CONFIG_DATA('H', "Hardware configuration data ('H')"),

    SUPPLIES_DATA('I', "Supplies data ('I')"),

    FITNESS_DATA('J', "Fitness data ('J')"),

    TAMPER_AND_SENSOR_STATUS('K', "Tamper and sensor status data ('K')"),

    SOFTWARE_ID_AND_RELEASE_NUM('L', "Software ID and release number data ('L')"),

    LOCAL_CONFIG_OPTION_DIGITS('M', "Local configuration option digits ('M')"),

    REPORT_CASH_DEPOSIT_DEFINITION('N', "Report cash deposit definition ('N')");

    private final char value;
    private final String displayedName;

    TerminalStateMessageId(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<TerminalStateMessageId> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(SEND_CONFIG_INFO);
            case '2':
                return DescriptiveOptional.of(SEND_SUPPLY_COUNTERS_BASIC);
            case '3':
                return DescriptiveOptional.of(SEND_TALLY_INFO);
            case '4':
                return DescriptiveOptional.of(SEND_ERROR_LOG_INFO);
            case '5':
                return DescriptiveOptional.of(SEND_DATE_TIME_INFO);
            case '6':
                return DescriptiveOptional.of(SEND_CONFIG_ID);
            case '7':
                return DescriptiveOptional.of(SEND_SUPPLY_COUNTERS_EXTENDED);
            case 'F':
                return DescriptiveOptional.of(RETRIEVE_HALLMARK_KEY);
            case 'H':
                return DescriptiveOptional.of(HARDWARE_CONFIG_DATA);
            case 'I':
                return DescriptiveOptional.of(SUPPLIES_DATA);
            case 'J':
                return DescriptiveOptional.of(FITNESS_DATA);
            case 'K':
                return DescriptiveOptional.of(TAMPER_AND_SENSOR_STATUS);
            case 'L':
                return DescriptiveOptional.of(SOFTWARE_ID_AND_RELEASE_NUM);
            case 'M':
                return DescriptiveOptional.of(LOCAL_CONFIG_OPTION_DIGITS);
            case 'N':
                return DescriptiveOptional.of(REPORT_CASH_DEPOSIT_DEFINITION);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%c' is not a valid 'Terminal State Message Identifier'", value));
            }
        }
    }

    public static DescriptiveOptional<TerminalStateMessageId> forValue(int value) {
        return forValue((char) value);
    }


    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return displayedName;
    }

    public char getValue() {
        return value;
    }

}
