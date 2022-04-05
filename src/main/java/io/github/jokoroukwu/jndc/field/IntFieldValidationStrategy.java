package io.github.jokoroukwu.jndc.field;

import java.util.Optional;

public interface IntFieldValidationStrategy {

    Optional<String> validateFieldValue(int value);
}
