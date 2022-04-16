package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry.AmountEntryFieldAppender;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class OperationCodeAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Operation Code Data";

    public OperationCodeAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public OperationCodeAppender() {
        super(new AmountEntryFieldAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME,
                        errorMessage, ndcCharBuffer));

        final String operationCodeData = readOperationCodeData(ndcCharBuffer);
        stateObject.withOperationCodeData(operationCodeData);
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private String readOperationCodeData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        return ndcCharBuffer.tryReadCharSequence(8)
                .ifEmpty(errorMessage
                        -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer))
                .get();
    }
}
