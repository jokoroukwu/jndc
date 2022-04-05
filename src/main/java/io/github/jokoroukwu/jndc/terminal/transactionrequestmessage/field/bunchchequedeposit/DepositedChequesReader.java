package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepositedChequesReader implements NdcComponentReader<List<DepositedCheque>> {

    @Override
    public List<DepositedCheque> readComponent(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<DepositedCheque> depositedCheques = new ArrayList<>();
        do {
            final int chequeId = readChequeId(ndcCharBuffer);
            final long customerChequeAmount = readCustomerChequeAmount(ndcCharBuffer);
            final long derivedChequeAmount = readDerivedChequeAmount(ndcCharBuffer);
            final int codeLineLength = readCodeLineLength(ndcCharBuffer);
            final String codeLineData = readCodeLineData(codeLineLength, ndcCharBuffer);
            skipTrailingGroupSeparator(ndcCharBuffer);
            depositedCheques.add(new DepositedCheque(chequeId, customerChequeAmount, derivedChequeAmount, codeLineData));
        } while (hasMoreCheques(ndcCharBuffer));

        depositedCheques.trimToSize();
        return Collections.unmodifiableList(depositedCheques);
    }

    private String readCodeLineData(int length, NdcCharBuffer ndcCharBuffer) {
        if (length == 0) {
            return Strings.EMPTY_STRING;
        }
        return ndcCharBuffer.tryReadCharSequence(length)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Codeline Length", errorMessage, ndcCharBuffer))
                .get();
    }

    private int readCodeLineLength(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Codeline Length", errorMessage, ndcCharBuffer))
                .get();
    }

    private long readDerivedChequeAmount(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadLong(12)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Derived Cheque Amount", errorMessage, ndcCharBuffer))
                .get();
    }

    private long readCustomerChequeAmount(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadLong(12)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Customer Cheque Amount", errorMessage, ndcCharBuffer))
                .get();
    }

    private int readChequeId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .filter(id -> id > 0, id -> () -> "should be in range 1-999 dec but was: " + id)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer: Cheque Identifier", errorMessage, ndcCharBuffer))
                .get();
    }

    private void skipTrailingGroupSeparator(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Bunch Cheque Deposit Buffer",
                        "missing trailing group separator after cheque data",
                        ndcCharBuffer));
    }

    private boolean hasMoreCheques(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.hasRemaining() && ndcCharBuffer.getCharAt(0) != NdcConstants.GROUP_SEPARATOR;
    }
}
