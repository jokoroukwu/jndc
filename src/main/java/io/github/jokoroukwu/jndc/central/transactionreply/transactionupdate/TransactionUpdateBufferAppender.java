package io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.CurrencyTableEntry;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.TransactionTableEntry;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.CompositeTlvReader;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTLVReader;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class TransactionUpdateBufferAppender extends IdentifiableBufferAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Transaction Update Buffer";
    private final CompositeTlvReader<String> compositeTlvReader;

    public TransactionUpdateBufferAppender(CompositeTlvReader<String> compositeTlvReader, ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
        this.compositeTlvReader = ObjectUtils.validateNotNull(compositeTlvReader, "compositeTlvReader");
    }

    public TransactionUpdateBufferAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        this(new CompositeTlvReader<>(HexStringBerTLVReader.DEFAULT, BerTlvReaderBase.INSTANCE, 0x77), nextAppender);
    }

    public TransactionUpdateBufferAppender() {
        this(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);
        final CurrencyTableEntry currencyTableEntry = readCurrencyTableEntry(ndcCharBuffer);
        final TransactionTableEntry transactionTableEntry = readTransactionTableEntry(ndcCharBuffer);
        final TargetBufferId targetBufferId = readTargetBufferId(ndcCharBuffer);
        final int bufferLength = readBufferLength(targetBufferId, ndcCharBuffer);
        final String bufferData = readBufferData(bufferLength, ndcCharBuffer);
        checkNoUnexpectedDataLeft(ndcCharBuffer);

        final TransactionUpdateBuffer transactionUpdateBuffer =
                new TransactionUpdateBuffer(targetBufferId, bufferData, currencyTableEntry, transactionTableEntry);
        stateObject.withTransactionUpdateBuffer(transactionUpdateBuffer);

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private TargetBufferId readTargetBufferId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(TargetBufferId::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "'Transaction Update Buffer':Target Buffer Identifier:", errorMessage, ndcCharBuffer));
    }

    private TransactionTableEntry readTransactionTableEntry(NdcCharBuffer ndcCharBuffer) {
        final int entryType = ndcCharBuffer.tryReadHexInt(2)
                .getOrThrow(errorMessage -> NdcMessageParseException.withComposedMessage(getCommandName(),
                        "'Transaction Update Buffer': ICC Transaction Table entry type",
                        errorMessage));
        if (entryType > 0) {
            final CompositeTlv<String> dataObjects = compositeTlvReader.readComponent(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                            "'Transaction Update Buffer': ICC Transaction Table entry data objects",
                            errorMessage, ndcCharBuffer));
            return new TransactionTableEntry(entryType, dataObjects);
        }
        return null;
    }

    private CurrencyTableEntry readCurrencyTableEntry(NdcCharBuffer ndcCharBuffer) {
        final int entryType = ndcCharBuffer.tryReadHexInt(2)
                .getOrThrow(errorMessage -> NdcMessageParseException.withComposedMessage(getCommandName(),
                        "'Transaction Update Buffer': ICC Currency Table entry type",
                        errorMessage));
        if (entryType > 0) {
            final CompositeTlv<String> dataObjects = compositeTlvReader.readComponent(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                            "'Transaction Update Buffer': ICC Currency Table entry data objects",
                            errorMessage, ndcCharBuffer));
            return new CurrencyTableEntry(entryType, dataObjects);
        }
        return null;
    }

    private int readBufferLength(TargetBufferId targetBufferId, NdcCharBuffer ndcCharBuffer) {
        switch (targetBufferId) {
            case BUFFER_B:
            case BUFFER_C: {
                return ndcCharBuffer.tryReadHexInt(2)
                        .filter(length -> length == 32, length -> ()
                                -> String.format("length should be 32 characters when 'Target Buffer Identifier is %s but was %d",
                                targetBufferId, length))
                        .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                                "'Transaction Update Buffer': Buffer Length", errorMessage, ndcCharBuffer));
            }
            case AMOUNT_BUFFER: {
                return ndcCharBuffer.tryReadHexInt(2)
                        .filter(this::isValidAmountBufferLength, length -> ()
                                -> String.format("length should be 8 or 2 characters when 'Target Buffer Identifier is %s but was %d",
                                targetBufferId, length))
                        .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                                "'Transaction Update Buffer': Buffer Length", errorMessage, ndcCharBuffer));
            }
            default: {
                return 0;
            }
        }
    }

    private String readBufferData(int length, NdcCharBuffer ndcCharBuffer) {
        if (length == 0) {
            return Strings.EMPTY_STRING;
        }
        return ndcCharBuffer.tryReadCharSequence(length)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(getCommandName(),
                        "'Transaction Update Buffer':  Buffer Data", errorMessage, ndcCharBuffer));
    }

    private boolean isValidAmountBufferLength(int length) {
        return length == 8 || length == 12;
    }

    private void checkNoUnexpectedDataLeft(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasNextCharMatching(NdcConstants.FIELD_SEPARATOR)) {
            throw NdcMessageParseException.withMessage(getCommandName(), "'Transaction Update Buffer'",
                    "unexpected data at position " + ndcCharBuffer.position(), ndcCharBuffer);
        }
    }


    @Override
    protected char getBufferId() {
        return TransactionUpdateBuffer.ID;
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
