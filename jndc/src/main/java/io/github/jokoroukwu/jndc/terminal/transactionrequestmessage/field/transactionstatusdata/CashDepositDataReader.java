package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import java.util.List;
import java.util.Optional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class CashDepositDataReader implements NdcComponentReader<Optional<CashDepositData>> {
    private final NdcComponentReader<List<RecycleCassette>> recycleCassettesReader;
    private final NdcComponentReader<CashDepositNotes> cashDepositNotesReader;

    public CashDepositDataReader(NdcComponentReader<List<RecycleCassette>> recycleCassettesReader,
                                 NdcComponentReader<CashDepositNotes> cashDepositNotesReader) {
        this.recycleCassettesReader = ObjectUtils.validateNotNull(recycleCassettesReader,
                "Recycle cassettes reader cannot be null");
        this.cashDepositNotesReader = ObjectUtils.validateNotNull(cashDepositNotesReader,
                "Cash Deposit Notes reader cannot be null");
    }

    public CashDepositDataReader() {
        this(new RecycledCassettesReader(), CashDepositNotesReader.INSTANCE);
    }

    @Override
    public Optional<CashDepositData> readComponent(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Optional.empty();
        }
        //  at least Last Cash Deposit Transaction Direction should be present
        final DepositTransactionDirection depositTransactionDirection = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(DepositTransactionDirection::forValue)
                .ifEmpty(errorMessage -> onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Last Cash Deposit Transaction Direction", errorMessage, ndcCharBuffer))
                .get();

        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Optional.of(new CashDepositData(depositTransactionDirection));
        }
        //  should have note data
        final CashDepositNotes cashDepositNotes = Result.of(() -> cashDepositNotesReader.readComponent(ndcCharBuffer))
                        .getOrThrow(exception -> withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                                "Last Transaction Status Data", exception.getMessage(), ndcCharBuffer));

        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Optional.of(new CashDepositData(depositTransactionDirection, cashDepositNotes, null, null));
        }
        // should have recycle cassette data
        final List<RecycleCassette> recycleCassettes = recycleCassettesReader.readComponent(ndcCharBuffer);
        return Optional.of(new CashDepositData(depositTransactionDirection, cashDepositNotes, recycleCassettes, null));
    }
}
