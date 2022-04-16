package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.collection.LimitedSizeList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class IccTerminalAcceptableAppIdsTable extends LimitedSizeList<TerminalApplicationIdEntry> implements ConfigurationData {
    public static final int MAX_SIZE = 0x100;
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.TERMINAL_ACCEPTABLE_AIDS;

    IccTerminalAcceptableAppIdsTable(List<TerminalApplicationIdEntry> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public IccTerminalAcceptableAppIdsTable(Collection<? extends TerminalApplicationIdEntry> collection) {
        super(MAX_SIZE, collection);
    }

    public IccTerminalAcceptableAppIdsTable() {
        super(MAX_SIZE);
    }

    public IccTerminalAcceptableAppIdsTable(int initCapacity) {
        super(MAX_SIZE, initCapacity);
    }


    public static IccTerminalAcceptableAppIdsTable unmodifiable(IccTerminalAcceptableAppIdsTable table) {
        return new IccTerminalAcceptableAppIdsTable(List.copyOf(table));
    }

    @Override
    public EmvConfigMessageSubClass getEmvConfigMessageSubClass() {
        return EmvConfigMessageSubClass.TERMINAL_ACCEPTABLE_AIDS;
    }


    @Override
    protected void performElementChecks(TerminalApplicationIdEntry element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(size() * 132)
                .appendComponent(EmvConfigMessageSubClass.TERMINAL_ACCEPTABLE_AIDS)
                .appendFs()
                .appendComponents(this, NdcConstants.GROUP_SEPARATOR_STRING)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IccTerminalAcceptableAppIdsTable that = (IccTerminalAcceptableAppIdsTable) o;
        return listDelegate.equals(that.listDelegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EmvConfigMessageSubClass.TERMINAL_ACCEPTABLE_AIDS, listDelegate);
    }
}
