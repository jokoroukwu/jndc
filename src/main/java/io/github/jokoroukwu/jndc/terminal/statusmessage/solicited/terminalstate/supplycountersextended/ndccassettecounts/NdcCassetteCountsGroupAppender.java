package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import java.util.List;
import java.util.function.BiConsumer;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class NdcCassetteCountsGroupAppender extends ChainedCounterGroupAppender {
    private final char groupId;
    private final String groupName;
    private final BiConsumer<SupplyCountersExtendedBuilder, NdcCassetteCountsGroup> dataConsumer;
    private final NdcComponentReader<List<NdcCassetteCounts>> cassetteCountsReader;

    public NdcCassetteCountsGroupAppender(char groupId,
                                          String groupName,
                                          NdcComponentReader<List<NdcCassetteCounts>> cassetteCountsReader,
                                          BiConsumer<SupplyCountersExtendedBuilder, NdcCassetteCountsGroup> dataConsumer,
                                          ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
        this.groupId = groupId;
        this.groupName = ObjectUtils.validateNotNull(groupName, "groupName");
        this.cassetteCountsReader = ObjectUtils.validateNotNull(cassetteCountsReader, "cassetteCountsReader");
        this.dataConsumer = dataConsumer;
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
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {

        final List<NdcCassetteCounts> ndcCassetteCounts = Result.of(() -> cassetteCountsReader.readComponent(ndcCharBuffer))
                .getOrThrow(exception -> withMessage(SupplyCountersExtended.COMMAND_NAME, getGroupName(), exception.getMessage(), ndcCharBuffer));

        final NdcCassetteCountsGroup ndcCassetteCountsGroup = new NdcCassetteCountsGroup(groupId, ndcCassetteCounts);
        dataConsumer.accept(stateObject, ndcCassetteCountsGroup);
    }
}
