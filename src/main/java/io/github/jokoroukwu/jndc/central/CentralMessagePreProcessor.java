package io.github.jokoroukwu.jndc.central;

import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessageAppender;
import io.github.jokoroukwu.jndc.central.terminalcommand.TerminalCommandAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandAppender;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

public class CentralMessagePreProcessor implements NdcMessagePreProcessor {
    private final NdcComponentAppenderFactory<CentralMessageClass, CentralMessageMeta> appenderFactory;

    public CentralMessagePreProcessor(NdcComponentAppenderFactory<CentralMessageClass, CentralMessageMeta> appenderFactory) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
    }

    public CentralMessagePreProcessor(CentralMessageListener messageListener,
                                      DeviceConfigurationSupplier<CentralMessageMeta> deviceConfigurationSupplier) {
        ObjectUtils.validateNotNull(messageListener, "messageListener");
        ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier");

        final Map<CentralMessageClass, NdcComponentAppender<CentralMessageMeta>> appenderMap = new EnumMap<>(CentralMessageClass.class);
        appenderMap.put(CentralMessageClass.EMV_CONFIGURATION, new EmvConfigurationMessageAppender(messageListener));
        appenderMap.put(CentralMessageClass.TERMINAL_COMMAND, new TerminalCommandAppender(messageListener));
        appenderMap.put(CentralMessageClass.DATA_COMMAND, new DataCommandAppender(messageListener));
        appenderMap.put(CentralMessageClass.TRANSACTION_REPLY_COMMAND,
                new TransactionReplyCommandAppender(messageListener, deviceConfigurationSupplier));
        appenderFactory = new AppenderFactoryBase<>(appenderMap);
    }

    public void processMessage(NdcCharBuffer ndcCharBuffer) {
        ObjectUtils.validateNotNull(ndcCharBuffer, "ndcCharBuffer");

        final CentralMessageClass centralMessageClass = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(CentralMessageClass::forValue)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withCommandName("Message Class", errorMessage, ndcCharBuffer));

        final char responseFlag = ndcCharBuffer.hasFieldDataRemaining() ? ndcCharBuffer.readNextChar() : NdcConstants.NULL_CHAR;

        final Luno luno = Luno.readLuno(ndcCharBuffer)
                .getOrThrow(errorMessage -> NdcMessageParseException.withFieldName(Luno.FIELD_NAME, errorMessage));

        final NdcComponentAppender<CentralMessageMeta> appender = appenderFactory.getAppender(centralMessageClass)
                .getOrThrow(ConfigurationException::new);
        appender.appendComponent(ndcCharBuffer, new CentralMessageMeta(centralMessageClass, responseFlag, luno));
    }
}
