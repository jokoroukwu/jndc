package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;

public class DepositLimitsBufferAppender extends IdentifiableBufferAppender<TransactionReplyCommandBuilder> {
    private final NdcComponentReader<AmountLimit> amountLimitReader;

    public DepositLimitsBufferAppender(NdcComponentReader<AmountLimit> amountLimitReader,
                                       ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
        this.amountLimitReader = ObjectUtils.validateNotNull(amountLimitReader, "amountLimitReader");
    }

    public DepositLimitsBufferAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        this(new AmountLimitReader(), nextAppender);

    }

    public DepositLimitsBufferAppender() {
        this(null);

    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);
        skipReservedField(ndcCharBuffer);

        final DepositLimitsBuffer buffer = readBuffer(ndcCharBuffer);
        stateObject.withDepositLimitsBuffer(buffer);

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private DepositLimitsBuffer readBuffer(NdcCharBuffer ndcCharBuffer) {
        ElementId elementId;
        final ArrayList<AmountLimit> amountLimits = new ArrayList<>();
        final ArrayList<Integer> notesLimits = new ArrayList<>(1);
        do {
            elementId = readElementId(ndcCharBuffer);
            if (elementId == ElementId.AMOUNT_LIMIT) {
                amountLimits.add(amountLimitReader.readComponent(ndcCharBuffer));
            } else {
                notesLimits.add(readNotesLimit(ndcCharBuffer));
            }
        } while (ndcCharBuffer.hasRemaining() && ndcCharBuffer.hasFollowingFieldSeparator());
        amountLimits.trimToSize();
        notesLimits.trimToSize();
        return new DepositLimitsBuffer(Collections.unmodifiableList(amountLimits), Collections.unmodifiableList(notesLimits));
    }

    private void skipReservedField(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkip(10)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(), "Reserved Field",
                        errorMessage, ndcCharBuffer));
    }

    private ElementId readElementId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .flatMap(ElementId::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(), "Element ID",
                        errorMessage, ndcCharBuffer));
    }

    private int readNotesLimit(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(), "Note Limit",
                        errorMessage, ndcCharBuffer));
    }

    @Override
    protected char getBufferId() {
        return DepositLimitsBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return "Deposit Limits Buffer ('d')";
    }

    @Override
    protected String getCommandName() {
        return CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString();
    }
}
