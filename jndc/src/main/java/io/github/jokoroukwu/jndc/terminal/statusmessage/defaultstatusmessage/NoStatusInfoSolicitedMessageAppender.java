package io.github.jokoroukwu.jndc.terminal.statusmessage.defaultstatusmessage;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.function.Consumer;

public class NoStatusInfoSolicitedMessageAppender implements
        ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;
    private final Consumer<SolicitedStatusMessage<SolicitedStatusInformation>> messageConsumer;

    public NoStatusInfoSolicitedMessageAppender(ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender,
                                                Consumer<SolicitedStatusMessage<SolicitedStatusInformation>> messageConsumer) {
        this.messageConsumer = ObjectUtils.validateNotNull(messageConsumer, "messageConsumer");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<SolicitedStatusInformation> noStatusInfoMessage = stateObject.build();
        messageConsumer.accept(noStatusInfoMessage);
    }
}
