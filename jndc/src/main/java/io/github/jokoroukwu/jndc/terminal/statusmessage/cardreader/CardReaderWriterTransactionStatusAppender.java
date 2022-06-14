package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class CardReaderWriterTransactionStatusAppender extends DeviceFaultFieldAppender<CardReaderStatusInfoBuilder> {
    private final String commandName;

    public CardReaderWriterTransactionStatusAppender(String commandName,
                                                     ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    public CardReaderWriterTransactionStatusAppender(String commandName) {
        this(commandName, new CardReaderWriterErrorSeverityAppender(commandName));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CardReaderStatusInfoBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            ndcCharBuffer.tryReadNextChar()
                    .flatMapToObject(CardReaderWriterStatus::forValue)
                    .resolve(stateObject::withTransactionDeviceStatus, errorMessage
                            -> NdcMessageParseException.onFieldParseError(commandName, "'Transaction/Device Status'", errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
