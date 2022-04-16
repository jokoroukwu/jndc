package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collection;

public abstract class NotesToDispense extends LimitedSizeList<Integer> implements NdcComponent {
    public static final int MAX_SIZE = 7;

    NotesToDispense(ArrayList<Integer> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    NotesToDispense() {
        super(MAX_SIZE);
    }

    NotesToDispense(int initCapacity) {
        super(MAX_SIZE, initCapacity);
    }

    NotesToDispense(Collection<? extends Integer> collection) {
        super(MAX_SIZE, collection);
    }

    public static NotesToDispense twoDigit(int... values) {
        ObjectUtils.validateNotNull(values, "values");
        return populateNotes(new TwoDigitNotes(values.length), values);
    }

    public static NotesToDispense twoDigit() {
        return new TwoDigitNotes();
    }

    public static NotesToDispense fourDigit(int... values) {
        ObjectUtils.validateNotNull(values, "values");
        return populateNotes(new FourDigitNotes(values.length), values);
    }

    public static NotesToDispense fourDigit() {
        return new FourDigitNotes();
    }

    private static NotesToDispense populateNotes(NotesToDispense notesToDispense, int... values) {
        for (int value : values) {
            notesToDispense.add(value);
        }
        return notesToDispense;
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            return Strings.EMPTY_STRING;
        }
        //  total capacity cam be evaluated to exact number
        final int numberOfDigits = getNumberOfDigits();
        final NdcStringBuilder builder = new NdcStringBuilder(numberOfDigits * size());
        for (Integer value : this) {
            builder.appendZeroPadded(value, numberOfDigits);
        }
        return builder.toString();
    }

    protected abstract int getNumberOfDigits();
}
