package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

public class Fits {
    private Fits() {
    }

    public static boolean isDigitPairValid(int digitPair, int min, int max) {
        final int firstDigit = digitPair >> 4;
        final int secondDigit = digitPair & 0b00001111;

        return (firstDigit >= min && firstDigit <= max) && (secondDigit >= min && secondDigit <= max);
    }

    public static int validateDigitPair(int digitPair, int min, int max, String fieldName) {
        if (isDigitPairValid(digitPair, min, max)) {
            return digitPair;
        }
        throw new IllegalArgumentException(String.format("%s digits should be in range 0x%X-0x%X but was: 0x%X",
                fieldName, min, max, digitPair));
    }

    public static CharSequence toThreeDigitDecimalString(long value, int numberOfBytes) {
        final NdcStringBuilder builder = new NdcStringBuilder(numberOfBytes * 3);
        long mask = 0xFFL;
        for (int i = numberOfBytes - 1; i >= 0; i--) {
            final int bitsShifted = i * Byte.SIZE;
            final int nextByte = (int) ((value >> bitsShifted) & mask);
            builder.appendZeroPadded(nextByte, 3);
        }
        return builder;
    }
}
