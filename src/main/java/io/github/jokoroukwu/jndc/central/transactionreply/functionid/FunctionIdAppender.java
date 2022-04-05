package io.github.jokoroukwu.jndc.central.transactionreply.functionid;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate.ScreenDisplayUpdateAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withFieldName;

public class FunctionIdAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Function Identifier";
    private final CustomFunctionIdFactory customFunctionIdFactory;

    public FunctionIdAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender,
                              CustomFunctionIdFactory customFunctionIdFactory) {
        super(nextAppender);
        this.customFunctionIdFactory = customFunctionIdFactory;
    }

    public FunctionIdAppender() {
        this(new ScreenDisplayUpdateAppender(), CustomFunctionIdFactory.EMPTY);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final char functionIdValue = (char) ndcCharBuffer.tryReadNextChar()
                .getOrThrow(errorMessage -> withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer));

        StandardFunction.forValue(functionIdValue)
                .or(() -> customFunctionIdFactory.getCustomFunctionId(functionIdValue))
                .resolve(stateObject::withFunctionId,
                        errorMessage -> NdcMessageParseException.onFieldParseError(FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
