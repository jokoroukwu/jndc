package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.util.Integers;

final class ThreeDigitNote extends CashAcceptorNote {

    ThreeDigitNote(int noteType, int numberOfNotes) {
        super(noteType, numberOfNotes);
    }

    @Override
    public String toNdcString() {
        return new StringBuilder(5)
                .append(Integers.toEvenLengthHexString(type))
                .append(Integers.toZeroPaddedString(number, 3))
                .toString();
    }
}
