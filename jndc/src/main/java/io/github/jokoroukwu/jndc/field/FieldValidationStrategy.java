package io.github.jokoroukwu.jndc.field;

import java.util.Optional;

public interface FieldValidationStrategy<T> {

    Optional<String> validateFieldValue(T fieldValue);
}
