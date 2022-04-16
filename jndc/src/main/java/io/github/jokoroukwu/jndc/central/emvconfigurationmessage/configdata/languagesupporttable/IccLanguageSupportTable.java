package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class IccLanguageSupportTable extends LimitedSizeList<LanguageSupportTableEntry> implements ConfigurationData {
    public static final int MAX_SIZE = 0xFF;
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.LANGUAGE_SUPPORT;

    IccLanguageSupportTable(List<LanguageSupportTableEntry> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public IccLanguageSupportTable() {
        super(MAX_SIZE);
    }

    public IccLanguageSupportTable(int initCapacity) {
        super(MAX_SIZE, initCapacity);
    }

    public IccLanguageSupportTable(Collection<? extends LanguageSupportTableEntry> collection) {
        super(MAX_SIZE, collection);
    }

    public static IccLanguageSupportTable of(LanguageSupportTableEntry... entries) {
        final IccLanguageSupportTable table = new IccLanguageSupportTable(entries.length);
        Collections.addAll(table, entries);
        return table;
    }

    public static IccLanguageSupportTable unmodifiable(IccLanguageSupportTable table) {
        return new IccLanguageSupportTable(List.copyOf(table));
    }

    @Override
    public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
        return EmvConfigMessageSubClass.LANGUAGE_SUPPORT;
    }

    @Override
    protected void performElementChecks(LanguageSupportTableEntry element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            throw new IllegalStateException("'ICC Language Support Table cannot be empty'");
        }
        return new NdcStringBuilder((size() * LanguageSupportTableEntry.NDC_STRING_LENGTH) + 4)
                .appendComponent(EmvConfigMessageSubClass.LANGUAGE_SUPPORT)
                .appendFs()
                .appendZeroPaddedHex(size(), 2)
                .appendComponents(this)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IccLanguageSupportTable that = (IccLanguageSupportTable) o;
        return listDelegate.equals(that.listDelegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EmvConfigMessageSubClass.LANGUAGE_SUPPORT, listDelegate);
    }
}
