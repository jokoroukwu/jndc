package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;
import java.util.function.BiConsumer;

public class CashHandlerDataGroupAppender extends ChainedCounterGroupAppender {
    private final char groupId;
    private final String groupName;
    private final NdcComponentReader<List<CassetteCounters>> cassettesReader;
    private final BiConsumer<SupplyCountersExtendedBuilder, CashHandlerDataGroup> dataAcceptor;

    CashHandlerDataGroupAppender(char groupId,
                                 String groupName,
                                 NdcComponentReader<List<CassetteCounters>> cassettesReader,
                                 BiConsumer<SupplyCountersExtendedBuilder, CashHandlerDataGroup> dataAcceptor,
                                 ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
        this.groupId = groupId;
        this.groupName = ObjectUtils.validateNotNull(groupName, "groupName");
        this.cassettesReader = ObjectUtils.validateNotNull(cassettesReader, "cassettesReader");
        this.dataAcceptor = ObjectUtils.validateNotNull(dataAcceptor, "dataAcceptor");
    }

    public static CashHandlerDataGroupAppenderBuilder builder() {
        return new CashHandlerDataGroupAppenderBuilder();
    }

    @Override
    protected char getGroupId() {
        return groupId;
    }

    @Override
    protected String getGroupName() {
        return groupName;
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersExtendedBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final List<CassetteCounters> cassettes = cassettesReader.readComponent(ndcCharBuffer);
        dataAcceptor.accept(stateObject, new CashHandlerDataGroup(groupId, cassettes));
    }
}
