package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class UnsolicitedCardReaderWriterStatusInfoAppender
        implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {

    private final UnsolicitedCardReaderWriterStatusInfoMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> subFieldAppender;

    public UnsolicitedCardReaderWriterStatusInfoAppender(UnsolicitedCardReaderWriterStatusInfoMessageListener messageListener,
                                                         ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> subFieldAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.subFieldAppender = ObjectUtils.validateNotNull(subFieldAppender, "subFieldAppender");
    }

    public UnsolicitedCardReaderWriterStatusInfoAppender(UnsolicitedCardReaderWriterStatusInfoMessageListener messageListener) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        final CardReaderWriterAdditionalDataAppender additionalDataAppender
                = new CardReaderWriterAdditionalDataAppender();

        final CardReaderWriterSuppliesStatusAppender suppliesStatusAppender
                = new CardReaderWriterSuppliesStatusAppender(UnsolicitedStatusMessage.COMMAND_NAME, additionalDataAppender);

        final CompletionDataAppender<CardReaderStatusInfoBuilder> completionDataAppender
                = new CompletionDataAppender<>(UnsolicitedStatusMessage.COMMAND_NAME, suppliesStatusAppender);

        final DiagnosticStatusAppender<CardReaderStatusInfoBuilder> diagnosticStatusAppender
                = new DiagnosticStatusAppender<>(UnsolicitedStatusMessage.COMMAND_NAME, completionDataAppender);

        final CardReaderWriterErrorSeverityAppender errorSeverityAppender
                = new CardReaderWriterErrorSeverityAppender(UnsolicitedStatusMessage.COMMAND_NAME, diagnosticStatusAppender);
        this.subFieldAppender = new CardReaderWriterTransactionStatusAppender(UnsolicitedStatusMessage.COMMAND_NAME, errorSeverityAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final CardReaderStatusInfoBuilder cardReaderStatusInfoBuilder = new CardReaderStatusInfoBuilder();
        subFieldAppender.appendComponent(ndcCharBuffer, cardReaderStatusInfoBuilder, deviceConfiguration);

        final UnsolicitedStatusMessage<? extends UnsolicitedStatusInformation> message
                = stateObject.withStatusInformation(cardReaderStatusInfoBuilder.build())
                .build();
        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<CardReaderWriterStatusInfo> cardReaderWriterFaultMessage =
                (UnsolicitedStatusMessage<CardReaderWriterStatusInfo>) message;

        messageListener.onUnsolicitedCardReaderWriterStatusInfoMessage(cardReaderWriterFaultMessage);
    }
}
