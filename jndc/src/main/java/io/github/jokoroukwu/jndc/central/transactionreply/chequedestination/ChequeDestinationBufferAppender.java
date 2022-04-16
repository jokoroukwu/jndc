package io.github.jokoroukwu.jndc.central.transactionreply.chequedestination;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;

public class ChequeDestinationBufferAppender extends IdentifiableBufferAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Cheque(s) Destination Buffer";

    public ChequeDestinationBufferAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public ChequeDestinationBufferAppender() {
        super(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ChequeDestinationBuffer::forValue)
                .resolve(stateObject::withChequeDestinationBuffer,
                        errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(), FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return ChequeDestinationBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString();
    }
}
