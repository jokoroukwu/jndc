package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup;

import java.util.Objects;
import java.util.StringJoiner;

public class CoinHopperBuilder {
    private int hopperTypeNumber;
    private int coinsRemaining;
    private int coinsDispensed;
    private int lastTransactionCoinsDispensed;
    private int coinsDeposited = -1;

    public int getHopperTypeNumber() {
        return hopperTypeNumber;
    }

    public CoinHopperBuilder withHopperTypeNumber(int hopperTypeNumber) {
        this.hopperTypeNumber = hopperTypeNumber;
        return this;
    }

    public int getCoinsRemaining() {
        return coinsRemaining;
    }

    public CoinHopperBuilder withCoinsRemaining(int coinsRemaining) {
        this.coinsRemaining = coinsRemaining;
        return this;
    }

    public int getCoinsDispensed() {
        return coinsDispensed;
    }

    public CoinHopperBuilder withCoinsDispensed(int coinsDispensed) {
        this.coinsDispensed = coinsDispensed;
        return this;
    }

    public int getLastTransactionCoinsDispensed() {
        return lastTransactionCoinsDispensed;
    }

    public CoinHopperBuilder withLastTransactionCoinsDispensed(int lastTransactionCoinsDispensed) {
        this.lastTransactionCoinsDispensed = lastTransactionCoinsDispensed;
        return this;
    }

    public int getCoinsDeposited() {
        return coinsDeposited;
    }

    public CoinHopperBuilder withCoinsDeposited(int coinsDeposited) {
        this.coinsDeposited = coinsDeposited;
        return this;
    }

    public CoinHopper build() {
        return new CoinHopper(hopperTypeNumber, coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed, coinsDeposited);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", CoinHopperBuilder.class.getSimpleName() + ": {", "}")
                .add("hopperTypeNumber: " + hopperTypeNumber)
                .add("coinsRemaining: " + coinsRemaining)
                .add("coinsDispensed: " + coinsDispensed)
                .add("lastTransactionCoinsDispensed: " + lastTransactionCoinsDispensed)
                .add("coinsDeposited: " + coinsDeposited)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinHopperBuilder that = (CoinHopperBuilder) o;
        return hopperTypeNumber == that.hopperTypeNumber &&
                coinsRemaining == that.coinsRemaining &&
                coinsDispensed == that.coinsDispensed &&
                lastTransactionCoinsDispensed == that.lastTransactionCoinsDispensed &&
                ((coinsDeposited < 0 && that.coinsDeposited < 0) || coinsDeposited == that.coinsDeposited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hopperTypeNumber, coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed, coinsDeposited);
    }
}
