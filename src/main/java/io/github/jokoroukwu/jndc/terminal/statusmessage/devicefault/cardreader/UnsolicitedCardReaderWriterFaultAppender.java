package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class UnsolicitedCardReaderWriterFaultAppender
        implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {

    private final UnsolicitedCardReaderWriterFaultMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> subFieldAppender;

    public UnsolicitedCardReaderWriterFaultAppender(UnsolicitedCardReaderWriterFaultMessageListener messageListener,
                                                    ConfigurableNdcComponentAppender<CardReaderWriterFaultBuilder> subFieldAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.subFieldAppender = ObjectUtils.validateNotNull(subFieldAppender, "subFieldAppender");
    }

    public UnsolicitedCardReaderWriterFaultAppender(UnsolicitedCardReaderWriterFaultMessageListener messageListener) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        final CardReaderWriterAdditionalDataAppender additionalDataAppender
                = new CardReaderWriterAdditionalDataAppender();

        final CardReaderWriterSuppliesStatusAppender suppliesStatusAppender
                = new CardReaderWriterSuppliesStatusAppender(UnsolicitedStatusMessage.COMMAND_NAME, additionalDataAppender);

        final CompletionDataAppender<CardReaderWriterFaultBuilder> completionDataAppender
                = new CompletionDataAppender<>(UnsolicitedStatusMessage.COMMAND_NAME, suppliesStatusAppender);

        final DiagnosticStatusAppender<CardReaderWriterFaultBuilder> diagnosticStatusAppender
                = new DiagnosticStatusAppender<>(UnsolicitedStatusMessage.COMMAND_NAME, completionDataAppender);

        final CardReaderWriterErrorSeverityAppender errorSeverityAppender
                = new CardReaderWriterErrorSeverityAppender(UnsolicitedStatusMessage.COMMAND_NAME, diagnosticStatusAppender);
        this.subFieldAppender = new CardReaderWriterTransactionStatusAppender(UnsolicitedStatusMessage.COMMAND_NAME, errorSeverityAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final CardReaderWriterFaultBuilder cardReaderWriterFaultBuilder = new CardReaderWriterFaultBuilder();
        subFieldAppender.appendComponent(ndcCharBuffer, cardReaderWriterFaultBuilder, deviceConfiguration);

        final UnsolicitedStatusMessage<? extends UnsolicitedStatusInformation> message
                = stateObject.withStatusInformation(cardReaderWriterFaultBuilder.build())
                .build();
        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<CardReaderWriterFault> cardReaderWriterFaultMessage =
                (UnsolicitedStatusMessage<CardReaderWriterFault>) message;

        messageListener.onUnsolicitedCardReaderWriterStatusMessage(cardReaderWriterFaultMessage);
    }
}
