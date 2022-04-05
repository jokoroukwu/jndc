package io.github.jokoroukwu.jndc.util;

import io.github.jokoroukwu.jndc.util.text.Strings;

public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("%s cannot be null", fieldName));
        }
        return value;
    }

    public static <T> T validateNotNull(T value) {
        return validateNotNull(value, Strings.EMPTY_STRING);
    }
}
