package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public enum CashDepositNotesReader implements NdcComponentReader<CashDepositNotes> {

    INSTANCE;

    @Override
    public CashDepositNotes readComponent(NdcCharBuffer ndcCharBuffer) {
        final int notesRefunded = ndcCharBuffer.tryReadInt(5)
                .mapDescription("Number of Notes Refunded: "::concat)
                .getOrThrow(NdcMessageParseException::new);

        final int notesRejected = ndcCharBuffer.tryReadInt(5)
                .mapDescription("Number of Notes Rejected: "::concat)
                .getOrThrow(NdcMessageParseException::new);

        final int notesEncashed = ndcCharBuffer.tryReadInt(5)
                .mapDescription("Number of Notes Encashed: "::concat)
                .getOrThrow(NdcMessageParseException::new);

        final int notesEscrowed = ndcCharBuffer.tryReadInt(5)
                .mapDescription("Number of Notes Escrowed: "::concat)
                .getOrThrow(NdcMessageParseException::new);

        return new CashDepositNotes(notesRefunded, notesRejected, notesEncashed, notesEscrowed, null);
    }
}
