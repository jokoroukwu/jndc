package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

public class ThreeDigitNoteReader extends CashAcceptorNoteReader {
    public static final ThreeDigitNoteReader INSTANCE = new ThreeDigitNoteReader();

    private ThreeDigitNoteReader() {
    }

    @Override
    public CashAcceptorNote readComponent(NdcCharBuffer ndcCharBuffer) {
        final int noteType = readNoteType(ndcCharBuffer);
        final int numberOfNotes = ndcCharBuffer.tryReadInt(3)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Cash Acceptor Buffer: Number of notes", errorMessage, ndcCharBuffer))
                .get();

        return new ThreeDigitNote(noteType, numberOfNotes);
    }
}
