package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended.COMMAND_NAME;

public abstract class ChainedCounterGroupAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> {
    public ChainedCounterGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SupplyCountersExtendedBuilder stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final char groupId = getGroupId();
        if (shouldProcess(ndcCharBuffer, groupId)) {
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> onNoGroupSeparator(COMMAND_NAME, getGroupName(), errorMessage, ndcCharBuffer));
            //  group ID was checked previously so it can be skipped
            ndcCharBuffer.skip(1);

            doAppendComponent(ndcCharBuffer, stateObject, deviceConfiguration);
            callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
        } else {
            callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
        }
    }

    protected abstract char getGroupId();

    protected abstract String getGroupName();

    protected abstract void doAppendComponent(NdcCharBuffer ndcCharBuffer,
                                              SupplyCountersExtendedBuilder stateObject,
                                              DeviceConfiguration deviceConfiguration);

    protected boolean shouldProcess(NdcCharBuffer ndcCharBuffer, char groupId) {
        return ndcCharBuffer.getCharAt(1) == groupId;
    }

    @Override
    protected void callNextAppenderIfDataRemains(NdcCharBuffer ndcCharBuffer,
                                                 SupplyCountersExtendedBuilder collector,
                                                 DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration.isMacEnabled()) {
            if (ndcCharBuffer.remaining() > 9) {
                callNextAppender(ndcCharBuffer, collector, deviceConfiguration);
            }
        } else if (ndcCharBuffer.hasRemaining(2)){
            callNextAppender(ndcCharBuffer, collector, deviceConfiguration);
        }
    }
}
