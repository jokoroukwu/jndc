package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class SolicitedCardReaderWriterStatusInfoAppender
        implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final SolicitedCardReaderWriterStatusInfoMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;
    private final ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> subFieldAppender;

    public SolicitedCardReaderWriterStatusInfoAppender(SolicitedCardReaderWriterStatusInfoMessageListener messageListener,
                                                       ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> subFieldAppender,
                                                       ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
        this.subFieldAppender = ObjectUtils.validateNotNull(subFieldAppender, "subFieldAppender");
    }

    public SolicitedCardReaderWriterStatusInfoAppender(SolicitedCardReaderWriterStatusInfoMessageListener messageListener) {
        this.messageListener = messageListener;
        final String commandName = SolicitedStatusMessage.COMMAND_NAME + ": " + StatusDescriptor.DEVICE_FAULT;
        this.macAppender = new MacAppender<>(commandName);
        this.subFieldAppender = new CardReaderWriterTransactionStatusAppender(commandName);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final CardReaderStatusInfoBuilder cardReaderStatusInfoBuilder = new CardReaderStatusInfoBuilder();
        subFieldAppender.appendComponent(ndcCharBuffer, cardReaderStatusInfoBuilder, deviceConfiguration);

        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<? extends SolicitedStatusInformation> message = stateObject
                .withStatusInformation(cardReaderStatusInfoBuilder.build())
                .build();
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<CardReaderWriterStatusInfo> cardReaderWriteFaultMessage
                = (SolicitedStatusMessage<CardReaderWriterStatusInfo>) message;

        messageListener.onSolicitedCardReaderWriterStatusInfoMessage(cardReaderWriteFaultMessage);
    }

}
