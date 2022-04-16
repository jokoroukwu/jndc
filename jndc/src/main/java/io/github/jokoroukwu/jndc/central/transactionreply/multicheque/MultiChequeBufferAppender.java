package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;

public class MultiChequeBufferAppender extends IdentifiableBufferAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Process Multiple Cheques Buffer";
    private final NdcComponentReader<List<Cheque>> chequeReader;

    public MultiChequeBufferAppender(NdcComponentReader<List<Cheque>> chequeReader, ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
        this.chequeReader = ObjectUtils.validateNotNull(chequeReader, "Cheque reader");
    }

    public MultiChequeBufferAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        this(new ChequeReader(), nextAppender);
    }

    public MultiChequeBufferAppender() {
        this(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);
        final List<Cheque> cheques = chequeReader.readComponent(ndcCharBuffer);
        stateObject.withMultiChequeBuffer(new MultiChequeBuffer(cheques));

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return MultiChequeBuffer.ID;
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
