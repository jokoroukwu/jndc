package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class SolicitedCardReaderWriterFaultAppender
        implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final SolicitedCardReaderWriterFaultMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;
    private final ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> faultFieldAppender;

    public SolicitedCardReaderWriterFaultAppender(SolicitedCardReaderWriterFaultMessageListener messageListener,
                                                  ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> faultFieldAppender,
                                                  ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
        this.faultFieldAppender = ObjectUtils.validateNotNull(faultFieldAppender, "faultFieldAppender");
    }

    public SolicitedCardReaderWriterFaultAppender(SolicitedCardReaderWriterFaultMessageListener messageListener) {
        this.messageListener = messageListener;
        final String commandName = SolicitedStatusMessage.COMMAND_NAME + ": " + StatusDescriptor.DEVICE_FAULT.toString();
        this.macAppender = new MacAppender<>(commandName);
        this.faultFieldAppender = new CardReaderWriterTransactionStatusAppender(commandName);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final CardReaderWriterFaultBuilder cardReaderWriterFaultBuilder = new CardReaderWriterFaultBuilder();
        faultFieldAppender.appendComponent(ndcCharBuffer, cardReaderWriterFaultBuilder, deviceConfiguration);

        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<? extends SolicitedStatusInformation> message = stateObject
                .withStatusInformation(cardReaderWriterFaultBuilder.build())
                .build();
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<CardReaderWriterFault> cardReaderWriteFaultMessage
                = (SolicitedStatusMessage<CardReaderWriterFault>) message;

        messageListener.onSolicitedCardReaderWriterFaultMessage(cardReaderWriteFaultMessage);
    }

}
