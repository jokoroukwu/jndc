package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.Currencies;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyDataReader implements NdcComponentReader<List<CurrencyData>> {
    private final NdcComponentReader<List<DepositedCheque>> depositedChequesReader;

    public CurrencyDataReader(NdcComponentReader<List<DepositedCheque>> depositedChequesReader) {
        this.depositedChequesReader = ObjectUtils.validateNotNull(depositedChequesReader, "depositedChequesReader cannot be null");
    }

    public CurrencyDataReader() {
        this(new DepositedChequesReader());
    }

    @Override
    public List<CurrencyData> readComponent(NdcCharBuffer ndcCharBuffer) {
        final CurrencyData first = readCurrencyData(ndcCharBuffer);
        if (!hasMoreCurrencyData(ndcCharBuffer)) {
            return Collections.singletonList(first);
        }
        final ArrayList<CurrencyData> currencyData = new ArrayList<>();
        currencyData.add(first);
        do {
            skipGroupSeparator(ndcCharBuffer);
            currencyData.add(readCurrencyData(ndcCharBuffer));
        } while (hasMoreCurrencyData(ndcCharBuffer));

        currencyData.trimToSize();
        return Collections.unmodifiableList(currencyData);
    }

    private boolean hasMoreCurrencyData(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.hasRemaining() && ndcCharBuffer.getCharAt(0) != NdcConstants.FIELD_SEPARATOR;
    }

    private CurrencyData readCurrencyData(NdcCharBuffer ndcCharBuffer) {
        final String currency = readDepositCurrency(ndcCharBuffer);
        final char exponentSign = readAmountExponentSign(ndcCharBuffer);
        final int exponentValue = readAmountExponentValue(ndcCharBuffer);
        final long totalCustomerAmount = readCustomerAmount(ndcCharBuffer);
        final long totalDerivedAmount = readDerivedAmount(ndcCharBuffer);
        skipReservedField(ndcCharBuffer);
        final List<DepositedCheque> depositedCheques = depositedChequesReader.readComponent(ndcCharBuffer);

        return new CurrencyData(currency, exponentSign, exponentValue, totalCustomerAmount, depositedCheques, totalDerivedAmount);
    }

    private void skipGroupSeparator(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoGroupSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Deposit Currency", errorMessage, ndcCharBuffer));
    }

    private long readDerivedAmount(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadLong(12)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Total Derived Amount", errorMessage, ndcCharBuffer))
                .get();
    }

    private void skipReservedField(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipNextSubsequence(CurrencyData.RESERVED_FIELD)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer",
                        "Reserved Field before 'Cheque Identifier' field: " + errorMessage,
                        ndcCharBuffer));
    }

    private long readCustomerAmount(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadLong(12)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Total Customer Amount", errorMessage, ndcCharBuffer))
                .get();
    }

    private int readAmountExponentValue(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Amount Exponent Value", errorMessage, ndcCharBuffer))
                .get();
    }

    private String readDepositCurrency(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .filter(Currencies::isValidIso4217CurrencyCode, code -> () -> code + " is not a valid ISO-4217 currency code")
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Deposit Currency", errorMessage, ndcCharBuffer))
                .get();
    }

    private char readAmountExponentSign(NdcCharBuffer ndcCharBuffer) {
        return (char) ndcCharBuffer.tryReadNextChar()
                .filter(this::isExponentSignValid, sign -> () -> "expected sign to be '-' or '+' but was: " + sign)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Amount Exponent Sign", errorMessage, ndcCharBuffer))
                .get();
    }

    private boolean isExponentSignValid(int exponentSign) {
        return '+' == exponentSign || '-' == exponentSign;
    }

}
