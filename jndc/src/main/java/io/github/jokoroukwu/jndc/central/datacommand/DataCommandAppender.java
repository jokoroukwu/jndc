package io.github.jokoroukwu.jndc.central.datacommand;


import io.github.jokoroukwu.jndc.*;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommandAppender;
import io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange.ExtendedEncryptionKeyChangeCommandAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.exception.ConfigurationException;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.EnumMap;
import java.util.Map;

public class DataCommandAppender implements NdcComponentAppender<CentralMessageMeta> {

    private final NdcComponentAppenderFactory<DataCommandSubClass, DataCommandBuilder<NdcComponent>> dataCommandReaderFactory;

    public DataCommandAppender(NdcComponentAppenderFactory<DataCommandSubClass, DataCommandBuilder<NdcComponent>> dataCommandReaderFactory) {
        this.dataCommandReaderFactory = ObjectUtils.validateNotNull(dataCommandReaderFactory, "dataCommandReaderFactory");
    }

    public DataCommandAppender(CentralMessageListener centralMessageListener) {
        final Map<DataCommandSubClass, NdcComponentAppender<DataCommandBuilder<NdcComponent>>> factoryMap =
                new EnumMap<>(DataCommandSubClass.class);
        factoryMap.put(DataCommandSubClass.CUSTOMISATION_DATA, new CustomisationDataCommandAppender(centralMessageListener));
        factoryMap.put(DataCommandSubClass.EXTENDED_ENCRYPTION_KEY_INFO, new ExtendedEncryptionKeyChangeCommandAppender(centralMessageListener));
        dataCommandReaderFactory = new AppenderFactoryBase<>(factoryMap);
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        final int messageSequenceNumber = readSequenceNumber(ndcCharBuffer);

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(CentralMessageClass.DATA_COMMAND.toString(), "'Message Sub-Class'",
                        errorMessage, ndcCharBuffer));

        final DataCommandSubClass subClass = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(DataCommandSubClass::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.DATA_COMMAND.toString(), "'Message Sub-Class'",
                        errorMessage, ndcCharBuffer));

        final DataCommandBuilder<NdcComponent> dataCommandBuilder = new DataCommandBuilder<>()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withMessageSequenceNumber(messageSequenceNumber)
                .withMessageSubclass(subClass);

        dataCommandReaderFactory.getAppender(subClass)
                .resolve(appender -> appender.appendComponent(ndcCharBuffer, dataCommandBuilder),
                        errorMessage -> ConfigurationException.onConfigError(CentralMessageClass.DATA_COMMAND.toString(), errorMessage));
    }

    private int readSequenceNumber(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(CentralMessageClass.DATA_COMMAND.toString(), "'Message Sequence Number'",
                        errorMessage, ndcCharBuffer));

        if (ndcCharBuffer.hasRemaining()) {
            return ndcCharBuffer.tryReadInt(3)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.DATA_COMMAND.toString(), "'Message Sequence Number'",
                            errorMessage, ndcCharBuffer));
        }
        //  field is empty
        return -1;
    }
}
