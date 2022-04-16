package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters.BnaCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.chequeprocessor.ChequeProcessorCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters.CoinCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValues;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFields;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.StringJoiner;

public class SupplyCountersBasic implements TerminalState {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " +
            StatusDescriptor.TERMINAL_STATE + ": " + TerminalStateMessageId.SEND_SUPPLY_COUNTERS_BASIC;

    private final int transactionSerialNumber;
    private final int accumulatedTransactionCount;
    private final GroupedCounterValues notesInCassettes;
    private final GroupedCounterValues notesRejected;
    private final GroupedCounterValues notesDispensed;
    private final GroupedCounterValues lastTransactionNotesDispensed;
    private final int cardsCaptured;
    private final int envelopesDeposited;
    private final CoinCounters coinCounters;
    private final BnaCounters bnaCounters;
    private final ChequeProcessorCounters chequeProcessorCounters;
    private final int numberOfPassbooksCaptured;
    private final ReservedCounterFields reservedCounterFields;

    public SupplyCountersBasic(int transactionSerialNumber,
                               int accumulatedTransactionCount,
                               GroupedCounterValues notesInCassettes,
                               GroupedCounterValues notesRejected,
                               GroupedCounterValues notesDispensed,
                               GroupedCounterValues lastTransactionNotesDispensed,
                               int cardsCaptured,
                               int envelopesDeposited,
                               CoinCounters coinCounters,
                               BnaCounters bnaCounters,
                               ChequeProcessorCounters chequeProcessorCounters,
                               int numberOfPassbooksCaptured,
                               ReservedCounterFields reservedCounterFields) {
        this.notesInCassettes = ObjectUtils.validateNotNull(notesInCassettes, "'Notes In Cassettes'");
        this.notesRejected = ObjectUtils.validateNotNull(notesRejected, "'Notes Rejected'");
        this.notesDispensed = ObjectUtils.validateNotNull(notesDispensed, "'Notes Dispensed'");
        this.cardsCaptured = Integers.validateRange(cardsCaptured, 0, 99999, "'Cards captured'");
        this.transactionSerialNumber = Integers.validateRange(transactionSerialNumber, 0, 9999,
                "'Transaction Serial Number'");
        this.accumulatedTransactionCount = Integers.validateRange(accumulatedTransactionCount, 0, 9999999,
                "'Accumulated Transaction Count'");
        this.lastTransactionNotesDispensed = ObjectUtils.validateNotNull(lastTransactionNotesDispensed,
                "'Last Transaction Notes Dispensed'");
        this.envelopesDeposited = Integers.validateRange(envelopesDeposited, 0, 99999, "'Envelopes Deposited'");
        this.numberOfPassbooksCaptured = Integers.validateMaxValue(numberOfPassbooksCaptured, 99999,
                "'Number of passbooks captured'");
        this.reservedCounterFields = ObjectUtils.validateNotNull(reservedCounterFields, "'Reserved Fields'");
        this.chequeProcessorCounters = ObjectUtils.validateNotNull(chequeProcessorCounters, "chequeProcessorCounters");
        this.coinCounters = coinCounters;
        this.bnaCounters = bnaCounters;
    }

    SupplyCountersBasic(int transactionSerialNumber,
                        int accumulatedTransactionCount,
                        GroupedCounterValues notesInCassettes,
                        GroupedCounterValues notesRejected,
                        GroupedCounterValues notesDispensed,
                        GroupedCounterValues lastTransactionNotesDispensed,
                        int cardsCaptured,
                        int envelopesDeposited,
                        CoinCounters coinCounters,
                        BnaCounters bnaCounters,
                        ChequeProcessorCounters chequeProcessorCounters,
                        int numberOfPassbooksCaptured,
                        ReservedCounterFields reservedCounterFields,
                        Void unused) {
        this.transactionSerialNumber = transactionSerialNumber;
        this.accumulatedTransactionCount = accumulatedTransactionCount;
        this.notesInCassettes = notesInCassettes;
        this.notesRejected = notesRejected;
        this.notesDispensed = notesDispensed;
        this.lastTransactionNotesDispensed = lastTransactionNotesDispensed;
        this.cardsCaptured = cardsCaptured;
        this.envelopesDeposited = envelopesDeposited;
        this.coinCounters = coinCounters;
        this.bnaCounters = bnaCounters;
        this.chequeProcessorCounters = chequeProcessorCounters;
        this.numberOfPassbooksCaptured = numberOfPassbooksCaptured;
        this.reservedCounterFields = reservedCounterFields;
    }

    public static SupplyCountersBasicBuilder builder() {
        return new SupplyCountersBasicBuilder();
    }

    public SupplyCountersBasicBuilder copy() {
        return new SupplyCountersBasicBuilder()
                .withTransactionSerialNumber(transactionSerialNumber)
                .withAccumulatedTransactionCount(accumulatedTransactionCount)
                .withNotesInCassettes(notesInCassettes)
                .withNotesRejected(notesRejected)
                .withNotesDispensed(notesDispensed)
                .withLastTransactionNotesDispensed(lastTransactionNotesDispensed)
                .withCardsCaptured(cardsCaptured)
                .withEnvelopesDeposited(envelopesDeposited)
                .withCoinCounters(coinCounters)
                .withBnaCounters(bnaCounters)
                .withChequeProcessorCounters(chequeProcessorCounters)
                .withNumberOfPassbooksCaptured(numberOfPassbooksCaptured)
                .withReservedCounterFields(reservedCounterFields);
    }

    @Override
    public TerminalStateMessageId getMessageId() {
        return TerminalStateMessageId.SEND_SUPPLY_COUNTERS_BASIC;
    }

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }

    public int getAccumulatedTransactionCount() {
        return accumulatedTransactionCount;
    }

    public GroupedCounterValues getNotesInCassettes() {
        return notesInCassettes;
    }

    public GroupedCounterValues getNotesRejected() {
        return notesRejected;
    }

    public GroupedCounterValues getNotesDispensed() {
        return notesDispensed;
    }

    public GroupedCounterValues getLastTransactionNotesDispensed() {
        return lastTransactionNotesDispensed;
    }

    public int getCardsCaptured() {
        return cardsCaptured;
    }

    public int getEnvelopesDeposited() {
        return envelopesDeposited;
    }

    public int getCameraFilmRemaining() {
        return 0;
    }

    public int getLastEnvelopeSerialNumber() {
        return 0;
    }

    public Optional<CoinCounters> getCoinCounters() {
        return Optional.ofNullable(coinCounters);
    }

    public Optional<BnaCounters> getBnaCounters() {
        return Optional.ofNullable(bnaCounters);
    }

    public ChequeProcessorCounters getChequeProcessorCounters() {
        return chequeProcessorCounters;
    }

    public OptionalInt getNumberOfPassbooksCaptured() {
        return numberOfPassbooksCaptured >= 0 ? OptionalInt.of(numberOfPassbooksCaptured) : OptionalInt.empty();
    }

    public ReservedCounterFields getReservedCounterFields() {
        return reservedCounterFields;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SupplyCountersBasic.class.getSimpleName() + ": {", "}")
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
        SupplyCountersBasic that = (SupplyCountersBasic) o;
        return transactionSerialNumber == that.transactionSerialNumber &&
                accumulatedTransactionCount == that.accumulatedTransactionCount &&
                cardsCaptured == that.cardsCaptured &&
                envelopesDeposited == that.envelopesDeposited &&
                ((numberOfPassbooksCaptured < 0 && that.numberOfPassbooksCaptured < 0) ||
                        numberOfPassbooksCaptured == that.numberOfPassbooksCaptured) &&
                Objects.equals(notesInCassettes, that.notesInCassettes) &&
                Objects.equals(notesRejected, that.notesRejected) &&
                Objects.equals(notesDispensed, that.notesDispensed) &&
                Objects.equals(lastTransactionNotesDispensed, that.lastTransactionNotesDispensed) &&
                coinCounters.equals(that.coinCounters) &&
                bnaCounters.equals(that.bnaCounters) &&
                chequeProcessorCounters.equals(that.chequeProcessorCounters) &&
                reservedCounterFields.equals(that.reservedCounterFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionSerialNumber, accumulatedTransactionCount, notesInCassettes, notesRejected,
                notesDispensed, lastTransactionNotesDispensed, cardsCaptured, envelopesDeposited, coinCounters,
                bnaCounters, chequeProcessorCounters, Math.max(numberOfPassbooksCaptured, -1), reservedCounterFields);
    }

    @Override
    public String toNdcString() {
        var builder = new NdcStringBuilder(256)
                .appendComponent(TerminalStateMessageId.SEND_SUPPLY_COUNTERS_BASIC)
                .appendZeroPadded(transactionSerialNumber, 4)
                .appendZeroPadded(accumulatedTransactionCount, 7)
                .appendComponent(notesInCassettes)
                .appendComponent(notesRejected)
                .appendComponent(notesDispensed)
                .appendComponent(lastTransactionNotesDispensed)
                .appendZeroPadded(cardsCaptured, 5)
                .appendZeroPadded(envelopesDeposited, 5)
                // camera film remaining is always zero
                .append("00000")
                //  last envelope serial number is always zero
                .append("00000");
        final CharSequence optionalFieldCharSequence = joinOptionalFields();
        if (optionalFieldCharSequence.length() > 0) {
            builder.ensureCapacity(optionalFieldCharSequence.length())
                    .append(optionalFieldCharSequence);
        }
        return builder.toString();
    }

    private CharSequence joinOptionalFields() {
        final NdcStringBuilder builder = new NdcStringBuilder(128);
        builder.append(reservedCounterFields.getG12());
        final String[] optionalFields =
                {
                        reservedCounterFields.getG20(), reservedCounterFields.getG30(),
                        reservedCounterFields.getG40(), reservedCounterFields.getG50(),
                        componentToString(coinCounters), componentToString(bnaCounters),
                        reservedCounterFields.getG80(), reservedCounterFields.getG90(),
                        reservedCounterFields.getG100(), reservedCounterFields.getG110(),
                        componentToString(chequeProcessorCounters), reservedCounterFields.getG130(),
                        reservedCounterFields.getG140(), reservedCounterFields.getG150(),
                        (numberOfPassbooksCaptured < 0
                                ? Strings.EMPTY_STRING
                                : Integers.toZeroPaddedString(numberOfPassbooksCaptured, 5)
                        )
                };
        int numberOfGroupSeparators = 1;
        for (String optionalField : optionalFields) {
            if (!optionalField.isEmpty()) {
                builder.appendGs(numberOfGroupSeparators)
                        .append(optionalField);
                numberOfGroupSeparators = 1;
            } else {
                ++numberOfGroupSeparators;
            }
        }
        return builder;
    }

    private String componentToString(NdcComponent ndcComponent) {
        return ndcComponent != null ? ndcComponent.toNdcString() : Strings.EMPTY_STRING;
    }

}
