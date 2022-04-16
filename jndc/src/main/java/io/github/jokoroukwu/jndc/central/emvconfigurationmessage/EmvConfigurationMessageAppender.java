package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.IccCurrencyDataObjectsTableAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable.IccLanguageSupportTableAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalAcceptableAidsTableAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable.TerminalDataObjectsTableAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.IccTransactionDataObjectsTableAppender;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

public class EmvConfigurationMessageAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String CONFIG_DATA_FIELD = "Configuration Data";
    private final NdcComponentAppenderFactory<EmvConfigMessageSubClass, CentralMessageMeta> appenderFactory;

    public EmvConfigurationMessageAppender(NdcComponentAppenderFactory<EmvConfigMessageSubClass, CentralMessageMeta> appenderFactory) {
        this.appenderFactory = ObjectUtils.validateNotNull(appenderFactory, "appenderFactory");
    }

    public EmvConfigurationMessageAppender(CentralMessageListener messageListener) {
        ObjectUtils.validateNotNull(messageListener, "messageListener");
        final Map<EmvConfigMessageSubClass, NdcComponentAppender<CentralMessageMeta>> appenderMap =
                new EnumMap<>(EmvConfigMessageSubClass.class);
        appenderMap.put(EmvConfigMessageSubClass.CURRENCY, new IccCurrencyDataObjectsTableAppender(messageListener));
        appenderMap.put(EmvConfigMessageSubClass.TRANSACTION, new IccTransactionDataObjectsTableAppender(messageListener));
        appenderMap.put(EmvConfigMessageSubClass.LANGUAGE_SUPPORT, new IccLanguageSupportTableAppender(messageListener));
        appenderMap.put(EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS, new TerminalDataObjectsTableAppender(messageListener));
        appenderMap.put(EmvConfigMessageSubClass.TERMINAL_ACCEPTABLE_AIDS, new TerminalAcceptableAidsTableAppender(messageListener));

        appenderFactory = new AppenderFactoryBase<>(appenderMap);
    }

    @Override
    public void appendComponent(NdcCharBuffer messageStream, CentralMessageMeta stateObject) {
        final String messageName = CentralMessageClass.EMV_CONFIGURATION.toString();
        messageStream.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(messageName,
                        "Message Sub-Class", errorMessage, messageStream));

        final EmvConfigMessageSubClass subclass = messageStream.tryReadNextChar()
                .flatMapToObject(EmvConfigMessageSubClass::forValue)
                .ifEmpty(errorMessage
                        -> NdcMessageParseException.onFieldParseError(messageName, "EMV Configuration Message Sub-Class", errorMessage, messageStream))
                .get();

        messageStream.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(fullMessageName(subclass), CONFIG_DATA_FIELD, errorMessage, messageStream));

        appenderFactory.getAppender(subclass)
                .resolve(appender -> appender.appendComponent(messageStream, stateObject), ConfigurationException::new);

    }

    private String fullMessageName(EmvConfigMessageSubClass subClass) {
        return CentralMessageClass.EMV_CONFIGURATION.toString() + ": " + subClass;
    }

}
