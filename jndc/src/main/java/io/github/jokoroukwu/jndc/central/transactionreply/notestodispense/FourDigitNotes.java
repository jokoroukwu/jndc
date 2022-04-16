package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import io.github.jokoroukwu.jndc.util.Integers;

import java.util.ArrayList;

class FourDigitNotes extends NotesToDispense {

    FourDigitNotes(ArrayList<Integer> listDelegate) {
        super(listDelegate);
    }

    FourDigitNotes() {
    }


    FourDigitNotes(int initCapacity) {
        super(initCapacity);
    }

    @Override
    protected void performElementChecks(Integer element) {
        Integers.validateRange(element, 0, 9999, "Number of notes");
    }

    @Override
    protected int getNumberOfDigits() {
        return 4;
    }
}
