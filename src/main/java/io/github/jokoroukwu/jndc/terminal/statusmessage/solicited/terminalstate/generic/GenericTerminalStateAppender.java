package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.generic;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.generic.GenericDataReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class GenericTerminalStateAppender implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {
    private final GenericTerminalStateMessageListener messageListener;
    private final TerminalStateMessageId messageId;
    private final ConfigurableNdcComponentReader<String> dataReader;
    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> macAppender;

    public GenericTerminalStateAppender(GenericTerminalStateMessageListener messageListener,
                                        TerminalStateMessageId messageId, ConfigurableNdcComponentReader<String> dataReader, ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> macAppender) {
        this.messageListener = messageListener;
        this.messageId = ObjectUtils.validateNotNull(messageId, "messageId");
        this.dataReader = ObjectUtils.validateNotNull(dataReader, "dataReader");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
    }

    public GenericTerminalStateAppender(TerminalStateMessageId messageId,
                                        ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> macAppender, GenericTerminalStateMessageListener messageListener) {
        this(messageListener, messageId, GenericDataReader.INSTANCE, macAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject, DeviceConfiguration deviceConfiguration) {
        final String data = dataReader.readComponent(ndcCharBuffer, deviceConfiguration);
        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);
        final SolicitedStatusMessageBuilder<? super GenericTerminalState> messageBuilder = stateObject;
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<GenericTerminalState> genericTerminalStateMessage
                = (SolicitedStatusMessage<GenericTerminalState>) messageBuilder
                        .withStatusInformation(new GenericTerminalState(messageId, data, null))
                        .build();

        messageListener.onGenericTerminalStateMessage(genericTerminalStateMessage);
    }
}
