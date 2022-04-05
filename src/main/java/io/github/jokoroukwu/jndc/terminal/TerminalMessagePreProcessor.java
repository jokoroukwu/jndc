package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcComponentAppenderFactory;
import io.github.jokoroukwu.jndc.NdcMessagePreProcessor;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInitialisationDataAppender;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMetaBase;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.UnsolicitedStatusMessageAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.EnumMap;
import java.util.Map;

public class TerminalMessagePreProcessor implements NdcMessagePreProcessor {
    public static final String MESSAGE_CLASS_FIELD = "Terminal Message Class";
    public static final String MESSAGE_SUBCLASS_FIELD = "Terminal Message Sub-Class";

    private final NdcComponentAppenderFactory<TerminalMessageSubClass, TerminalMessageMeta> solicitedAppenderFactory;
    private final NdcComponentAppenderFactory<TerminalMessageSubClass, TerminalMessageMeta> unsolicitedAppenderFactory;

    public TerminalMessagePreProcessor(NdcComponentAppenderFactory<TerminalMessageSubClass, TerminalMessageMeta> solicitedAppenderFactory,
                                       NdcComponentAppenderFactory<TerminalMessageSubClass, TerminalMessageMeta> unsolicitedAppenderFactory) {
        this.solicitedAppenderFactory = ObjectUtils.validateNotNull(solicitedAppenderFactory, "solicitedAppenderFactory");
        this.unsolicitedAppenderFactory = ObjectUtils.validateNotNull(unsolicitedAppenderFactory, "unsolicitedAppenderFactory");
    }

    public TerminalMessagePreProcessor(TerminalMessageListener messageListener,
                                       DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        ObjectUtils.validateNotNull(messageListener, "messageListener");
        ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier ");

        final Map<TerminalMessageSubClass, NdcComponentAppender<TerminalMessageMeta>> solicitedAppenderMap
                = new EnumMap<>(TerminalMessageSubClass.class);
        solicitedAppenderMap.put(TerminalMessageSubClass.STATUS_MESSAGE, new SolicitedStatusMessageAppender(messageListener, deviceConfigurationSupplier));
        solicitedAppenderMap.put(TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA, new EncryptorInitialisationDataAppender(messageListener, deviceConfigurationSupplier));
        solicitedAppenderFactory = new BaseNdcComponentAppenderFactory<>(solicitedAppenderMap);

        final Map<TerminalMessageSubClass, NdcComponentAppender<TerminalMessageMeta>> unsolicitedAppenderMap
                = new EnumMap<>(TerminalMessageSubClass.class);
        unsolicitedAppenderMap.put(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE, new TransactionRequestMessageAppender(messageListener, deviceConfigurationSupplier));
        unsolicitedAppenderMap.put(TerminalMessageSubClass.STATUS_MESSAGE, new UnsolicitedStatusMessageAppender(messageListener, deviceConfigurationSupplier));
        unsolicitedAppenderFactory = new BaseNdcComponentAppenderFactory<>(unsolicitedAppenderMap);
    }

    @Override
    public void processMessage(NdcCharBuffer ndcCharBuffer) {
        final TerminalMessageClass messageClass = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(TerminalMessageClass::forValue)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(MESSAGE_CLASS_FIELD, errorMessage, ndcCharBuffer))
                .get();

        final TerminalMessageSubClass messageSubClass = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(TerminalMessageSubClass::forValue)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(MESSAGE_SUBCLASS_FIELD, errorMessage, ndcCharBuffer))
                .get();

        final NdcComponentAppender<TerminalMessageMeta> appender = getAppender(messageClass, messageSubClass)
                .getOrThrow(ConfigurationException::new);
        final TerminalMessageMetaBase messageMeta = new TerminalMessageMetaBase(messageClass, messageSubClass);

        appender.appendComponent(ndcCharBuffer, messageMeta);
    }

    protected DescriptiveOptional<NdcComponentAppender<TerminalMessageMeta>> getAppender(TerminalMessageClass messageClass,
                                                                                         TerminalMessageSubClass subClass) {
        return messageClass == TerminalMessageClass.SOLICITED
                ? solicitedAppenderFactory.getAppender(subClass)
                : unsolicitedAppenderFactory.getAppender(subClass);

    }
}
