package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class TagContainer extends LimitedSizeList<Integer> implements NdcComponent {
    public static final int MAX_SIZE = 0xFF;
    public static final TagContainer EMPTY = new TagContainer(Collections.emptyList());


    private TagContainer(List<Integer> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public TagContainer(Collection<? extends Integer> collection) {
        super(MAX_SIZE, collection);
    }

    public TagContainer(int capacity) {
        super(MAX_SIZE, capacity);
    }

    public static TagContainer of(int... values) {
        final TagContainer container = new TagContainer(values.length);
        for (int value : values) {
            container.add(value);
        }
        return container;
    }

    @Override
    protected void performElementChecks(Integer element) {
        if (element < 0) {
            throw new IllegalArgumentException("Tag must be a non-negative value but was: " + element);
        }
    }

    @Override
    public String toNdcString() {
        //  total capacity can be evaluated to exact number
        return new NdcStringBuilder((4 * size()) + 2)
                .appendZeroPaddedHex(size(), 2)
                .appendEvenZeroPaddedHex(this)
                .toString();
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(", ", TagContainer.class.getSimpleName() + ": [", "]");
        for (Integer value : this) {
            joiner.add(Integers.toEvenLengthHexString(value));
        }
        return joiner.toString();
    }

}
