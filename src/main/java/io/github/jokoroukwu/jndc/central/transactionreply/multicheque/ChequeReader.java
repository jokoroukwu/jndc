package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChequeReader implements NdcComponentReader<List<Cheque>> {
    @Override
    public List<Cheque> readComponent(NdcCharBuffer ndcCharBuffer) {
        Cheque nextCheque = readCheque(ndcCharBuffer);
        if (!hasMoreCheques(ndcCharBuffer)) {
            return List.of(nextCheque);
        }
        final ArrayList<Cheque> cheques = new ArrayList<>();
        cheques.add(nextCheque);
        do {
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> NdcMessageParseException.onNoGroupSeparator(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                            "'Process Multiple Cheques Buffer': Cheque Data", errorMessage, ndcCharBuffer));
            nextCheque = readCheque(ndcCharBuffer);
            cheques.add(nextCheque);
        } while (hasMoreCheques(ndcCharBuffer));

        cheques.trimToSize();
        return Collections.unmodifiableList(cheques);
    }

    private Cheque readCheque(NdcCharBuffer ndcCharBuffer) {
        final int chequeId = readChequeId(ndcCharBuffer);
        final int chequeDestination = readChequeDestination(ndcCharBuffer);
        final ChequeStamp chequeStamp = readChequeStamp(ndcCharBuffer);
        skipReservedValue(ndcCharBuffer);
        final String chequeText = readChequeEndorseText(ndcCharBuffer);

        return new Cheque(chequeId, chequeDestination, chequeStamp, chequeText, null);
    }


    private String readChequeEndorseText(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());
        return builder.toString();
    }

    private void skipReservedValue(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipNextSubsequence("0000")
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Process Multiple Cheques Buffer': Reserved field", errorMessage, ndcCharBuffer));
    }

    private ChequeStamp readChequeStamp(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ChequeStamp::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Process Multiple Cheques Buffer': Cheque Stamp", errorMessage, ndcCharBuffer));
    }

    private int readChequeId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(3)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Process Multiple Cheques Buffer': Cheque Identifier", errorMessage, ndcCharBuffer));
    }

    private int readChequeDestination(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadInt(2)
                .filter(this::isDestinationValid, value -> () -> "value should be in range 0-15 dec but was " + value)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(CentralMessageClass.TRANSACTION_REPLY_COMMAND.toString(),
                        "'Process Multiple Cheques Buffer': Cheque Destination", errorMessage, ndcCharBuffer));
    }

    private boolean isDestinationValid(int chequeDestination) {
        return chequeDestination <= 15;
    }

    private boolean hasMoreCheques(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.hasRemaining() && ndcCharBuffer.getCharAt(0) != NdcConstants.FIELD_SEPARATOR;
    }

}
