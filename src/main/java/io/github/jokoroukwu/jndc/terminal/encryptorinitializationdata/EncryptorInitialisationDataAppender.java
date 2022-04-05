package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic.GenericEncryptorInformationAppender;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv.NewKvvAppender;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInitialisationData.COMMAND_NAME;


public class EncryptorInitialisationDataAppender implements NdcComponentAppender<TerminalMessageMeta> {
    private final ConfigurableNdcComponentAppenderFactory<InformationId, EncryptorInitialisationDataBuilder<EncryptorInformation>> appenderFactory;
    private final DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier;

    public EncryptorInitialisationDataAppender(ConfigurableNdcComponentAppenderFactory<InformationId, EncryptorInitialisationDataBuilder<EncryptorInformation>> appenderFactory,
                                               DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier");
    }

    public EncryptorInitialisationDataAppender(TerminalMessageListener terminalMessageListener,
                                               DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier");

        final Map<InformationId, ConfigurableNdcComponentAppender<EncryptorInitialisationDataBuilder<EncryptorInformation>>> appenderMap
                = new EnumMap<>(InformationId.class);
        appenderMap.put(InformationId.NEW_KVV, new NewKvvAppender(terminalMessageListener));

        for (InformationId id : EnumSet.complementOf(EnumSet.of(InformationId.NEW_KVV))) {
            appenderMap.put(id, new GenericEncryptorInformationAppender(terminalMessageListener, id));
        }
        appenderFactory = new ConfigurableNdcComponentAppenderFactoryBase<>(appenderMap);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalMessageMeta stateObject) {
        final Luno luno = Luno.readLuno(ndcCharBuffer)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, "LUNO", errorMessage, ndcCharBuffer));

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, "Information Identifier", errorMessage, ndcCharBuffer));
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, "Information Identifier", errorMessage, ndcCharBuffer));

        final DeviceConfiguration deviceConfiguration = deviceConfigurationSupplier.getConfiguration(stateObject);
        final InformationId informationId = readInformationId(ndcCharBuffer);
        final EncryptorInitialisationDataBuilder<EncryptorInformation> messageBuilder = new EncryptorInitialisationDataBuilder<>()
                .withLuno(luno);

        appenderFactory.getAppender(informationId)
                .ifPresentOrElse(appender -> appender.appendComponent(ndcCharBuffer, messageBuilder, deviceConfiguration),
                        () -> ConfigurationException.onConfigError(COMMAND_NAME,
                                "no appender configured for: " + informationId));
    }


    private InformationId readInformationId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(InformationId::forValue)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withMessage(COMMAND_NAME, "Information Identifier", errorMessage, ndcCharBuffer));
    }

}
