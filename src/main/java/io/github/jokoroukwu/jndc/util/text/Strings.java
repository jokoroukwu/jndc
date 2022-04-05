package io.github.jokoroukwu.jndc.util.text;

import io.github.jokoroukwu.jndc.util.ArrayUtils;
import io.github.jokoroukwu.jndc.util.ObjectUtils;


public final class Strings {
    public static final String EMPTY_STRING = "";

    private Strings() {
        throw new InstantiationError(getClass() + " is for static use only");
    }

    public static String leftPad(Object object, Object padding, int totalLength) {
        return doPad(object, padding, totalLength, true);
    }

    public static String rightPad(Object object, Object padding, int totalLength) {
        return doPad(object, padding, totalLength, false);
    }

    private static String doPad(Object object, Object padding, int totalLength, boolean leftPad) {
        final String text = String.valueOf(object);
        final int textLength = text.length();
        if (textLength >= totalLength) {
            return text;
        }
        final String paddingString = String.valueOf(padding);
        final int numberOfPaddings = totalLength - textLength;
        final StringBuilder builder = new StringBuilder(paddingString.length() * numberOfPaddings + textLength);
        if (leftPad) {
            for (int i = 0; i < numberOfPaddings; i++) {
                builder.append(paddingString);
            }
            return builder.append(text).toString();
        }
        builder.append(text);
        for (int i = 0; i < numberOfPaddings; i++) {
            builder.append(paddingString);
        }
        return builder.toString();
    }

    public static String format(String string, Object... args) {
        if (!ArrayUtils.isNullOrEmpty(args)) {
            return String.format(string, args);
        }
        return string;
    }

    public static String validateNotNullNotEmpty(String value) {
        return validateNotNullNotEmpty(value, "String value");
    }

    public static String validateNotNullNotEmpty(String value, String fieldName) {
        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String validateLength(String value, int expectedLength, String fieldName) {
        ObjectUtils.validateNotNull(value, fieldName);
        if (value.length() == expectedLength) {
            return value;
        }
        final String template = "%s string length should be %d but was %d for value '%s'";
        throw new IllegalArgumentException(String.format(template, fieldName, expectedLength, value.length(), value));
    }

    public static String validateLength(String value, int expectedLength, int expectedLength2, String fieldName) {
        ObjectUtils.validateNotNull(value, fieldName);
        final int length = value.length();
        if (length == expectedLength || length == expectedLength2) {
            return value;
        }
        final String template = "%s string length should be %d or %d but was %d for value '%s'";
        throw new IllegalArgumentException(String.format(template, fieldName, expectedLength, expectedLength2, length, value));
    }

    public static String validateLengthRange(String value, int min, int max, String fieldName) {
        ObjectUtils.validateNotNull(value, fieldName);
        final int length = value.length();
        if (length >= min && length <= max) {
            return value;
        }
        final String template = "%s string length should be within range %d-%d but was %d for value '%s'";
        throw new IllegalArgumentException(String.format(template, fieldName, min, max, length, value));
    }

    public static String validateIsEmpty(String value, String fieldName) {
        if (value.isEmpty()) {
            return value;
        }
        throw new IllegalArgumentException(fieldName + " should be empty");
    }

    public static String validateCharRange(String value, int min, int max, String fieldName) {
        if (isWithinCharRange(value, min, max)) {
            return value;
        }
        final String errorMessage = "%s characters should be within range '%c'-'%c' but actual value was: %s";
        throw new IllegalArgumentException(String.format(errorMessage, fieldName, min, max, value));
    }

    public static boolean isWithinCharRange(String value, int beginOffset, int endOffset, int min, int max) {
        if (!value.isEmpty()) {
            if (endOffset < beginOffset) {
                throw new StringIndexOutOfBoundsException(String.format("Upper bound (%d) is less that lower bound (%d)",
                        beginOffset, endOffset));
            }
            for (; beginOffset < endOffset; beginOffset++) {
                final char nextChar = value.charAt(beginOffset);
                if (nextChar < min || nextChar > max) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isWithinCharRange(String value, int min, int max) {
        return isWithinCharRange(value, 0, value.length(), min, max);
    }


    public static boolean isWithinCharRange(String value, int beginOffset, int min, int max) {
        return isWithinCharRange(value, beginOffset, value.length(), min, max);
    }

    public static boolean isDecimal(String value) {
        return isDecimal(value, 0, value.length());
    }

    public static boolean isDecimal(String value, int beginOffset, int endOffset) {
        return isWithinCharRange(value, beginOffset, endOffset, '0', '9');
    }

    public static boolean isHex(String value) {
        for (int i = 0, k = value.length(); i < k; i++) {
            final char nextChar = value.charAt(i);
            if (nextChar < '0' || (nextChar > '9' && nextChar < 'A') || (nextChar > 'F' && nextChar < 'a') || nextChar > 'f') {
                return false;
            }
        }
        return true;
    }

    public static String validateIsHex(String value, String fieldName) {
        if (value != null && isHex(value)) {
            return value;
        }
        final String errorMessage = String.format("%s should be hexadecimal but was '%s'", fieldName, value);
        throw new IllegalArgumentException(errorMessage);
    }

    public static boolean isAlphanumeric(String value) {
        for (int i = 0, k = value.length(); i < k; i++) {
            final char nextChar = value.charAt(i);
            final boolean isWithinRange = (nextChar >= '0' && nextChar <= '9')
                    || (nextChar >= 'A' && nextChar <= 'Z')
                    || (nextChar >= 'a' && nextChar <= 'z');
            if (!isWithinRange) {
                return false;
            }
        }
        return true;
    }

}
