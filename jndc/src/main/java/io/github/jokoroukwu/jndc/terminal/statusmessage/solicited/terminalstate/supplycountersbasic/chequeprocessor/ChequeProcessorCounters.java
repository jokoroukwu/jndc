package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.chequeprocessor;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collection;
import java.util.List;

public class ChequeProcessorCounters extends LimitedSizeList<Integer> implements NdcComponent {
    public static final ChequeProcessorCounters EMPTY = new ChequeProcessorCounters(List.of());
    public static final int MAX_SIZE = 10;


    ChequeProcessorCounters(List<Integer> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public ChequeProcessorCounters(Collection<? extends Integer> collection) {
        super(MAX_SIZE, collection);
    }

    public ChequeProcessorCounters(int capacity) {
        super(MAX_SIZE, capacity);
    }

    public ChequeProcessorCounters() {
        this(MAX_SIZE);
    }

    public static ChequeProcessorCounters of(int... values) {
        final ChequeProcessorCounters chequeProcessorCounters = new ChequeProcessorCounters(values.length);
        for (int value : values) {
            chequeProcessorCounters.add(value);
        }
        return chequeProcessorCounters;
    }

    public static ChequeProcessorCounters unmodifiable(ChequeProcessorCounters chequeProcessorCounters) {
        return new ChequeProcessorCounters(List.copyOf(chequeProcessorCounters));
    }

    @Override
    protected void performElementChecks(Integer element) {
        Integers.validateRange(element, 0, 99999, "Cheques deposited into BIN");
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(size() * 5)
                .appendZeroPadded(this, Strings.EMPTY_STRING, 5)
                .toString();
    }
}
