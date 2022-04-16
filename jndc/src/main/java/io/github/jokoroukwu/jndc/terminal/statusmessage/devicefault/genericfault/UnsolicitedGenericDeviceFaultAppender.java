package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class UnsolicitedGenericDeviceFaultAppender
        implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {

    private final Dig dig;
    private final UnsolicitedGenericDeviceFaultMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> subFieldAppender;


    public UnsolicitedGenericDeviceFaultAppender(Dig dig,
                                                 UnsolicitedGenericDeviceFaultMessageListener messageListener,
                                                 ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> subFieldAppender) {
        this.dig = ObjectUtils.validateNotNull(dig, "'DIG'");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.subFieldAppender = ObjectUtils.validateNotNull(subFieldAppender, "subFieldAppender");
    }

    public UnsolicitedGenericDeviceFaultAppender(Dig dig, UnsolicitedGenericDeviceFaultMessageListener messageListener) {
        this(dig, messageListener, new GenericTransactionStatusAppender(UnsolicitedStatusMessage.COMMAND_NAME));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final GenericDeviceFaultBuilder genericDeviceFaultBuilder = new GenericDeviceFaultBuilder()
                .withDig(dig);
        subFieldAppender.appendComponent(ndcCharBuffer, genericDeviceFaultBuilder, deviceConfiguration);

        final UnsolicitedStatusMessage<? extends UnsolicitedStatusInformation> message
                = stateObject.withStatusInformation(genericDeviceFaultBuilder.build())
                .build();
        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<GenericDeviceFault> genericDeviceFaultMessage
                = (UnsolicitedStatusMessage<GenericDeviceFault>) message;
        messageListener.onUnsolicitedGenericDeviceFaultMessage(genericDeviceFaultMessage);
    }
}
