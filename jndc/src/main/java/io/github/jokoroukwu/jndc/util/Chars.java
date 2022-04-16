package io.github.jokoroukwu.jndc.util;

public final class Chars {
    private Chars() {
    }

    public static char validateRange(char value, int min, int max, String fieldName) {
        if (!isInRange(value, min, max)) {
            throw new IllegalArgumentException(String.format("'%s' should be within character range '%c'-'%c', but was: %c",
                    fieldName, min, max, value));
        }
        return value;
    }

    public static boolean isInRange(char value, int min, int max) {
        return value >= min && value <= max;
    }
}
