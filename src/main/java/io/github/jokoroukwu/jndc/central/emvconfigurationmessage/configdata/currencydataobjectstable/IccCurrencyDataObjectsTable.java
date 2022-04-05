package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class IccCurrencyDataObjectsTable extends LimitedSizeList<CurrencyTableEntry> implements ConfigurationData {
    public static final int MAX_SIZE = 0xFF;

    IccCurrencyDataObjectsTable(List<CurrencyTableEntry> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public IccCurrencyDataObjectsTable(int capacity) {
        super(MAX_SIZE, capacity);
    }

    public IccCurrencyDataObjectsTable(Collection<? extends CurrencyTableEntry> collection) {
        super(MAX_SIZE, collection);
    }

    public IccCurrencyDataObjectsTable() {
        super(MAX_SIZE);
    }

    public static IccCurrencyDataObjectsTable unmodifiable(IccCurrencyDataObjectsTable table) {
        return new IccCurrencyDataObjectsTable(List.copyOf(table));
    }

    public static IccCurrencyDataObjectsTable of(CurrencyTableEntry... entries) {
        final IccCurrencyDataObjectsTable table = new IccCurrencyDataObjectsTable(entries.length);
        table.addAll(Arrays.asList(entries));
        return table;
    }

    @Override
    public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
        return EmvConfigMessageSubClass.CURRENCY;
    }


    @Override
    protected void performElementChecks(CurrencyTableEntry element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            throw new IllegalStateException("'ICC Currency Data Objects Table' cannot be empty");
        }
        return new NdcStringBuilder((CurrencyTableEntry.STRING_LENGTH * size()) + 2)
                .appendComponent(EmvConfigMessageSubClass.CURRENCY)
                .appendFs()
                .appendZeroPaddedHex(size(), 2)
                .appendComponents(this)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IccCurrencyDataObjectsTable that = (IccCurrencyDataObjectsTable) o;
        return listDelegate.equals(that.listDelegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EmvConfigMessageSubClass.CURRENCY, listDelegate);
    }
}
