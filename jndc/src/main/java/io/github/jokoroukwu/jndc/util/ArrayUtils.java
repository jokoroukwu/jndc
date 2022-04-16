package io.github.jokoroukwu.jndc.util;


public final class ArrayUtils {
    private ArrayUtils() {
        throw new InstantiationError(getClass() + " is for static use only");
    }

    public static byte[] validateLength(byte[] bytes, int expectedLength, String fieldName) {
        if (ObjectUtils.validateNotNull(bytes, fieldName).length == expectedLength) {
            return bytes;
        }
        throw new IllegalArgumentException(String.format("%s length should be %d but was %d", fieldName, expectedLength,
                bytes.length));
    }

    public static String toDecimalString(byte[] bytes) {
        ObjectUtils.validateNotNull(bytes, "Byte array");
        final StringBuilder builder = new StringBuilder(bytes.length * 3);
        for (byte aByte : bytes) {
            builder.append(Integers.toZeroPaddedString(aByte & 0xFF, 3));
        }
        return builder.toString();
    }

    public static boolean isEmpty(Object[] array) {
        return array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array.length == 0;
    }

    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || isEmpty(array);
    }

    public static boolean isNullOrEmpty(int[] array) {
        return array == null || isEmpty(array);
    }

    public static boolean isNullOrEmpty(long[] array) {
        return array == null || isEmpty(array);
    }

    public static boolean isNullOrEmpty(byte[] array) {
        return array == null || isEmpty(array);
    }

    public static boolean isNullOrEmpty(char[] array) {
        return array == null || isEmpty(array);
    }

}
