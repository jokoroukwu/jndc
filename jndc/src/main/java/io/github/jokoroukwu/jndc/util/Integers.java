package io.github.jokoroukwu.jndc.util;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import io.github.jokoroukwu.jndc.util.text.Strings;

public final class Integers {

    private Integers() {
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static int validateNotNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("%s must not be negative but was: %d", fieldName, value));
        }
        return value;
    }

    public static int validateHexRangeOrExactValue(int actual, int min, int max, int altValue, String fieldName) {
        if (actual == altValue || (actual >= min && actual <= max)) {
            return actual;
        }
        throw new IllegalArgumentException(String.format("%s should be equal to 0x%X or be in range 0x%X-0x%X, but was: 0x%X",
                fieldName, altValue, min, max, actual));
    }

    public static int validateMaxValue(int value, int maxValue, String fieldName) {
        if (value > maxValue) {
            throw new IllegalArgumentException(String.format("%s should not be greater than %d but was: %d",
                    fieldName, maxValue, value));
        }
        return value;
    }

    public static int validateMinValue(int value, int minValue, String fieldName) {
        if (value < minValue) {
            throw new IllegalArgumentException(String.format("%s must be greater than %d but was: %d", fieldName,
                    minValue, value));
        }
        return value;
    }

    public static int validateRange(int value, int min, int max, String fieldName) {
        if (isInRange(value, min, max)) {
            return value;
        }
        throw new IllegalArgumentException(String.format("%s should be in range %d-%d, but was: %d",
                fieldName, min, max, value));

    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static int validateIsExactHexValue(int value, int expected, String fieldName) {
        if (value != expected) {
            throw new IllegalArgumentException(String.format("%s should be %#X, but was: %#X", fieldName, expected, value));
        }
        return value;
    }

    public static int validateIsExactValue(int value, int expected, String fieldName) {
        if (value != expected) {
            throw new IllegalArgumentException(String.format("%s should be %d, but was: %d", fieldName, expected, value));
        }
        return value;
    }

    public static int validateHexRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s should be in range 0x%X-0x%X, but was: 0x%X",
                    fieldName, min, max, value));
        }
        return value;
    }

    public static int validateIsEven(int value, String fieldName) {
        if (!isEven(value)) {
            throw new IllegalArgumentException(String.format("'%s' should be even but was: %d", fieldName, value));
        }
        return value;
    }

    public static int validateIsPositive(int value, String fieldName) {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(String.format("'%s' should be positive but was: %d", fieldName, value));
    }

    public static boolean isEven(int value) {
        return value % 2 == 0;
    }

    public static String toZeroPaddedHexString(int value, int stringLength) {
        return toZeroPaddedString(value, stringLength, 16);
    }

    public static String toZeroPaddedString(int value, int stringLength) {
        return toZeroPaddedString(value, stringLength, 10);
    }

    public static String toZeroPaddedString(int value, int stringLength, int radix) {
        return Strings.leftPad(Integer.toString(value, radix).toUpperCase(), "0", stringLength);
    }


    public static String toEvenLengthHexString(int value) {
        final String stringValue = Integer.toHexString(value).toUpperCase();
        return stringValue.length() % 2 == 0 ? stringValue : '0' + stringValue;
    }

    public static DescriptiveOptionalInt tryParseInt(String value, int radix) {
        ObjectUtils.validateNotNull(value, "value");
        try {
            return DescriptiveOptionalInt.of(Integer.parseInt(value, radix));
        } catch (NumberFormatException e) {
            return DescriptiveOptionalInt.empty(e::toString);
        }
    }

    public static DescriptiveOptionalInt tryParseInt(String value) {
        return tryParseInt(value, 10);
    }

    public static DescriptiveOptionalInt tryParseHexInt(String value) {
        return tryParseInt(value, 16);
    }

    public static boolean isNegative(int value) {
        return value < 0;
    }

    public static boolean isNotNegative(int value) {
        return value >= 0;
    }

}
