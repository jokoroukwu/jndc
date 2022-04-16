package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.Currencies;
import io.github.jokoroukwu.jndc.util.Doubles;

public class AmountLimitReader implements NdcComponentReader<AmountLimit> {
    @Override
    public AmountLimit readComponent(NdcCharBuffer ndcCharBuffer) {
        return readAmountLimit(ndcCharBuffer);
    }

    private AmountLimit readAmountLimit(NdcCharBuffer ndcCharBuffer) {
        final String currencyCode = readCurrencyCode(ndcCharBuffer);
        final int limitValue = readLimitValue(ndcCharBuffer);
        return new AmountLimit(currencyCode, limitValue, null);
    }

    private void checkHasLimitData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            throw NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                    "Deposit Limits Buffer ('d'): Deposit Amount Limit",
                    String.format("expected limit data at position %d but none remaining", ndcCharBuffer.position()), ndcCharBuffer);
        }
    }

    private int readLimitValue(NdcCharBuffer ndcCharBuffer) {
        checkHasLimitData(ndcCharBuffer);
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());
        return (int) Doubles.tryParseDouble(builder.toString())
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "Deposit Limits Buffer ('d'): Deposit Amount Limit value", errorMessage, ndcCharBuffer));
    }

    private String readCurrencyCode(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(3)
                .filter(Currencies::isValidIso4217CurrencyCode, code -> () -> code + " is not a valid ISO-4217 currency code")
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "Deposit Limits Buffer ('d'): Deposit Amount Limit currency code", errorMessage, ndcCharBuffer));
    }
}
