package io.github.jokoroukwu.jndc.util.text.stringgenerator;


import io.github.jokoroukwu.jndc.util.ArrayUtils;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.random.ThreadLocalSecureRandom;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;


public final class BmpStringGenerator implements IStringGenerator {
    public static final BmpStringGenerator HEX = ofCharacterRanges('0', '9', 'A', 'F');
    public static final BmpStringGenerator ALPHANUMERIC = ofCharacterRanges('0', '9', 'A', 'Z', 'a', 'z');
    public static final BmpStringGenerator DECIMAL = ofCharacterRange('0', '9');

    private final IRandomBmpCharSupplier[] charSuppliers;
    private final Random random;

    private BmpStringGenerator(IRandomBmpCharSupplier[] charSuppliers, Random random) {
        this.charSuppliers = charSuppliers;
        this.random = ObjectUtils.validateNotNull(random);
    }

    public static BmpStringGenerator ofCharacters(char first, char... more) {
        if (ArrayUtils.isNullOrEmpty(more)) {
            return getInstance(random -> first);
        }
        final char[] chars = new char[more.length + 1];
        chars[0] = first;
        System.arraycopy(more, 0, chars, 1, more.length);
        return getInstance(random -> chars[random.nextInt(chars.length)]);
    }

    public static BmpStringGenerator ofCharacterRange(int start, int endInclusive) {
        if (endInclusive < start) {
            throw new IllegalArgumentException(String.format("upper bound value %d is less than lower bound value %d",
                    endInclusive, start));
        }
        return getInstance(random -> (char) (random.nextInt((int) (((long) endInclusive + 1) - start)) + start));
    }

    public static BmpStringGenerator ofCharacterRanges(int... ranges) {
        for (int i = 0, rangesLength = ranges.length; i < rangesLength; ) {
            final int from = ranges[i++];
            final int upTo = ranges[i++];
            if (from > upTo) {
                final String template = "upper bound range value %d is less than lower bound range value %d";
                throw new IllegalArgumentException(String.format(template, from, upTo));
            }
        }
        return getInstance(random -> {
            int rangeStart = random.nextInt(ranges.length);
            if (rangeStart % 2 != 0) {
                --rangeStart;
            }
            final int from = ranges[rangeStart];
            final int upTo = ranges[rangeStart + 1];
            return (char) (random.nextInt((int) (((long) upTo + 1) - from)) + from);
        });
    }

    public static BmpStringGenerator getInstance(Random random, IRandomBmpCharSupplier first, IRandomBmpCharSupplier... other) {
        ObjectUtils.validateNotNull(random);
        ObjectUtils.validateNotNull(first);
        final IRandomBmpCharSupplier[] charSuppliers;
        if (other != null) {
            charSuppliers = new IRandomBmpCharSupplier[other.length + 1];
            System.arraycopy(other, 0, charSuppliers, 1, other.length);
        } else {
            charSuppliers = new IRandomBmpCharSupplier[1];
        }
        charSuppliers[0] = first;
        return new BmpStringGenerator(charSuppliers, ThreadLocalSecureRandom.get());
    }

    public static BmpStringGenerator getInstance(IRandomBmpCharSupplier first, IRandomBmpCharSupplier... other) {
        return getInstance(ThreadLocalSecureRandom.get(), first, other);
    }

    @Override
    public String fixedLength(int stringLength) {
        if (stringLength <= 0) {
            throw new IllegalArgumentException("String length should be positive");
        }
        final char[] randomText = new char[stringLength];
        final int suppliersLength = charSuppliers.length;
        for (int i = 0; i < stringLength; i++) {
            final IRandomBmpCharSupplier generator = charSuppliers[random.nextInt(suppliersLength)];
            randomText[i] = (generator.getChar(random));
        }
        return new String(randomText);
    }

    @Override
    public char randomChar() {
        return charSuppliers[random.nextInt(charSuppliers.length)].getChar(random);
    }

    @Override
    public String randomLength(int min, int max) {
        if (min < 1) {
            throw new IllegalArgumentException("String length lower bound must be equal or greater than 1");
        }
        final int actualLength = random.nextInt((int) (((long) max + 1) - min)) + min;
        return fixedLength(actualLength);
    }

    @Override
    public String strings(ILength numberOfSubstrings, ILength substringsLength, CharSequence delimiter) {
        ObjectUtils.validateNotNull(delimiter);
        ObjectUtils.validateNotNull(numberOfSubstrings);
        ObjectUtils.validateNotNull(substringsLength);

        final int capacity = (numberOfSubstrings.getMax() * (substringsLength.getMax() + delimiter.length())) - delimiter.length();
        final StringBuilder builder = new StringBuilder(capacity);
        for (int i = 0, k = numberOfSubstrings.get(random) - 1; i < k; i++) {
            final int length = substringsLength.get(random);
            builder.append(fixedLength(length)).append(delimiter);
        }
        builder.append(fixedLength(substringsLength.get(random)));
        return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final BmpStringGenerator generator = (BmpStringGenerator) object;
        return Arrays.equals(charSuppliers, generator.charSuppliers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(random);
        result = 31 * result + Arrays.hashCode(charSuppliers);
        return result;
    }
}
