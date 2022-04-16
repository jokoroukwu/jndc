package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dpmgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended.COMMAND_NAME;

public class DpmDataGroupAppender extends ChainedCounterGroupAppender {

    public DpmDataGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    @Override
    protected char getGroupId() {
        return DpmDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "DPM data group";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersExtendedBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final String depositBinId = ndcCharBuffer.tryReadCharSequence(2)
                .getOrThrow(errorMessage
                        -> withMessage(COMMAND_NAME, "'Deposit Bin Identifier'", errorMessage, ndcCharBuffer));
        final String documentsDeposited = ndcCharBuffer.tryReadCharSequence(5)
                .getOrThrow(errorMessage ->
                        withMessage(COMMAND_NAME, "'Documents Deposited in Bin'", errorMessage, ndcCharBuffer));

        final DpmDataGroup dataGroup = new DpmDataGroup(depositBinId, documentsDeposited, null);
        stateObject.withDpmDataGroup(dataGroup);
    }
}
