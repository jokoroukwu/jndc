package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

public final class TwoDigitNoteReader extends CashAcceptorNoteReader {
    public static final TwoDigitNoteReader INSTANCE = new TwoDigitNoteReader();

    private TwoDigitNoteReader() {
    }

    @Override
    public CashAcceptorNote readComponent(NdcCharBuffer ndcCharBuffer) {
        final int noteType = readNoteType(ndcCharBuffer);
        final int numberOfNotes = ndcCharBuffer.tryReadInt(2)
                .filter(this::isNumberOfNotesValid, number -> () -> "value should be in range 01-90 dec but was " + number)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "Cash Acceptor Buffer: Number of notes", errorMessage, ndcCharBuffer))
                .get();
        return new TwoDigitNote(noteType, numberOfNotes);
    }

    private boolean isNumberOfNotesValid(int value) {
        return value > 0 && value <= 90;
    }
}
