package io.github.jokoroukwu.jndc.central.datacommand.customisationdata;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandSubClass;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload.ConfigurationIdNumberLoadAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommandAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.reader.EnhancedConfigParamsLoadCommandAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommandAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardDataLoadCommandAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload.StateTablesLoadCommandAppender;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

import static io.github.jokoroukwu.jndc.exception.ConfigurationException.withMessage;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class CustomisationDataCommandAppender implements NdcComponentAppender<DataCommandBuilder<NdcComponent>> {
    public static final String MESSAGE_ID_FIELD = "Message Identifier";

    private final NdcComponentAppenderFactory<MessageId, DataCommandBuilder<NdcComponent>> appenderFactory;

    public CustomisationDataCommandAppender(NdcComponentAppenderFactory<MessageId, DataCommandBuilder<NdcComponent>> readerFactory) {
        this.appenderFactory = ObjectUtils.validateNotNull(readerFactory, "appenderFactory");
    }

    public CustomisationDataCommandAppender(CentralMessageListener centralMessageListener) {
        final Map<MessageId, NdcComponentAppender<DataCommandBuilder<NdcComponent>>> factoryMap = new EnumMap<>(MessageId.class);
        factoryMap.put(MessageId.DATE_AND_TIME, new DateTimeLoadCommandAppender(centralMessageListener));
        factoryMap.put(MessageId.ENHANCED_CONFIG_DATA, new EnhancedConfigParamsLoadCommandAppender(centralMessageListener));
        factoryMap.put(MessageId.STATE_TABLE, new StateTablesLoadCommandAppender(centralMessageListener));
        factoryMap.put(MessageId.FIT_DATA, new FitDataLoadCommandAppender(centralMessageListener));
        factoryMap.put(MessageId.CONFIG_ID_NUMBER, new ConfigurationIdNumberLoadAppender(centralMessageListener));
        factoryMap.put(MessageId.SCREEN_KEYBOARD_DATA, new ScreenKeyboardDataLoadCommandAppender(centralMessageListener));

        this.appenderFactory = new AppenderFactoryBase<>(factoryMap);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, DataCommandBuilder<NdcComponent> stateObject) {
        ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(MessageId::forValue)
                .ifEmpty(message -> onFieldParseError(CentralMessageClass.DATA_COMMAND + ": " + DataCommandSubClass.CUSTOMISATION_DATA,
                        MESSAGE_ID_FIELD, message, ndcCharBuffer))
                .map(this::getAppender)
                .resolve(appender -> appender.appendComponent(ndcCharBuffer, stateObject),
                        ConfigurationException::onConfigError);
    }

    private NdcComponentAppender<DataCommandBuilder<NdcComponent>> getAppender(MessageId messageId) {
        return appenderFactory.getAppender(messageId)
                .getOrThrow(errorMessage -> withMessage(CentralMessageClass.DATA_COMMAND + ": " + DataCommandSubClass.CUSTOMISATION_DATA, errorMessage));
    }
}
