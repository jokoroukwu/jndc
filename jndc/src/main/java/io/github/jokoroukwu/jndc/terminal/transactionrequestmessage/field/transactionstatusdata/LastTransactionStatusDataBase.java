package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class LastTransactionStatusDataBase implements LastTransactionStatusData {
    public static final char ID = '2';

    //  Coinage Amount Dispensed should always be zero
    private static final int coinageAmountDispensed = 0;

    private final int lastTransactionSerialNumber;
    private final LastStatusIssued statusIssued;
    private final List<Integer> notesDispensed;
    private final List<Integer> coinsDispensed;
    private final CashDepositData cashDepositData;
    private final CompletionData completionData;

    public LastTransactionStatusDataBase(int lastTransactionSerialNumber,
                                         LastStatusIssued statusIssued,
                                         Collection<Integer> notesDispensed,
                                         Collection<Integer> coinsDispensed,
                                         CashDepositData cashDepositData,
                                         CompletionData completionData) {
        this.lastTransactionSerialNumber = validateLastTransactionSerialNumber(lastTransactionSerialNumber);
        this.statusIssued = ObjectUtils.validateNotNull(statusIssued, "Last Status Issued");
        this.notesDispensed = copyNotesDispensed(notesDispensed);
        this.coinsDispensed = copyCoinsDispensed(coinsDispensed);
        this.cashDepositData = cashDepositData;
        this.completionData = completionData;
    }

    public LastTransactionStatusDataBase(int lastTransactionSerialNumber,
                                         LastStatusIssued statusIssued,
                                         Collection<Integer> notesDispensed,
                                         Collection<Integer> coinsDispensed) {
        this(lastTransactionSerialNumber, statusIssued, notesDispensed, coinsDispensed, null, null);
    }

    LastTransactionStatusDataBase(int lastTransactionSerialNumber,
                                  LastStatusIssued statusIssued,
                                  List<Integer> notesDispensed,
                                  List<Integer> coinsDispensed,
                                  CashDepositData cashDepositData,
                                  CompletionData completionData) {
        this.lastTransactionSerialNumber = lastTransactionSerialNumber;
        this.statusIssued = statusIssued;
        this.notesDispensed = notesDispensed;
        this.coinsDispensed = coinsDispensed;
        this.cashDepositData = cashDepositData;
        this.completionData = completionData;
    }

    public static LastTransactionStatusDataBuilder builder() {
        return new LastTransactionStatusDataBuilder();
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public int getLastTransactionSerialNumber() {
        return lastTransactionSerialNumber;
    }

    @Override
    public LastStatusIssued getStatusIssued() {
        return statusIssued;
    }

    @Override
    public List<Integer> getNotesDispensed() {
        return notesDispensed;
    }

    @Override
    public int getCoinageAmountDispensed() {
        return coinageAmountDispensed;
    }

    @Override
    public List<Integer> getCoinsDispensed() {
        return coinsDispensed;
    }

    @Override
    public Optional<CompletionData> getCompletionData() {
        return Optional.ofNullable(completionData);
    }

    @Override
    public Optional<CashDepositData> getCashDepositData() {
        return Optional.ofNullable(cashDepositData);
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(128)
                .append(ID)
                .appendZeroPadded(lastTransactionSerialNumber, 4)
                .append(statusIssued.getValue())
                .appendZeroPadded(notesDispensed, Strings.EMPTY_STRING, 5)
                //  coinage amount dispensed bytes
                .append("00000")
                .appendZeroPadded(coinsDispensed, Strings.EMPTY_STRING, 5)
                .appendComponent(cashDepositData)
                .append(completionData != null ? NdcConstants.GROUP_SEPARATOR + completionData.toNdcString() : Strings.EMPTY_STRING)
                .toString();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", LastTransactionStatusDataBase.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("lastTransactionSerialNumber: " + lastTransactionSerialNumber)
                .add("statusIssued: " + statusIssued)
                .add("notesDispensed: " + notesDispensed)
                .add("coinsDispensed: " + coinsDispensed)
                .add("cashDepositData: " + cashDepositData)
                .add("completionData: " + completionData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastTransactionStatusDataBase that = (LastTransactionStatusDataBase) o;
        return lastTransactionSerialNumber == that.lastTransactionSerialNumber &&
                statusIssued == that.statusIssued &&
                notesDispensed.equals(that.notesDispensed) &&
                coinsDispensed.equals(that.coinsDispensed) &&
                Objects.equals(cashDepositData, that.cashDepositData) &&
                Objects.equals(completionData, that.completionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastTransactionSerialNumber, statusIssued, notesDispensed, coinsDispensed, cashDepositData, completionData);
    }

    private int validateLastTransactionSerialNumber(int value) {
        if (value < 0 || value > 9999) {
            final String message = "'Last Transaction Serial Number' value should be within valid range (0-9999 dec) but was: ";
            throw new IllegalArgumentException(message + value);
        }
        return value;
    }

    private List<Integer> copyCoinsDispensed(Collection<Integer> values) {
        final int size = ObjectUtils.validateNotNull(values, "Coins Dispensed").size();
        if (size != 4) {
            final String message = "'Coins Dispensed' should contain four values but actual number was: " + size;
            throw new IllegalArgumentException(message);
        }
        return copyValues(values, "'Coins Dispensed'");
    }

    private List<Integer> copyNotesDispensed(Collection<Integer> values) {
        final int size = ObjectUtils.validateNotNull(values, "Notes Dispensed").size();
        if (size != 4 && size != 7) {
            final String message = "'Notes Dispensed' should contain either four or seven values but actual number was: ";
            throw new IllegalArgumentException(message + size);
        }
        return copyValues(values, "Notes Dispensed");
    }

    private List<Integer> copyValues(Collection<Integer> values, String fieldName) {
        final List<Integer> copy = new ArrayList<>(values.size());
        for (Integer value : values) {
            if (value == null || value < 0 || value > 99999) {
                final String message = "'%s' values must be within valid range (0-99999 dec) but was: %s";
                throw new IllegalArgumentException(String.format(message, fieldName, value));
            }
            copy.add(value);
        }
        return Collections.unmodifiableList(copy);
    }

}
