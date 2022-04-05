package io.github.jokoroukwu.jndc.util;


public final class ByteUtils {

    private ByteUtils() {
        throw new InstantiationError(getClass() + " is for static use only");
    }


    public static boolean isWithinUnsignedRange(int value) {
        return value >= 0 && value <= 0xFF;
    }

    public static int validateIsWithinUnsignedRange(int value, String fieldName) {
        if (isWithinUnsignedRange(value)) {
            return value;
        }
        throw new IllegalArgumentException(String.format("%s should be in range 0x00-0xFF but was 0x%X", fieldName, value));
    }

    public static int numberOfOctets(int value) {
        if ((value & 0xFF_00_00_00) != 0) {
            return 4;
        }
        if ((value & 0xFF_00_00) != 0) {
            return 3;
        }
        if ((value & 0xFF_00) != 0) {
            return 2;
        }
        return 1;
    }

    public static int numberOfOctets(long value) {
        if ((value & 0xFF_00_00_00_00_00_00_00L) != 0) {
            return 8;
        }
        if ((value & 0xFF_00_00_00_00_00_00L) != 0) {
            return 7;
        }
        if ((value & 0xFF_00_00_00_00_00L) != 0) {
            return 6;
        }
        if ((value & 0xFF_00_00_00_00L) != 0) {
            return 5;
        }
        if ((value & 0xFF_00_00_00L) != 0) {
            return 4;
        }
        if ((value & 0xFF_00_00L) != 0) {
            return 3;
        }
        if ((value & 0xFF_00L) != 0) {
            return 2;
        }
        return 1;
    }
}
