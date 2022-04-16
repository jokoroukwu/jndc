package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Represents Data ID ‘g’ of Transaction Request Message.
 * This field shows that a bunch cheque deposit is being reported.
 * <br>
 * The field is optional.
 */
public class BunchChequeDepositBuffer implements IdentifiableBuffer {
    public static final char ID = 'g';
    public static final BunchChequeDepositBuffer EMPTY = new BunchChequeDepositBuffer(null);

    private final BunchChequeDepositData bunchChequeDepositData;

    public BunchChequeDepositBuffer(BunchChequeDepositData bunchChequeDepositData) {
        this.bunchChequeDepositData = bunchChequeDepositData;
    }

    public Optional<BunchChequeDepositData> getBunchChequeDepositData() {
        return Optional.ofNullable(bunchChequeDepositData);
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return bunchChequeDepositData != null ? ID + bunchChequeDepositData.toNdcString() : Character.toString(ID);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BunchChequeDepositBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + '\'')
                .add("bunchChequeDepositData: " + bunchChequeDepositData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BunchChequeDepositBuffer that = (BunchChequeDepositBuffer) o;
        return Objects.equals(bunchChequeDepositData, that.bunchChequeDepositData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, bunchChequeDepositData);
    }
}
