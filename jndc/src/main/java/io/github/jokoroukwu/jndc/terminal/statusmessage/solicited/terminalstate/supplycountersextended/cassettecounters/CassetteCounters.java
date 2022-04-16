package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup.CassetteCountersBuilder;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.StringJoiner;

public class CassetteCounters implements NdcComponent {
    private final int cassetteType;
    private final int notesInCassette;
    private final int notesRejected;
    private final int notesDispensed;
    private final int lastTransactionNotesDispensed;
    private final int notesDeposited;

    CassetteCounters(int cassetteType,
                     int notesInCassette,
                     int notesRejected,
                     int notesDispensed,
                     int lastTransactionNotesDispensed,
                     int notesDeposited) {
        this.cassetteType = cassetteType;
        this.notesInCassette = Integers.validateRange(notesInCassette, 0, 99999, "'Notes In Cassette'");
        this.notesRejected = Integers.validateRange(notesRejected, 0, 99999, "'Notes Rejected'");
        this.notesDispensed = Integers.validateRange(notesDispensed, 0, 99999, "'Notes Dispensed'");
        this.lastTransactionNotesDispensed = Integers.validateRange(lastTransactionNotesDispensed, 0, 99999,
                "'Last Transaction Notes Dispensed'");
        this.notesDeposited = Integers.validateMaxValue(notesDeposited, 99999, "'Notes Deposited'");
    }

    CassetteCounters(int cassetteType,
                     int notesInCassette,
                     int notesRejected,
                     int notesDispensed,
                     int lastTransactionNotesDispensed,
                     int notesDeposited,
                     Void unused) {
        this.cassetteType = cassetteType;
        this.notesRejected = notesRejected;
        this.notesDispensed = notesDispensed;
        this.lastTransactionNotesDispensed = lastTransactionNotesDispensed;
        this.notesDeposited = notesDeposited;
        this.notesInCassette = notesInCassette;
    }

    public static CassetteCounters cashHandler(int cassetteType,
                                               int notesInCassette,
                                               int notesRejected,
                                               int notesDispensed,
                                               int lastTransactionNotesDispensed,
                                               int notesDeposited) {
        Integers.validateRange(cassetteType, 0, 7, "'Cassette Type'");
        return new CassetteCounters(cassetteType, notesInCassette, notesRejected, notesDispensed,
                lastTransactionNotesDispensed, notesDeposited);
    }

    public static CassetteCounters dualDispenser(int cassetteType,
                                                 int notesInCassette,
                                                 int notesRejected,
                                                 int notesDispensed,
                                                 int lastTransactionNotesDispensed,
                                                 int notesDeposited) {
        Integers.validateRange(cassetteType, 1, 7, "'Cassette Type'");
        return new CassetteCounters(cassetteType, notesInCassette, notesRejected, notesDispensed,
                lastTransactionNotesDispensed, notesDeposited);
    }

    public static CassetteCountersBuilder builder() {
        return new CassetteCountersBuilder();
    }

    public CassetteCountersBuilder copy() {
        return new CassetteCountersBuilder()
                .withCassetteType(cassetteType)
                .withNotesInCassette(notesInCassette)
                .withNotesRejected(notesRejected)
                .withNotesDispensed(notesDispensed)
                .withLastTransactionNotesDispensed(lastTransactionNotesDispensed)
                .withNotesDeposited(notesDeposited);
    }


    public int getCassetteType() {
        return cassetteType;
    }

    public int getNotesInCassette() {
        return notesInCassette;
    }

    public int getNotesRejected() {
        return notesRejected;
    }

    public int getNotesDispensed() {
        return notesDispensed;
    }

    public int getLastTransactionNotesDispensed() {
        return lastTransactionNotesDispensed;
    }

    public OptionalInt getNotesDeposited() {
        return notesDeposited >= 0 ? OptionalInt.of(notesDeposited) : OptionalInt.empty();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(28)
                .appendZeroPadded(cassetteType, 3)
                .appendZeroPadded(notesInCassette, 5)
                .appendZeroPadded(notesRejected, 5)
                .appendZeroPadded(notesDispensed, 5)
                .appendZeroPadded(lastTransactionNotesDispensed, 5)
                .appendZeroPadded(notesDeposited, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CassetteCounters.class.getSimpleName() + ": {", "}")
                .add("cassetteType: " + cassetteType)
                .add("notesInCassette: " + notesInCassette)
                .add("notesRejected: " + notesRejected)
                .add("notesDispensed: " + notesDispensed)
                .add("lastTransactionNotesDispensed: " + lastTransactionNotesDispensed)
                .add("notesDeposited: " + notesDeposited)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CassetteCounters that = (CassetteCounters) o;
        return cassetteType == that.cassetteType &&
                notesInCassette == that.notesInCassette &&
                notesRejected == that.notesRejected &&
                notesDispensed == that.notesDispensed &&
                lastTransactionNotesDispensed == that.lastTransactionNotesDispensed &&
                ((notesDeposited < 0 && that.notesDeposited < 0) || notesDeposited == that.notesDeposited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassetteType, notesInCassette, notesRejected, notesDispensed, Math.max(notesDeposited, -1),
                lastTransactionNotesDispensed);
    }
}
