package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.secondarycardreadergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ChainedCounterGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class SecondCardReaderDataAppender extends ChainedCounterGroupAppender {
    public SecondCardReaderDataAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public SecondCardReaderDataAppender() {
        super(null);
    }

    @Override
    protected char getGroupId() {
        return SecondCardReaderData.ID;
    }

    @Override
    protected String getGroupName() {
        return "Second Card Reader data group id 'T'";
    }

    @Override
    protected void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                     SupplyCountersExtendedBuilder stateObject,
                                     DeviceConfiguration deviceConfiguration) {
        final int cardCaptured = ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(SupplyCountersExtended.COMMAND_NAME, getGroupName(), errorMessage, ndcCharBuffer));

        stateObject.withSecondCardReaderData(new SecondCardReaderData(cardCaptured, null));
    }
}
