package io.github.jokoroukwu.jndc.util;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalLong;
import io.github.jokoroukwu.jndc.util.text.Strings;

public final class Longs {

    private Longs() {
    }

    public static long validateNotNegative(long value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("'%s' must not be negative but was: %d", fieldName, value));
        }
        return value;
    }

    public static long validateMaxValue(long value, long maxValue, String fieldName) {
        if (value > maxValue) {
            throw new IllegalArgumentException(String.format("'%s' should not be greater than %d but was: %d",
                    fieldName, maxValue, value));
        }
        return value;
    }

    public static long validateMaxHexValue(long value, long maxValue, String fieldName) {
        if (value > maxValue) {
            throw new IllegalArgumentException(String.format("'%s' should not be greater than %#X but was: %#X",
                    fieldName, maxValue, value));
        }
        return value;
    }

    public static long validateRange(long value, long min, long max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("'%s' should be in range %d-%d dec, but was: %d",
                    fieldName, min, max, value));
        }
        return value;
    }

    public static long validateHexRange(long value, long min, long max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("'%s' should be in range %#X-%#X hex, but was: %#X",
                    fieldName, min, max, value));
        }
        return value;
    }

    public static String toZeroPaddedHexString(long value, int stringLength) {
        return Strings.leftPad(Long.toHexString(value).toUpperCase(), "0", stringLength);
    }

    public static String toZeroPaddedString(long value, int stringLength) {
        return Strings.leftPad(Long.toString(value).toUpperCase(), "0", stringLength);
    }

    public static DescriptiveOptionalLong tryParseLong(String value, int radix) {
        ObjectUtils.validateNotNull(value, "value");
        try {
            return DescriptiveOptionalLong.of(Long.parseLong(value, radix));
        } catch (NumberFormatException e) {
            return DescriptiveOptionalLong.empty(e::toString);
        }
    }

    public static DescriptiveOptionalLong tryParseLong(String value) {
        return tryParseLong(value, 10);
    }

    public static DescriptiveOptionalLong tryParseHexLong(String value) {
        return tryParseLong(value, 16);
    }

    public static boolean isNotNegative(long value) {
        return value >= 0;
    }
}
