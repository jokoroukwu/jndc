package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

public abstract class CashAcceptorNoteReader implements NdcComponentReader<CashAcceptorNote> {

    protected int readNoteType(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadHexInt(2)
                .filter(this::isNoteTypeValid, type -> () -> "should be in range 1-50 dec but was: " + type)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Cash Acceptor Buffer: Note type", errorMessage, ndcCharBuffer))
                .get();
    }

    private boolean isNoteTypeValid(int noteType) {
        return noteType > 0 && noteType <= 0x32;
    }

}
