package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class Bin implements NdcComponent {
    private final int binNumber;
    private final int chequesDeposited;

    public Bin(int binNumber, int chequesDeposited) {
        this.binNumber = Integers.validateRange(binNumber, 1, 9, "'Bin Number'");
        this.chequesDeposited = Integers.validateRange(chequesDeposited, 0, 99999, "'Cheques deposited in bin'");
    }

    Bin(int binNumber, int chequesDeposited, Void unused) {
        this.binNumber = binNumber;
        this.chequesDeposited = chequesDeposited;
    }

    public int getBinNumber() {
        return binNumber;
    }

    public int getChequesDeposited() {
        return chequesDeposited;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(6)
                .append(binNumber)
                .appendZeroPadded(chequesDeposited, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bin.class.getSimpleName() + ": {", "}")
                .add("binNumber: " + binNumber)
                .add("chequesDeposited: " + chequesDeposited)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bin bin = (Bin) o;
        return binNumber == bin.binNumber && chequesDeposited == bin.chequesDeposited;
    }

    @Override
    public int hashCode() {
        return Objects.hash(binNumber, chequesDeposited);
    }
}
