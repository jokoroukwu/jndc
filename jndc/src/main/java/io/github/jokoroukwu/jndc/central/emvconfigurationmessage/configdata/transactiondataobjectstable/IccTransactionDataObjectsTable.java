package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class IccTransactionDataObjectsTable extends LimitedSizeList<TransactionTableEntry> implements ConfigurationData {
    public static final int MAX_SIZE = 0xFF;
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.TRANSACTION;

    IccTransactionDataObjectsTable(List<TransactionTableEntry> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public IccTransactionDataObjectsTable(Collection<? extends TransactionTableEntry> collection) {
        super(MAX_SIZE, collection);
    }

    public IccTransactionDataObjectsTable() {
        super(MAX_SIZE);
    }

    public IccTransactionDataObjectsTable(int initCapacity) {
        super(MAX_SIZE, initCapacity);
    }

    public static IccTransactionDataObjectsTable of(TransactionTableEntry... entries) {
        final IccTransactionDataObjectsTable table = new IccTransactionDataObjectsTable(entries.length);
        table.addAll(Arrays.asList(entries));
        return table;
    }

    public static IccTransactionDataObjectsTable unmodifiable(IccTransactionDataObjectsTable table) {
        return new IccTransactionDataObjectsTable(List.copyOf(table));
    }

    @Override
    public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
        return EmvConfigMessageSubClass.TRANSACTION;
    }

    @Override
    protected void performElementChecks(TransactionTableEntry element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            throw new IllegalStateException("'ICC Transaction Data Objects Table' cannot be empty");
        }
        return new NdcStringBuilder((TransactionTableEntry.STRING_LENGTH * size()) + 4)
                .appendComponent(EmvConfigMessageSubClass.TRANSACTION)
                .appendFs()
                .appendZeroPaddedHex(size(), 2)
                .appendComponents(this)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IccTransactionDataObjectsTable that = (IccTransactionDataObjectsTable) o;
        return listDelegate.equals(that.listDelegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EmvConfigMessageSubClass.TRANSACTION, listDelegate);
    }
}
