package io.github.jokoroukwu.jndc.util;


import io.github.jokoroukwu.jndc.NdcComponent;

import java.util.Iterator;
import java.util.stream.IntStream;

public class NdcStringBuilder implements Appendable, CharSequence {
    private final StringBuilder builderDelegate;

    public NdcStringBuilder(int capacity) {
        this.builderDelegate = new StringBuilder(capacity);
    }

    public NdcStringBuilder() {
        this.builderDelegate = new StringBuilder();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builderDelegate.subSequence(start, end);
    }

    @Override
    public IntStream codePoints() {
        return builderDelegate.codePoints();
    }

    public NdcStringBuilder appendComponent(NdcComponent ndcComponent) {
        String value;
        if (ndcComponent != null && !(value = ndcComponent.toNdcString()).isEmpty()) {
            builderDelegate.append(value);
        }
        return this;
    }

    public NdcStringBuilder appendComponent(String prefix, NdcComponent ndcComponent) {
        String value;
        if (ndcComponent != null && !(value = ndcComponent.toNdcString()).isEmpty()) {
            append(prefix).append(value);
        }
        return this;
    }

    public NdcStringBuilder appendFieldGroup(NdcComponent ndcComponent) {
        return appendFieldGroup(ndcComponent, NdcConstants.FIELD_SEPARATOR);
    }

    public NdcStringBuilder appendFieldGroup(NdcComponent ndcComponent, char separator) {
        if (ndcComponent != null) {
            builderDelegate.append(separator).append(ndcComponent.toNdcString());
        }
        return this;
    }

    public NdcStringBuilder appendFieldGroup(Iterable<? extends NdcComponent> ndcComponents) {
        if (ndcComponents != null) {
            final Iterator<? extends NdcComponent> iterator = ndcComponents.iterator();
            if (iterator.hasNext()) {
                appendFs();
                do {
                    appendComponent(iterator.next());
                } while (iterator.hasNext());
            }
        }
        return this;
    }

    public NdcStringBuilder appendFieldGroup(String value) {
        if (!value.isEmpty()) {
            builderDelegate.append(NdcConstants.FIELD_SEPARATOR).append(value);
        }
        return this;
    }

    public NdcStringBuilder append(String prefix, String value) {
        if (!value.isEmpty()) {
            builderDelegate.append(prefix).append(value);
        }
        return this;
    }

    public NdcStringBuilder appendFs() {
        return append(NdcConstants.FIELD_SEPARATOR);
    }

    public NdcStringBuilder appendFs(int count) {
        for (int i = 0; i < count; i++) {
            builderDelegate.append(NdcConstants.FIELD_SEPARATOR);
        }
        return this;
    }


    public NdcStringBuilder appendGs() {
        return append(NdcConstants.GROUP_SEPARATOR);
    }

    public NdcStringBuilder appendGs(int count) {
        for (int i = 0; i < count; i++) {
            builderDelegate.append(NdcConstants.GROUP_SEPARATOR);
        }
        return this;
    }


    public NdcStringBuilder appendZeroPadded(int value, int stringLength) {
        if (value >= 0) {
            builderDelegate.append(Integers.toZeroPaddedString(value, stringLength));
        }
        return this;
    }

    public NdcStringBuilder appendZeroPaddedHex(int value, int stringLength) {
        if (value >= 0) {
            builderDelegate.append(Integers.toZeroPaddedHexString(value, stringLength));
        }
        return this;
    }

    public NdcStringBuilder appendEvenZeroPaddedHex(int value) {
        if (value >= 0) {
            builderDelegate.append(Integers.toEvenLengthHexString(value));
        }
        return this;
    }

    public NdcStringBuilder appendZeroPadded(long value, int stringLength) {
        if (value >= 0) {
            builderDelegate.append(Longs.toZeroPaddedString(value, stringLength));
        }
        return this;
    }

    public NdcStringBuilder appendZeroPaddedHex(long value, int stringLength) {
        if (value >= 0) {
            builderDelegate.append(Longs.toZeroPaddedHexString(value, stringLength));
        }
        return this;
    }


    public NdcStringBuilder append(String str) {
        if (!str.isEmpty()) {
            builderDelegate.append(str);
        }
        return this;
    }

    public NdcStringBuilder ensureCapacity(int capacity) {
        builderDelegate.ensureCapacity(capacity);
        return this;
    }

    public <T extends NdcComponent> NdcStringBuilder appendComponents(Iterable<T> iterable, String delimiter) {
        final Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return this;
        }
        append(iterator.next().toNdcString());
        while (iterator.hasNext()) {
            append(delimiter).append(iterator.next().toNdcString());
        }
        return this;
    }

    public NdcStringBuilder appendComponents(Iterable<? extends NdcComponent> ndcComponents) {
        for (NdcComponent ndcComponent : ndcComponents) {
            appendComponent(ndcComponent);
        }
        return this;
    }

    public NdcStringBuilder appendZeroPadded(Iterable<Integer> iterable, String delimiter, int stringLength) {
        final Iterator<Integer> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return this;
        }
        appendZeroPadded(iterator.next(), stringLength);
        while (iterator.hasNext()) {
            append(delimiter).appendZeroPadded(iterator.next(), stringLength);
        }
        return this;
    }

    public NdcStringBuilder appendEvenZeroPaddedHex(Iterable<Integer> iterable) {
        final Iterator<Integer> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            return this;
        }
        while (iterator.hasNext()) {
            appendEvenZeroPaddedHex(iterator.next());
        }
        return this;
    }


    @Override
    public NdcStringBuilder append(char c) {
        builderDelegate.append(c);
        return this;
    }


    @Override
    public NdcStringBuilder append(CharSequence s) {
        builderDelegate.append(s);
        return this;
    }

    @Override
    public NdcStringBuilder append(CharSequence s, int start, int end) {
        builderDelegate.append(s, start, end);
        return this;
    }

    public NdcStringBuilder append(double d) {
        builderDelegate.append(d);
        return this;
    }

    public NdcStringBuilder append(int d) {
        builderDelegate.append(d);
        return this;
    }


    @Override
    public String toString() {
        return builderDelegate.toString();
    }

    public int length() {
        return builderDelegate.length();
    }

    public void setLength(int newLength) {
        builderDelegate.setLength(newLength);
    }

    public char charAt(int index) {
        return builderDelegate.charAt(index);
    }

    public IntStream chars() {
        return builderDelegate.chars();
    }

}
