package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class SolicitedGenericDeviceFaultAppender
        implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final Dig dig;
    private final SolicitedGenericDeviceFaultMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<MacAcceptor<?>> macAppender;
    private final ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> subFieldAppender;

    public SolicitedGenericDeviceFaultAppender(Dig dig,
                                               SolicitedGenericDeviceFaultMessageListener messageListener,
                                               ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> subFieldAppender,
                                               ConfigurableNdcComponentAppender<MacAcceptor<?>> macAppender) {
        this.dig = ObjectUtils.validateNotNull(dig, "DIG");
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.subFieldAppender = ObjectUtils.validateNotNull(subFieldAppender, "subFieldAppender");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
    }

    public SolicitedGenericDeviceFaultAppender(Dig dig, SolicitedGenericDeviceFaultMessageListener messageListener) {
        this(
                dig,
                messageListener,
                new GenericTransactionStatusAppender(SolicitedStatusMessage.COMMAND_NAME),
                new MacAppender<>(SolicitedStatusMessage.COMMAND_NAME + ": " + StatusDescriptor.DEVICE_FAULT.toString())
        );
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final GenericDeviceFaultBuilder genericDeviceFaultBuilder = new GenericDeviceFaultBuilder()
                .withDig(dig);
        subFieldAppender.appendComponent(ndcCharBuffer, genericDeviceFaultBuilder, deviceConfiguration);

        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<? extends SolicitedStatusInformation> solicitedStatusMessage = stateObject
                .withStatusInformation(genericDeviceFaultBuilder.buildWithNoValidation())
                .build();

        @SuppressWarnings("unchecked") final SolicitedStatusMessage<GenericDeviceFault> genericDeviceFaultMessage =
                (SolicitedStatusMessage<GenericDeviceFault>) solicitedStatusMessage;
        messageListener.onSolicitedGenericDeviceFaultMessage(genericDeviceFaultMessage);
    }
}
