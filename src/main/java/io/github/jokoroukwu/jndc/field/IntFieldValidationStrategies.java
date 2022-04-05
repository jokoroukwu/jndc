package io.github.jokoroukwu.jndc.field;

import java.util.Optional;

public final class IntFieldValidationStrategies {
    public static IntFieldValidationStrategy NO_VALIDATION = val -> Optional.empty();

    private IntFieldValidationStrategies() {
    }

    public static IntFieldValidationStrategy exactValueValidation(int expectedValue) {
        return val -> val != expectedValue
                ? Optional.of("expected value to be exactly " + expectedValue + " but was " + val)
                : Optional.empty();
    }
}
