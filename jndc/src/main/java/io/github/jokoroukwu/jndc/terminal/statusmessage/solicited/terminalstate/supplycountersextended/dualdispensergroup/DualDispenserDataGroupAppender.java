package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dualdispensergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.DualDispenserCassetteCountersReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroupAppenderBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.secondarycardreadergroup.SecondCardReaderDataAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;

public class DualDispenserDataGroupAppender extends ChainedCounterGroupAppender {
    private final NdcComponentReader<List<CassetteCounters>> cassetteCountersReader;

    public DualDispenserDataGroupAppender(NdcComponentReader<List<CassetteCounters>> cassetteCountersReader,
                                          ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
        this.cassetteCountersReader = ObjectUtils.validateNotNull(cassetteCountersReader, "cassetteCountersReader");
    }

    public DualDispenserDataGroupAppender() {
        this(DualDispenserCassetteCountersReader.INSTANCE, new NdcCassetteCountsGroupAppenderBuilder()
                .withGroupName("ECB 6 Category 2 Notes data group ID 'N'")
                .withGroupId(NdcCassetteCountsGroup.ECB_2_GROUP_ID)
                .withCassetteCountsReader(new NdcCassetteCountsReader())
                .withDataConsumer(SupplyCountersExtendedBuilder::withEcbCategory2NotesData)
                .withNextAppender(new NdcCassetteCountsGroupAppenderBuilder()
                        .withGroupName("ECB 6 Category 3 Notes data group ID 'O'")
                        .withGroupId(NdcCassetteCountsGroup.ECB_3_GROUP_ID)
                        .withCassetteCountsReader(new NdcCassetteCountsReader())
                        .withDataConsumer(SupplyCountersExtendedBuilder::withEcbCategory3NotesData)
                        .withNextAppender(new SecondCardReaderDataAppender())
                        .build())
                .build());
    }

    @Override
    protected char getGroupId() {
        return DualDispenserDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "Dual Dispenser combined data group";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersExtendedBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final List<CassetteCounters> cassetteCounters = cassetteCountersReader.readComponent(ndcCharBuffer);
        final DualDispenserDataGroup dualDispenserDataGroup = new DualDispenserDataGroup(cassetteCounters);

        stateObject.withDualDispenserDataGroup(dualDispenserDataGroup);
    }
}
