package io.github.jokoroukwu.jndc.util;



public final class ArrayUtils {
    private ArrayUtils() {
        throw new InstantiationError(getClass() + " is for static use only");
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
