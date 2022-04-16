package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValues;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class CoinCounters implements NdcComponent {
    private final GroupedCounterValues coinsRemaining;
    private final GroupedCounterValues coinsDispensed;
    private final GroupedCounterValues lastTransactionCoinsDispensed;

    public CoinCounters(GroupedCounterValues coinsRemaining, GroupedCounterValues coinsDispensed, GroupedCounterValues lastTransactionCoinsDispensed) {
        this.coinsRemaining = ObjectUtils.validateNotNull(coinsRemaining, "'Coins remaining'");
        this.coinsDispensed = ObjectUtils.validateNotNull(coinsDispensed, "'Coins dispensed'");
        this.lastTransactionCoinsDispensed = ObjectUtils.validateNotNull(lastTransactionCoinsDispensed,
                "'Last transaction coins dispensed");
    }
     CoinCounters(GroupedCounterValues coinsRemaining,
                        GroupedCounterValues coinsDispensed,
                        GroupedCounterValues lastTransactionCoinsDispensed,
                        Void unused) {
        this.coinsRemaining = coinsRemaining;
        this.coinsDispensed = coinsDispensed;
        this.lastTransactionCoinsDispensed = lastTransactionCoinsDispensed;
    }

    public GroupedCounterValues getCoinsRemaining() {
        return coinsRemaining;
    }

    public GroupedCounterValues getCoinsDispensed() {
        return coinsDispensed;
    }

    public GroupedCounterValues getLastTransactionCoinsDispensed() {
        return lastTransactionCoinsDispensed;
    }

    @Override
    public String toNdcString() {
        //  exact capacity can be evaluated
        return new NdcStringBuilder(60)
                .appendComponent(coinsRemaining)
                .appendComponent(coinsDispensed)
                .appendComponent(lastTransactionCoinsDispensed)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CoinCounters.class.getSimpleName() + ": {", "}")
                .add("coinsRemaining: " + coinsRemaining)
                .add("coinsDispensed: " + coinsDispensed)
                .add("lastTransactionCoinsDispensed: " + lastTransactionCoinsDispensed)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinCounters that = (CoinCounters) o;
        return coinsRemaining.equals(that.coinsRemaining) &&
                coinsDispensed.equals(that.coinsDispensed) &&
                lastTransactionCoinsDispensed.equals(that.lastTransactionCoinsDispensed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed);
    }
}
