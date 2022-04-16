package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;

import java.util.List;
import java.util.Objects;

public class LastTransactionStatusDataBuilder {
    private int lastTransactionSerialNumber;
    private LastStatusIssued statusIssued;
    private List<Integer> notesDispensed;
    private List<Integer> coinsDispensed;
    private CashDepositData cashDepositData;
    private CompletionData completionData;

    public LastTransactionStatusDataBuilder withLastTransactionSerialNumber(int lastTransactionSerialNumber) {
        this.lastTransactionSerialNumber = lastTransactionSerialNumber;
        return this;
    }

    public LastTransactionStatusDataBuilder withLastStatusIssued(LastStatusIssued statusIssued) {
        this.statusIssued = statusIssued;
        return this;
    }

    public LastTransactionStatusDataBuilder withNotesDispensed(List<Integer> notesDispensed) {
        this.notesDispensed = notesDispensed;
        return this;
    }

    public LastTransactionStatusDataBuilder withCoinsDispensed(List<Integer> coinsDispensed) {
        this.coinsDispensed = coinsDispensed;
        return this;
    }

    public LastTransactionStatusDataBuilder withCashDepositData(CashDepositData cashDepositData) {
        this.cashDepositData = cashDepositData;
        return this;
    }

    public LastTransactionStatusDataBuilder withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public int getLastTransactionSerialNumber() {
        return lastTransactionSerialNumber;
    }

    public LastStatusIssued getStatusIssued() {
        return statusIssued;
    }

    public List<Integer> getNotesDispensed() {
        return notesDispensed;
    }

    public List<Integer> getCoinsDispensed() {
        return coinsDispensed;
    }

    public CashDepositData getCashDepositData() {
        return cashDepositData;
    }

    public CompletionData getCompletionData() {
        return completionData;
    }

    public LastTransactionStatusDataBase build() {
        return new LastTransactionStatusDataBase(lastTransactionSerialNumber,
                statusIssued,
                notesDispensed,
                coinsDispensed,
                cashDepositData,
                completionData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastTransactionStatusDataBuilder that = (LastTransactionStatusDataBuilder) o;
        return lastTransactionSerialNumber == that.lastTransactionSerialNumber &&
                statusIssued == that.statusIssued &&
                Objects.equals(notesDispensed, that.notesDispensed) &&
                Objects.equals(coinsDispensed, that.coinsDispensed) &&
                Objects.equals(cashDepositData, that.cashDepositData) &&
                Objects.equals(completionData, that.completionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastTransactionSerialNumber, statusIssued, notesDispensed, coinsDispensed, cashDepositData, completionData);
    }
}
