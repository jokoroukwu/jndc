package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.StringJoiner;

public class CoinHopper implements NdcComponent {
    private final int hopperTypeNumber;
    private final int coinsRemaining;
    private final int coinsDispensed;
    private final int lastTransactionCoinsDispensed;
    private final int coinsDeposited;

    public CoinHopper(int hopperTypeNumber,
                      int coinsRemaining,
                      int coinsDispensed,
                      int lastTransactionCoinsDispensed,
                      int coinsDeposited) {
        this.hopperTypeNumber = Integers.validateRange(hopperTypeNumber, 1, 8, "'Hopper Type Number'");
        this.coinsRemaining = Integers.validateRange(coinsRemaining, 0, 99999, "'Coins Remaining'");
        this.coinsDispensed = Integers.validateRange(coinsDispensed, 0, 99999, "'Coins Dispensed'");
        this.lastTransactionCoinsDispensed = Integers.validateRange(lastTransactionCoinsDispensed, 0, 99999,
                "'Last Transaction Coins Dispensed'");
        this.coinsDeposited = Integers.validateMaxValue(coinsDeposited, 99999, "'Coins Deposited'");
    }

    CoinHopper(int hopperTypeNumber,
               int coinsRemaining,
               int coinsDispensed,
               int lastTransactionCoinsDispensed,
               int coinsDeposited,
               Void unused) {
        this.hopperTypeNumber = hopperTypeNumber;
        this.coinsRemaining = coinsRemaining;
        this.coinsDispensed = coinsDispensed;
        this.lastTransactionCoinsDispensed = lastTransactionCoinsDispensed;
        this.coinsDeposited = coinsDeposited;
    }

    public static CoinHopperBuilder builder() {
        return new CoinHopperBuilder();
    }

    public CoinHopperBuilder copy() {
        return new CoinHopperBuilder()
                .withHopperTypeNumber(hopperTypeNumber)
                .withCoinsRemaining(coinsRemaining)
                .withCoinsDispensed(coinsDispensed)
                .withLastTransactionCoinsDispensed(lastTransactionCoinsDispensed)
                .withCoinsDeposited(coinsDeposited);
    }


    public int getHopperTypeNumber() {
        return hopperTypeNumber;
    }

    public int getCoinsRemaining() {
        return coinsRemaining;
    }

    public int getCoinsDispensed() {
        return coinsDispensed;
    }

    public int getLastTransactionCoinsDispensed() {
        return lastTransactionCoinsDispensed;
    }

    public OptionalInt getCoinsDeposited() {
        return coinsDeposited >= 0 ? OptionalInt.of(coinsDeposited) : OptionalInt.empty();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(23)
                .appendZeroPadded(hopperTypeNumber, 2)
                .appendZeroPadded(coinsRemaining, 5)
                .appendZeroPadded(coinsDispensed, 5)
                .appendZeroPadded(lastTransactionCoinsDispensed, 5)
                .appendZeroPadded(coinsDeposited, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CoinDispenserDataGroup.class.getSimpleName() + ": {", "}")
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
        CoinHopper that = (CoinHopper) o;
        return hopperTypeNumber == that.hopperTypeNumber &&
                coinsRemaining == that.coinsRemaining &&
                coinsDispensed == that.coinsDispensed &&
                lastTransactionCoinsDispensed == that.lastTransactionCoinsDispensed &&
                ((coinsDeposited < 0 && that.coinsDeposited < 0) || coinsDeposited == that.coinsDeposited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hopperTypeNumber, coinsRemaining, coinsDispensed, lastTransactionCoinsDispensed,
                Math.max(coinsDeposited, -1));
    }
}
