package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Document Data ID ‘a’ field representation.
 * The field is optional.
 */
public class DocumentBuffer implements IdentifiableBuffer {
    public static final DocumentBuffer EMPTY = new DocumentBuffer(null);
    public static final char ID = 'a';

    private final SingleChequeDepositData singleChequeDepositData;

    public DocumentBuffer(SingleChequeDepositData singleChequeDepositData) {
        this.singleChequeDepositData = singleChequeDepositData;
    }

    public Optional<SingleChequeDepositData> getSingleChequeDepositData() {
        return Optional.ofNullable(singleChequeDepositData);
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        if (singleChequeDepositData != null) {
            return ID + singleChequeDepositData.toNdcString();
        }
        return Character.toString(ID);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DocumentBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + '\'')
                .add("singleChequeDepositData: " + singleChequeDepositData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentBuffer that = (DocumentBuffer) o;
        return Objects.equals(singleChequeDepositData, that.singleChequeDepositData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, singleChequeDepositData);
    }

}
