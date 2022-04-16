package io.github.jokoroukwu.jndc.terminal.statusmessage;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.TimeVariantNumber;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.statusmessage.defaultstatusmessage.NoStatusInfoSolicitedMessageAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SolicitedDeviceFaultAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.generic.GenericStatusInfoAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.ReadyBStatusAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor.*;

public class SolicitedStatusMessageAppender implements NdcComponentAppender<TerminalMessageMeta> {
    private final ConfigurableNdcComponentAppenderFactory<StatusDescriptor, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory;
    private final DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier;

    public SolicitedStatusMessageAppender(ConfigurableNdcComponentAppenderFactory<StatusDescriptor, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory,
                                          DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {

        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier,
                "deviceConfigurationSupplier");
    }

    public SolicitedStatusMessageAppender(TerminalMessageListener terminalMessageListener,
                                          DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier");

        final Map<StatusDescriptor, ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>>> appenderMap
                = new EnumMap<>(StatusDescriptor.class);
        appenderMap.put(SPECIFIC_COMMAND_REJECT, new GenericStatusInfoAppender(terminalMessageListener,
                new MacAppender<>(COMMAND_NAME + ":" + SPECIFIC_COMMAND_REJECT)));
        appenderMap.put(TERMINAL_STATE, new TerminalStateAppender(terminalMessageListener));
        appenderMap.put(DEVICE_FAULT, new SolicitedDeviceFaultAppender(terminalMessageListener));
        appenderMap.put(READY_B, new ReadyBStatusAppender(terminalMessageListener));

        final String commandRejectName = COMMAND_NAME + ": " + COMMAND_REJECT.toString();
        appenderMap.put(COMMAND_REJECT, new NoStatusInfoSolicitedMessageAppender(new MacAppender<>(commandRejectName),
                terminalMessageListener::onCommandRejectStatusMessage));

        final String readyCommandName = COMMAND_NAME + ": " + READY.toString();
        appenderMap.put(READY, new NoStatusInfoSolicitedMessageAppender(new MacAppender<>(readyCommandName),
                terminalMessageListener::onReadyStatusMessage));

        appenderFactory = new ConfigurableNdcComponentAppenderFactoryBase<>(appenderMap);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalMessageMeta stateObject) {
        final Luno luno = Luno.readLuno(ndcCharBuffer)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, "LUNO", errorMessage, ndcCharBuffer));

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, "Time Variant Number", errorMessage, ndcCharBuffer));

        final DeviceConfiguration deviceConfiguration = deviceConfigurationSupplier.getConfiguration(stateObject);

        final long timeVariantNumber = readTimeVariant(ndcCharBuffer, deviceConfiguration);
        final StatusDescriptor statusDescriptor = readStatusDescriptor(ndcCharBuffer);
        final SolicitedStatusMessageBuilder<SolicitedStatusInformation> solicitedStatusMessageBuilder =
                new SolicitedStatusMessageBuilder<>()
                        .withLuno(luno)
                        .withTimeVariantNumber(timeVariantNumber)
                        .withStatusDescriptor(statusDescriptor);

        appenderFactory.getAppender(statusDescriptor)
                .ifPresentOrElse(appender -> appender.appendComponent(ndcCharBuffer, solicitedStatusMessageBuilder, deviceConfiguration),
                        () -> ConfigurationException.onConfigError(COMMAND_NAME,
                                String.format("no appender configured for status descriptor '%s'", statusDescriptor)));
    }


    private StatusDescriptor readStatusDescriptor(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, "Status Descriptor", errorMessage, ndcCharBuffer));

        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(StatusDescriptor::forValue)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withMessage(COMMAND_NAME, "Status Descriptor", errorMessage, ndcCharBuffer));
    }

    private long readTimeVariant(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        //  Time Variant Number field is not sent unless MAC is enabled
        if (deviceConfiguration.isMacEnabled()) {
            //  the field will still be empty
            //  when Status Descriptor value is '9' or 'B'
            return TimeVariantNumber.tryReadTimeVariantNumber(ndcCharBuffer)
                    .getOrThrow(errorMessage
                            -> NdcMessageParseException.withMessage(COMMAND_NAME, "Time Variant Number", errorMessage, ndcCharBuffer));
        }
        return -1;
    }
}
