package io.github.jokoroukwu.jndc.tsn;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;

public class TransactionSerialNumberAppender<T extends TransactionSerialNumberAcceptor<?>>
        extends ChainedConfigurableNdcComponentAppender<T> {
    public static final String FIELD_NAME = "Transaction Serial Number";
    private final String commandName;

    public TransactionSerialNumberAppender(String commandName, ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));

        ndcCharBuffer.tryReadInt(4)
                .resolve(stateObject::withTransactionSerialNumber,
                        errorMessage -> onFieldParseError(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
