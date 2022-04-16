package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cameragroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup.ChequeProcessorDataGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroupAppenderBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsReader;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended.COMMAND_NAME;

public class CameraDataGroupAppender extends ChainedCounterGroupAppender {

    public CameraDataGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public CameraDataGroupAppender() {
        super(
                new NdcCassetteCountsGroupAppenderBuilder()
                        .withGroupId(NdcCassetteCountsGroup.BNA_GROUP_ID)
                        .withGroupName("BNA Cassette Counts data group ID 'I'")
                        .withDataConsumer(SupplyCountersExtendedBuilder::withBnaCassetteCountsGroup)
                        .withCassetteCountsReader(new NdcCassetteCountsReader())
                        .withNextAppender(new ChequeProcessorDataGroupAppender())
                        .build()
        );
    }

    @Override
    protected char getGroupId() {
        return CameraDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "Camera data group";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersExtendedBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipNextSubsequence("00000")
                .ifPresent(errorMessage
                        -> onFieldParseError(COMMAND_NAME, "'Camera Film Remaining'", errorMessage, ndcCharBuffer));
        stateObject.withCameraDataGroup(CameraDataGroup.INSTANCE);
    }
}
