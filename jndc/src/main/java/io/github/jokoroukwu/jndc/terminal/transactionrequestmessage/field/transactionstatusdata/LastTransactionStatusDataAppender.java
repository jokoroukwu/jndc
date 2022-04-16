package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataReader;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender.TransactionRqDelegatingAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import java.util.List;
import java.util.Optional;

public class LastTransactionStatusDataAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Transaction Status Data";

    private final ConfigurableNdcComponentReader<List<Integer>> notesDispensedReader;
    private final NdcComponentReader<List<Integer>> coinsDispensedReader;
    private final NdcComponentReader<Optional<CashDepositData>> cashDepositDataReader;
    private final NdcComponentReader<Optional<CompletionData>> completionDataReader;

    public LastTransactionStatusDataAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                             ConfigurableNdcComponentReader<List<Integer>> notesDispensedReader,
                                             NdcComponentReader<List<Integer>> coinsDispensedReader,
                                             NdcComponentReader<Optional<CashDepositData>> cashDepositDataReader,
                                             NdcComponentReader<Optional<CompletionData>> completionDataReader) {
        super(nextAppender);
        this.notesDispensedReader = ObjectUtils.validateNotNull(notesDispensedReader, "notesDispensedReader");
        this.coinsDispensedReader = ObjectUtils.validateNotNull(coinsDispensedReader, "coinsDispensedReader");
        this.cashDepositDataReader = ObjectUtils.validateNotNull(cashDepositDataReader, "cashDepositDataReader");
        this.completionDataReader = ObjectUtils.validateNotNull(completionDataReader, "completionDataReader");
    }

    public LastTransactionStatusDataAppender() {
        this(new TransactionRqDelegatingAppender(),
                new NotesDispensedDataReader(),
                new CoinsDispensedDataReader(),
                new CashDepositDataReader(),
                CompletionDataReader.DEFAULT);
    }

    public LastTransactionStatusDataAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        this(nextAppender,
                new NotesDispensedDataReader(),
                new CoinsDispensedDataReader(),
                new CashDepositDataReader(),
                CompletionDataReader.DEFAULT);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer));

        if (ndcCharBuffer.hasFollowingFieldSeparator()) {
            //  field is empty but subsequent fields are present
            callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
        } else {
            final LastTransactionStatusDataBase lastTransactionStatusData = readData(ndcCharBuffer, deviceConfiguration);
            stateObject.withLastTransactionStatusData(lastTransactionStatusData);
            callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
        }
    }

    private LastTransactionStatusDataBase readData(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.tryReadNextCharMatching(LastTransactionStatusDataBase.ID)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        FIELD_NAME + " ID", errorMessage, ndcCharBuffer));

        final LastTransactionStatusDataBuilder lastTransactionStatusDataBuilder = new LastTransactionStatusDataBuilder()
                .withLastTransactionSerialNumber(readLastTransactionSerialNumber(ndcCharBuffer))
                .withLastStatusIssued(readLastStatusIssued(ndcCharBuffer))
                .withNotesDispensed(notesDispensedReader.readComponent(ndcCharBuffer, deviceConfiguration))
                .withCoinsDispensed(coinsDispensedReader.readComponent(ndcCharBuffer));


        cashDepositDataReader.readComponent(ndcCharBuffer)
                .ifPresent(lastTransactionStatusDataBuilder::withCashDepositData);

        Result.of(() -> completionDataReader.readComponent(ndcCharBuffer))
                .getOrThrow(ex -> new NdcMessageParseException(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString() + ": " + ex.getMessage()))
                .ifPresent(lastTransactionStatusDataBuilder::withCompletionData);

        return lastTransactionStatusDataBuilder.build();
    }

    private LastStatusIssued readLastStatusIssued(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(LastStatusIssued::forValue)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        FIELD_NAME + ": Last Status Issued", errorMessage, ndcCharBuffer))
                .get();
    }

    private int readLastTransactionSerialNumber(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(4)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        FIELD_NAME + ": Last Transaction Serial Number", errorMessage, ndcCharBuffer))
                .get();
    }

}
