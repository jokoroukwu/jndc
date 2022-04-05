package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters.BnaCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.chequeprocessor.ChequeProcessorCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters.CoinCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValues;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFields;
import io.github.jokoroukwu.jndc.tsn.TransactionSerialNumberAcceptor;

import java.util.Objects;
import java.util.StringJoiner;

public class SupplyCountersBasicBuilder implements TransactionSerialNumberAcceptor<SupplyCountersBasicBuilder> {
    private int transactionSerialNumber;
    private int accumulatedTransactionCount;
    private GroupedCounterValues notesInCassettes;
    private GroupedCounterValues notesRejected;
    private GroupedCounterValues notesDispensed;
    private GroupedCounterValues lastTransactionNotesDispensed;
    private int cardsCaptured;
    private int envelopesDeposited;
    private CoinCounters coinCounters;
    private BnaCounters bnaCounters;
    private ChequeProcessorCounters chequeProcessorCounters = ChequeProcessorCounters.EMPTY;
    private int numberOfPassbooksCaptured;
    private ReservedCounterFields reservedCounterFields = ReservedCounterFields.EMPTY;

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }

    @Override
    public SupplyCountersBasicBuilder withTransactionSerialNumber(int transactionSerialNumber) {
        this.transactionSerialNumber = transactionSerialNumber;
        return this;
    }

    public int getAccumulatedTransactionCount() {
        return accumulatedTransactionCount;
    }

    public SupplyCountersBasicBuilder withAccumulatedTransactionCount(int accumulatedTransactionCount) {
        this.accumulatedTransactionCount = accumulatedTransactionCount;
        return this;
    }

    public GroupedCounterValues getNotesInCassettes() {
        return notesInCassettes;
    }

    public SupplyCountersBasicBuilder withNotesInCassettes(GroupedCounterValues notesInCassettes) {
        this.notesInCassettes = notesInCassettes;
        return this;
    }

    public GroupedCounterValues getNotesRejected() {
        return notesRejected;
    }

    public SupplyCountersBasicBuilder withNotesRejected(GroupedCounterValues notesRejected) {
        this.notesRejected = notesRejected;
        return this;
    }

    public GroupedCounterValues getNotesDispensed() {
        return notesDispensed;
    }

    public SupplyCountersBasicBuilder withNotesDispensed(GroupedCounterValues notesDispensed) {
        this.notesDispensed = notesDispensed;
        return this;
    }

    public GroupedCounterValues getLastTransactionNotesDispensed() {
        return lastTransactionNotesDispensed;
    }

    public SupplyCountersBasicBuilder withLastTransactionNotesDispensed(GroupedCounterValues lastTransactionNotesDispensed) {
        this.lastTransactionNotesDispensed = lastTransactionNotesDispensed;
        return this;
    }

    public int getCardsCaptured() {
        return cardsCaptured;
    }

    public SupplyCountersBasicBuilder withCardsCaptured(int cardsCaptured) {
        this.cardsCaptured = cardsCaptured;
        return this;
    }

    public int getEnvelopesDeposited() {
        return envelopesDeposited;
    }

    public SupplyCountersBasicBuilder withEnvelopesDeposited(int envelopesDeposited) {
        this.envelopesDeposited = envelopesDeposited;
        return this;
    }

    public CoinCounters getCoinCounters() {
        return coinCounters;
    }

    public SupplyCountersBasicBuilder withCoinCounters(CoinCounters coinCounters) {
        this.coinCounters = coinCounters;
        return this;
    }

    public BnaCounters getBnaCounters() {
        return bnaCounters;
    }

    public SupplyCountersBasicBuilder withBnaCounters(BnaCounters bnaCounters) {
        this.bnaCounters = bnaCounters;
        return this;
    }

    public ChequeProcessorCounters getChequeProcessorCounters() {
        return chequeProcessorCounters;
    }

    public SupplyCountersBasicBuilder withChequeProcessorCounters(ChequeProcessorCounters chequeProcessorCounters) {
        this.chequeProcessorCounters = chequeProcessorCounters;
        return this;
    }

    public int getNumberOfPassbooksCaptured() {
        return numberOfPassbooksCaptured;
    }

    public SupplyCountersBasicBuilder withNumberOfPassbooksCaptured(int numberOfPassbooksCaptured) {
        this.numberOfPassbooksCaptured = numberOfPassbooksCaptured;
        return this;
    }

    public SupplyCountersBasicBuilder withReservedCounterFields(ReservedCounterFields reservedCounterFields) {
        this.reservedCounterFields = reservedCounterFields;
        return this;
    }

    public SupplyCountersBasic build() {
        return new SupplyCountersBasic(transactionSerialNumber,
                accumulatedTransactionCount,
                notesInCassettes,
                notesRejected,
                notesDispensed,
                lastTransactionNotesDispensed,
                cardsCaptured,
                envelopesDeposited,
                coinCounters,
                bnaCounters,
                chequeProcessorCounters,
                numberOfPassbooksCaptured,
                reservedCounterFields);
    }

    SupplyCountersBasic buildWithNoValidation() {
        return new SupplyCountersBasic(transactionSerialNumber,
                accumulatedTransactionCount,
                notesInCassettes,
                notesRejected,
                notesDispensed,
                lastTransactionNotesDispensed,
                cardsCaptured,
                envelopesDeposited,
                coinCounters,
                bnaCounters,
                chequeProcessorCounters,
                numberOfPassbooksCaptured,
                reservedCounterFields,
                null);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", SupplyCountersBasicBuilder.class.getSimpleName() + ": {", "}")
                .add("transactionSerialNumber: " + transactionSerialNumber)
                .add("accumulatedTransactionCount: " + accumulatedTransactionCount)
                .add("notesInCassettes: " + notesInCassettes)
                .add("notesRejected: " + notesRejected)
                .add("notesDispensed: " + notesDispensed)
                .add("lastTransactionNotesDispensed: " + lastTransactionNotesDispensed)
                .add("cardsCaptured: " + cardsCaptured)
                .add("envelopesDeposited: " + envelopesDeposited)
                .add("coinCounters: " + coinCounters)
                .add("bnaCounters: " + bnaCounters)
                .add("chequeProcessorCounters: " + chequeProcessorCounters)
                .add("numberOfPassbooksCaptured: " + numberOfPassbooksCaptured)
                .add("reservedCounterFields: " + reservedCounterFields)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplyCountersBasicBuilder that = (SupplyCountersBasicBuilder) o;
        return transactionSerialNumber == that.transactionSerialNumber &&
                accumulatedTransactionCount == that.accumulatedTransactionCount &&
                cardsCaptured == that.cardsCaptured &&
                envelopesDeposited == that.envelopesDeposited &&
                numberOfPassbooksCaptured == that.numberOfPassbooksCaptured &&
                Objects.equals(notesInCassettes, that.notesInCassettes) &&
                Objects.equals(notesRejected, that.notesRejected) &&
                Objects.equals(notesDispensed, that.notesDispensed) &&
                Objects.equals(lastTransactionNotesDispensed, that.lastTransactionNotesDispensed) &&
                Objects.equals(coinCounters, that.coinCounters) &&
                Objects.equals(bnaCounters, that.bnaCounters) &&
                Objects.equals(chequeProcessorCounters, that.chequeProcessorCounters) &&
                Objects.equals(reservedCounterFields, that.reservedCounterFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionSerialNumber, accumulatedTransactionCount, notesInCassettes, notesRejected,
                notesDispensed, lastTransactionNotesDispensed, cardsCaptured, envelopesDeposited, coinCounters,
                bnaCounters, chequeProcessorCounters, numberOfPassbooksCaptured, reservedCounterFields);
    }
}
