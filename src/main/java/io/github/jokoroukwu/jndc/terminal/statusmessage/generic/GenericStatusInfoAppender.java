package io.github.jokoroukwu.jndc.terminal.statusmessage.generic;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;


public class GenericStatusInfoAppender implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {
    private final GenericSolicitedStatusMessageListener messageListener;
    private final ConfigurableNdcComponentReader<String> dataReader;
    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;


    public GenericStatusInfoAppender(GenericSolicitedStatusMessageListener messageListener,
                                     ConfigurableNdcComponentReader<String> dataReader,
                                     ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.dataReader = ObjectUtils.validateNotNull(dataReader, "dataReader");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
    }

    public GenericStatusInfoAppender(GenericSolicitedStatusMessageListener messageListener,
                                     ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender) {
        this(messageListener, GenericDataReader.INSTANCE, macAppender);
    }

    public GenericStatusInfoAppender(GenericSolicitedStatusMessageListener messageListener) {
        this(messageListener,
                GenericDataReader.INSTANCE,
                new MacAppender<>(TerminalMessageClass.SOLICITED.toString() + ": " + TerminalMessageSubClass.STATUS_MESSAGE.toString())
        );
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final String statusInformationData = dataReader.readComponent(ndcCharBuffer, deviceConfiguration);
        stateObject.withStatusInformation(new GenericSolicitedStatusInformation(statusInformationData));
        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<? extends SolicitedStatusInformation> solicitedStatusMessage = stateObject
                .build();
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<GenericSolicitedStatusInformation> genericSolicitedStatusMessage
                = (SolicitedStatusMessage<GenericSolicitedStatusInformation>) solicitedStatusMessage;
        messageListener.onSolicitedGenericStatusMessage(genericSolicitedStatusMessage);
    }

}
