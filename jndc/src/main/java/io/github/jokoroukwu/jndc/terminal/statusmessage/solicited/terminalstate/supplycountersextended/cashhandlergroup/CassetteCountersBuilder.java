package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;

import java.util.Objects;
import java.util.StringJoiner;

public class CassetteCountersBuilder {
    private int cassetteType;
    private int notesInCassette;
    private int notesRejected;
    private int notesDispensed;
    private int lastTransactionNotesDispensed;
    private int notesDeposited = -1;


    public int getCassetteType() {
        return cassetteType;
    }

    public CassetteCountersBuilder withCassetteType(int cassetteType) {
        this.cassetteType = cassetteType;
        return this;
    }

    public int getNotesInCassette() {
        return notesInCassette;
    }

    public CassetteCountersBuilder withNotesInCassette(int notesInCassette) {
        this.notesInCassette = notesInCassette;
        return this;
    }

    public int getNotesRejected() {
        return notesRejected;
    }

    public CassetteCountersBuilder withNotesRejected(int notesRejected) {
        this.notesRejected = notesRejected;
        return this;
    }

    public int getNotesDispensed() {
        return notesDispensed;
    }

    public CassetteCountersBuilder withNotesDispensed(int notesDispensed) {
        this.notesDispensed = notesDispensed;
        return this;
    }

    public int getLastTransactionNotesDispensed() {
        return lastTransactionNotesDispensed;
    }

    public CassetteCountersBuilder withLastTransactionNotesDispensed(int lastTransactionNotesDispensed) {
        this.lastTransactionNotesDispensed = lastTransactionNotesDispensed;
        return this;
    }

    public int getNotesDeposited() {
        return notesDeposited;
    }

    public CassetteCountersBuilder withNotesDeposited(int notesDeposited) {
        this.notesDeposited = notesDeposited;
        return this;
    }

    public CassetteCounters buildDualDispenserCassetteCounters() {
        return CassetteCounters.dualDispenser(cassetteType,
                notesInCassette,
                notesRejected,
                notesDispensed,
                lastTransactionNotesDispensed,
                notesDeposited);
    }

    public CassetteCounters buildCashHandlerCassetteCounters() {
        return CassetteCounters.cashHandler(cassetteType,
                notesInCassette,
                notesRejected,
                notesDispensed,
                lastTransactionNotesDispensed,
                notesDeposited);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CassetteCountersBuilder.class.getSimpleName() + ": {", "}")
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
        CassetteCountersBuilder that = (CassetteCountersBuilder) o;
        return cassetteType == that.cassetteType &&
                notesInCassette == that.notesInCassette &&
                notesRejected == that.notesRejected &&
                notesDispensed == that.notesDispensed &&
                lastTransactionNotesDispensed == that.lastTransactionNotesDispensed &&
                ((notesDeposited < 0 && that.notesDeposited < 0) || notesDeposited == that.notesDeposited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassetteType, notesInCassette, notesRejected, notesDispensed, lastTransactionNotesDispensed,
                notesDeposited);
    }
}
