package io.github.jokoroukwu.jndc.terminal.statusmessage;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader.UnsolicitedCardReaderWriterStatusInfoAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.UnsolicitedGenericDeviceFaultAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.UnsolicitedGenericDeviceFaultMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure.PowerFailureMessageAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClockMessageAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage.COMMAND_NAME;

public class UnsolicitedStatusMessageAppender implements NdcComponentAppender<TerminalMessageMeta> {
    private final ConfigurableNdcComponentAppenderFactory<Dig, UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> appenderFactory;
    private final DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier;

    public UnsolicitedStatusMessageAppender(ConfigurableNdcComponentAppenderFactory<Dig, UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> appenderFactory,
                                            DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier,
                "deviceConfigurationSupplier");
    }

    public UnsolicitedStatusMessageAppender(TerminalMessageListener messageListener,
                                            DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier,
                "deviceConfigurationSupplier");

        final Map<Dig, ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>>> appenderMap
                = new EnumMap<>(Dig.class);
        appenderMap.put(Dig.MAGNETIC_CARD_READER_WRITER, new UnsolicitedCardReaderWriterStatusInfoAppender(messageListener));
        appenderMap.put(Dig.TIME_OF_DAY_CLOCK, new TimeOfDayClockMessageAppender(messageListener));
        appenderMap.put(Dig.COMMUNICATIONS, new PowerFailureMessageAppender(messageListener));
        putGenericAppenders(appenderMap, messageListener);

        appenderFactory = new ConfigurableNdcComponentAppenderFactoryBase<>(appenderMap);
    }

    private static void putGenericAppenders(Map<Dig,
            ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>>> appenderMap,
                                            UnsolicitedGenericDeviceFaultMessageListener messageListener) {

        final EnumSet<Dig> digsWithImplementations = EnumSet.of(
                Dig.TIME_OF_DAY_CLOCK,
                Dig.MAGNETIC_CARD_READER_WRITER,
                Dig.COMMUNICATIONS
        );

        for (Dig dig : EnumSet.complementOf(digsWithImplementations)) {
            appenderMap.put(dig, new UnsolicitedGenericDeviceFaultAppender(dig, messageListener));
        }
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalMessageMeta stateObject) {
        final Luno luno = Luno.readLuno(ndcCharBuffer)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "'LUNO'", errorMessage, ndcCharBuffer));

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> onNoFieldSeparator(COMMAND_NAME, "'Status Information'", errorMessage, ndcCharBuffer));
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> onNoFieldSeparator(COMMAND_NAME, "'Status Information'", errorMessage, ndcCharBuffer));

        final Dig dig = readDig(ndcCharBuffer);
        final ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> nextAppender
                = appenderFactory.getAppender(dig)
                .orElseThrow(()
                        -> ConfigurationException.withMessage(COMMAND_NAME, String.format("no appender configured for DIG '%s'", dig)));

        final UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> messageBuilder
                = new UnsolicitedStatusMessageBuilder<>()
                .withLuno(luno);
        final DeviceConfiguration deviceConfiguration = deviceConfigurationSupplier.getConfiguration(stateObject);
        nextAppender.appendComponent(ndcCharBuffer, messageBuilder, deviceConfiguration);
    }

    private Dig readDig(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(Dig::forValue)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "Device Identifier Graphic (DIG)",
                        errorMessage, ndcCharBuffer));
    }
}
