package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.transactiongroup.TransactionGroupAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended.COMMAND_NAME;

public class SupplyCountersExtendedAppender
        extends ChainedConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {
    private final ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> supplyCountersFieldAppender;
    private final SupplyCountersExtendedMessageListener messageListener;

    public SupplyCountersExtendedAppender(SupplyCountersExtendedMessageListener messageListener,
                                          ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> supplyCountersFieldAppender,
                                          ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> nextAppender) {
        super(nextAppender);
        this.supplyCountersFieldAppender
                = ObjectUtils.validateNotNull(supplyCountersFieldAppender, "supplyCountersFieldAppender");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    public SupplyCountersExtendedAppender(SupplyCountersExtendedMessageListener messageListener) {
        this(messageListener, new TransactionGroupAppender(), new MacAppender<>(COMMAND_NAME));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {
        final SupplyCountersExtendedBuilder supplyCountersExtendedBuilder = new SupplyCountersExtendedBuilder();
        supplyCountersFieldAppender.appendComponent(ndcCharBuffer, supplyCountersExtendedBuilder, deviceConfiguration);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);

        final SupplyCountersExtended supplyCountersExtended = supplyCountersExtendedBuilder.build();
        final SolicitedStatusMessage<? extends SolicitedStatusInformation> solicitedStatusMessage
                = stateObject.withStatusInformation(supplyCountersExtended)
                .build();

        @SuppressWarnings("unchecked") final SolicitedStatusMessage<SupplyCountersExtended> supplyCountersExtendedMessage
                = (SolicitedStatusMessage<SupplyCountersExtended>) solicitedStatusMessage;
        messageListener.onSupplyCountersExtendedMessage(supplyCountersExtendedMessage);
    }
}
