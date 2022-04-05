package io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collection;

public class CoinsToDispense extends LimitedSizeList<Integer> implements NdcComponent {
    public static final CoinsToDispense EMPTY = new CoinsToDispense(0, new ArrayList<>(0));
    public static final int MAX_SIZE = 99;

    public CoinsToDispense() {
        super(MAX_SIZE, 10);
    }

    public CoinsToDispense(Collection<? extends Integer> collection) {
        super(MAX_SIZE, collection);
    }

    public CoinsToDispense(int initCapacity) {
        super(MAX_SIZE, initCapacity);
    }

    CoinsToDispense(int maxSize, ArrayList<Integer> listDelegate) {
        super(maxSize, listDelegate);
    }

    public static CoinsToDispense of(int... values) {
        ObjectUtils.validateNotNull(values, "values");
        final CoinsToDispense coinsToDispense = new CoinsToDispense(values.length);
        for (int value : values) {
            coinsToDispense.add(value);
        }
        return coinsToDispense;
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            return Strings.EMPTY_STRING;
        }
        //  initial capacity can be evaluated to exact number
        final NdcStringBuilder messageBuilder = new NdcStringBuilder(size() * 2);
        for (Integer value : this) {
            messageBuilder.appendZeroPadded(value, 2);
        }
        return messageBuilder.toString();
    }

    @Override
    protected void performElementChecks(Integer element) {
        Integers.validateRange(element, 0, 99, "Number of coins");
    }
}
