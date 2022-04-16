package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class CoinDispenserDataGroup extends LimitedSizeList<CoinHopper> implements IdentifiableCounterGroup {
    public static final int MAX_SIZE = 8;
    public static final char ID = 'E';

    CoinDispenserDataGroup(List<CoinHopper> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public CoinDispenserDataGroup(Collection<? extends CoinHopper> collection) {
        super(MAX_SIZE, collection);
    }

    public CoinDispenserDataGroup() {
        super(MAX_SIZE, MAX_SIZE);
    }

    public CoinDispenserDataGroup(int capacity) {
        super(MAX_SIZE, capacity);
    }


    public static CoinDispenserDataGroup of(CoinHopper... coinHoppers) {
        final CoinDispenserDataGroup coinDispenserDataGroup = new CoinDispenserDataGroup(coinHoppers.length);
        Collections.addAll(coinDispenserDataGroup, coinHoppers);
        return coinDispenserDataGroup;
    }

    public static CoinDispenserDataGroup unmodifiable(CoinDispenserDataGroup coinDispenserDataGroup) {
        return new CoinDispenserDataGroup(List.copyOf(coinDispenserDataGroup));
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            throw new IllegalStateException("Coin dispenser data group ‘E’ cannot be empty");
        }
        return new NdcStringBuilder(1 + 22 * size())
                .append(ID)
                .appendComponents(this)
                .toString();
    }

    @Override
    public String toString() {
        final String prefix = CoinDispenserDataGroup.class.getSimpleName() + ": {id: '" + ID + "', coinHoppers: [";
        final StringJoiner joiner = new StringJoiner(", ", prefix, "]}");
        for (CoinHopper hopper : this) {
            joiner.add(hopper.toString());
        }
        return joiner.toString();
    }

    @Override
    protected void performElementChecks(CoinHopper element) {
        //  no additional checks
    }

}
