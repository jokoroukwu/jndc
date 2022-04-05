package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.TimeVariantNumber;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;

public class TimeVariantNumberAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Time Variant Number";

    public TimeVariantNumberAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextFieldAppender) {
        super(nextFieldAppender);
    }

    public TimeVariantNumberAppender() {
        super(new TopOfReceiptTransactionFlagAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        FIELD_NAME, errorMessage, ndcCharBuffer));

        TimeVariantNumber.tryReadTimeVariantNumber(ndcCharBuffer)
                .resolve(stateObject::withTimeVariantNumber,
                        errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                                FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
