package io.github.jokoroukwu.jndc.central.terminalcommand.commandcode;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum SendConfigInfoModifier implements CommandModifier {

    HARDWARE_CONFIGURATION(1),
    SUPPLIES_DATA(2),
    FITNESS_DATA(3),
    TEMPER_AND_SENSOR_STATUS(4),
    SOFTWARE_ID_AND_RELEASE_NUMBER(5),
    ENHANCED_CONFIG_DATA(6),
    CONFIGURATION_OPTION_DIGITS(7),
    CASH_DEPOSIT_DEFINITION(8);

    private static final SendConfigInfoModifier[] values = values();
    private final int value;

    SendConfigInfoModifier(int value) {
        this.value = value;
    }

    public static DescriptiveOptional<SendConfigInfoModifier> forValue(int value) {
        for (var constant : values) {
            if (constant.value == value) {
                return DescriptiveOptional.of(constant);
            }
        }
        var errorMessageTemplate = "value '%d' is not a valid 'Configuration Information' command modifier";
        return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, value));
    }

    @Override
    public int value() {
        return value;
    }

}
