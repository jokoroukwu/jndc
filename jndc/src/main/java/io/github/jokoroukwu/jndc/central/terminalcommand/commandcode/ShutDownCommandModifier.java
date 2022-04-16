package io.github.jokoroukwu.jndc.central.terminalcommand.commandcode;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

/**
 * This field can optionally be used when the Command Code is 2.
 * Defaults to zero if:
 * ‐ Any value other than zero or one is sent
 * ‐ The field is empty
 */
public enum ShutDownCommandModifier implements CommandModifier {
    STANDARD_SCREEN_DISPLAYED(0),
    TEMP_SCREEN_DISPLAYED(1);


    private final int value;

    ShutDownCommandModifier(int value) {
        this.value = value;
    }


    public static DescriptiveOptional<ShutDownCommandModifier> forValue(int value) {
        if (value == 1) {
            return DescriptiveOptional.of(TEMP_SCREEN_DISPLAYED);
        }
        if (value >= 0 && value <= 9) {
            return DescriptiveOptional.of(STANDARD_SCREEN_DISPLAYED);
        }
        var errorMessageTemplate = "value '%d' is not a valid 'Go-out-of-service' command modifier";
        return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, value));
    }

    @Override
    public int value() {
        return value;
    }
}
