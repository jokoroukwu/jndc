package io.github.jokoroukwu.jndc.central.terminalcommand.commandcode;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

/**
 * This field can optionally be used when the Command Code is 4
 */
public enum SendSupplyCountersModifier implements CommandModifier {
    SEND_BASIC_MESSAGE(1),
    SEND_EXTENDED_MESSAGE(2);

    private final int value;

    SendSupplyCountersModifier(int value) {
        this.value = value;
    }

    public static DescriptiveOptional<SendSupplyCountersModifier> forValue(int value) {
        switch (value) {
            case 1:
                return DescriptiveOptional.of(SEND_BASIC_MESSAGE);
            case 2:
                return DescriptiveOptional.of(SEND_EXTENDED_MESSAGE);
            default: {
                var errorMessageTemplate = "value '%s' is not a valid 'Send supply counters' command modifier";
                return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, value));
            }
        }
    }

    @Override
    public int value() {
        return value;
    }
}
