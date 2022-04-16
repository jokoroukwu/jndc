package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate;

import io.github.jokoroukwu.jndc.ConfigurableNdcComponentAppenderFactory;
import io.github.jokoroukwu.jndc.ConfigurableNdcComponentAppenderFactoryBase;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

import static io.github.jokoroukwu.jndc.exception.ConfigurationException.onConfigError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId.SEND_SUPPLY_COUNTERS_BASIC;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId.SEND_SUPPLY_COUNTERS_EXTENDED;

public class TerminalStateAppender implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {
    public static final String MESSAGE_ID_FIELD = "Terminal State Message Identifier";
    private final ConfigurableNdcComponentAppenderFactory<TerminalStateMessageId, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory;

    public TerminalStateAppender(ConfigurableNdcComponentAppenderFactory<TerminalStateMessageId, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactory) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
    }

    public TerminalStateAppender(TerminalMessageListener messageListener) {
        final Map<TerminalStateMessageId, ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>>> appenderMap
                = new EnumMap<>(TerminalStateMessageId.class);
        appenderMap.put(SEND_SUPPLY_COUNTERS_BASIC, new SupplyCountersBasicAppender(messageListener));
        appenderMap.put(SEND_SUPPLY_COUNTERS_EXTENDED, new SupplyCountersExtendedAppender(messageListener));
        this.appenderFactory = new ConfigurableNdcComponentAppenderFactoryBase<>(appenderMap);
    }

    @Override

    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(COMMAND_NAME, MESSAGE_ID_FIELD, errorMessage, ndcCharBuffer));

        ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(TerminalStateMessageId::forValue)
                .ifEmpty(errorMessage -> onFieldParseError(COMMAND_NAME, MESSAGE_ID_FIELD, errorMessage, ndcCharBuffer))
                .ifPresent(terminalMessageId -> appenderFactory.getAppender(terminalMessageId)
                        .ifPresentOrElse(appender -> appender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration),
                                () -> onConfigError(COMMAND_NAME, String.format("no appender for '%s' Terminal State Message Identifier",
                                        terminalMessageId)))
                );
    }
}
