package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.DataObjectsContainer;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.tlv.TransactionType;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public class TransactionTableEntry extends DataObjectsContainer {
    public static final int STRING_LENGTH = 20;

    private final int transactionType;
    private final CompositeTlv<String> dataObjects;

    public TransactionTableEntry(int transactionType, CompositeTlv<String> dataObjects) {
        this.transactionType = Integers.validateHexRange(transactionType, 1, 0xFF, "'Entry Type'");
        this.dataObjects = validateDataObjects(dataObjects, TransactionType.TAG, TransactionCategoryCode.TAG);
    }

    TransactionTableEntry(int transactionType, CompositeTlv<String> dataObjects, Void unused) {
        this.transactionType = transactionType;
        this.dataObjects = dataObjects;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public CompositeTlv<String> getDataObjects() {
        return dataObjects;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionTableEntry.class.getSimpleName() + ": {", "}")
                .add("transactionType: " + Integers.toEvenLengthHexString(transactionType))
                .add("dataObjects: " + dataObjects)
                .toString();
    }

    @Override
    public String toNdcString() {
        return Integers.toEvenLengthHexString(transactionType) + dataObjects.toNdcString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionTableEntry that = (TransactionTableEntry) o;
        return transactionType == that.transactionType && dataObjects.equals(that.dataObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionType, dataObjects);
    }


}
