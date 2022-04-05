package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cameragroup.CameraDataGroupAppender;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class EnvelopeDepositoryDataGroupAppender extends ChainedCounterGroupAppender {

    public EnvelopeDepositoryDataGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public EnvelopeDepositoryDataGroupAppender() {
        super(new CameraDataGroupAppender());
    }

    @Override
    protected char getGroupId() {
        return EnvelopeDepositoryDataGroup.ID;
    }

    @Override
    protected String getGroupName() {
        return "'Envelope Depository data group'";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {
        final int envelopesDeposited = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Envelopes Deposited'", errorMessage, ndcCharBuffer));

        final int lastEnvelopeSerialNumber = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Last Envelope Serial Number'", errorMessage, ndcCharBuffer));

        final EnvelopeDepositoryDataGroup envelopeDepositoryDataGroup
                = new EnvelopeDepositoryDataGroup(envelopesDeposited, lastEnvelopeSerialNumber, null);
        stateObject.withEnvelopeDepositoryDataGroup(envelopeDepositoryDataGroup);
    }
}
