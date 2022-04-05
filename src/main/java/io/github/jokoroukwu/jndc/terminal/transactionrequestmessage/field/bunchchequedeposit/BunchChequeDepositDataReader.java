package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.List;

public class BunchChequeDepositDataReader implements NdcComponentReader<BunchChequeDepositData> {
    private final NdcComponentReader<List<CurrencyData>> currencyDataReader;

    public BunchChequeDepositDataReader(NdcComponentReader<List<CurrencyData>> currencyDataReader) {
        this.currencyDataReader = ObjectUtils.validateNotNull(currencyDataReader, "currencyDataReader cannot be null");
    }

    public BunchChequeDepositDataReader() {
        this(new CurrencyDataReader());
    }

    @Override
    public BunchChequeDepositData readComponent(NdcCharBuffer ndcCharBuffer) {
        final int totalChequesToReturn = readTotalChequesToReturn(ndcCharBuffer);
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return new BunchChequeDepositData(totalChequesToReturn);
        }
        skipReservedField(ndcCharBuffer);
        final List<CurrencyData> currencyData = currencyDataReader.readComponent(ndcCharBuffer);
        return new BunchChequeDepositData(totalChequesToReturn, currencyData);
    }

    private int readTotalChequesToReturn(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit buffer: Total Cheques to Return", errorMessage, ndcCharBuffer))
                .get();
    }

    private void skipReservedField(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipNextSubsequence(BunchChequeDepositData.RESERVED_FIELD)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit buffer",
                        "Reserved Field before 'Deposit Currency' field: " + errorMessage,
                        ndcCharBuffer));
    }
}
