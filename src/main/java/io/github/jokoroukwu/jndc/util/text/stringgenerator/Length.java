package io.github.jokoroukwu.jndc.util.text.stringgenerator;

import java.util.Random;

public final class Length {
    private Length() {
        throw new InstantiationError(getClass() + "is for static use only");
    }


    public static ILength fixed(int quantity) {
        return new Fixed(quantity);
    }

    public static ILength range(int min, int max) {
        return new Range(min, max);
    }

    public static ILength range(int max) {
        return range(1, max);
    }


    private static final class Fixed implements ILength {
        private final int length;

        Fixed(int length) {
            this.length = length;
        }

        @Override
        public int get(Random random) {
            return length;
        }

        @Override
        public int getMax() {
            return length;
        }
    }

    private static final class Range implements ILength {
        private final int min;
        private final int max;

        Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public int get(Random random) {
            return random.nextInt((int) (((long) max + 1) - min)) + min;
        }

        @Override
        public int getMax() {
            return max;
        }
    }
}
