package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.transactiongroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class TransactionCounterGroup implements IdentifiableCounterGroup {
    public static final char ID = 'A';

    private final int transactionSerialNumber;
    private final int accumulatedTransactionCount;

    public TransactionCounterGroup(int transactionSerialNumber, int accumulatedTransactionCount) {
        this.transactionSerialNumber = Integers.validateRange(transactionSerialNumber, 0, 9999,
                "'Transaction Serial Number (TSN)'");
        this.accumulatedTransactionCount = Integers.validateRange(accumulatedTransactionCount, 0, 9999999,
                "'Accumulated Transaction Count'");
    }

    TransactionCounterGroup(int transactionSerialNumber, int accumulatedTransactionCount, Void unused) {
        this.transactionSerialNumber = transactionSerialNumber;
        this.accumulatedTransactionCount = accumulatedTransactionCount;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }

    public int getAccumulatedTransactionCount() {
        return accumulatedTransactionCount;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(12)
                .append(ID)
                .appendZeroPadded(transactionSerialNumber, 4)
                .appendZeroPadded(accumulatedTransactionCount, 7)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionCounterGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("transactionSerialNumber: " + transactionSerialNumber)
                .add("accumulatedTransactionCount: " + accumulatedTransactionCount)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCounterGroup that = (TransactionCounterGroup) o;
        return transactionSerialNumber == that.transactionSerialNumber &&
                accumulatedTransactionCount == that.accumulatedTransactionCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionSerialNumber, accumulatedTransactionCount);
    }
}
