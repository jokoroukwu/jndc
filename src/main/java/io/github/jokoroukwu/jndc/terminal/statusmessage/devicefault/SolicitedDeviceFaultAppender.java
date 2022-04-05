package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.ConfigurableNdcComponentAppenderFactory;
import io.github.jokoroukwu.jndc.ConfigurableNdcComponentAppenderFactoryBase;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.SolicitedCardReaderWriterFaultAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.SolicitedGenericDeviceFaultAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static io.github.jokoroukwu.jndc.exception.ConfigurationException.onConfigError;

public class SolicitedDeviceFaultAppender
        implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final ConfigurableNdcComponentAppenderFactory<Dig, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory;

    public SolicitedDeviceFaultAppender(ConfigurableNdcComponentAppenderFactory<Dig, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
    }

    public SolicitedDeviceFaultAppender(TerminalMessageListener messageListener) {
        final Map<Dig, ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>>> appenderMap
                = new EnumMap<>(Dig.class);
        appenderMap.put(Dig.MAGNETIC_CARD_READER_WRITER, new SolicitedCardReaderWriterFaultAppender(messageListener));
        putGenericAppenders(appenderMap, messageListener);
        appenderFactory = new ConfigurableNdcComponentAppenderFactoryBase<>(appenderMap);
    }

    private static void putGenericAppenders(Map<Dig, ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>>> appenderMap,
                                            TerminalMessageListener messageListener) {

        for (Dig dig : EnumSet.complementOf(EnumSet.of(Dig.MAGNETIC_CARD_READER_WRITER))) {
            appenderMap.put(dig, new SolicitedGenericDeviceFaultAppender(dig, messageListener));
        }
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(DeviceFault.COMMAND_NAME,
                        "Status Information", errorMessage, ndcCharBuffer));

        final Dig dig = readDig(ndcCharBuffer);
        appenderFactory.getAppender(dig)
                .ifPresentOrElse(appender -> appender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration),
                        () -> onConfigError(DeviceFault.COMMAND_NAME, "no appender configured for DIG " + dig));
    }

    private Dig readDig(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(Dig::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(DeviceFault.COMMAND_NAME, "Device Identifier Graphic (DIG)",
                        errorMessage, ndcCharBuffer));
    }
}
