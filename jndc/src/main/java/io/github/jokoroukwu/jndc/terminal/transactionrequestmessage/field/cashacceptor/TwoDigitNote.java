package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.util.Integers;

final class TwoDigitNote extends CashAcceptorNote {

    TwoDigitNote(int noteType, int numberOfNotes) {
        super(noteType, numberOfNotes);
    }

    @Override
    public String toNdcString() {
        return new StringBuilder(4)
                .append(Integers.toEvenLengthHexString(type))
                .append(Integers.toZeroPaddedString(number, 2))
                .toString();
    }
}
