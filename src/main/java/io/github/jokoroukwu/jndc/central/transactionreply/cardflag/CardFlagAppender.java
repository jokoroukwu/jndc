package io.github.jokoroukwu.jndc.central.transactionreply.cardflag;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterDataListAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class CardFlagAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Card Return/Retain Flag";

    public CardFlagAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public CardFlagAppender() {
        super(new PrinterDataListAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(CardFlag::forValue)
                .resolve(stateObject::withCardFlag, errorMessage -> onFieldParseError(FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
