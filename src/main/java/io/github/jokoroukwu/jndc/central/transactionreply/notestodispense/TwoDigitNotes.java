package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import io.github.jokoroukwu.jndc.util.Integers;

import java.util.ArrayList;

class TwoDigitNotes extends NotesToDispense {

    TwoDigitNotes(ArrayList<Integer> listDelegate) {
        super(listDelegate);
    }

    TwoDigitNotes(int initCapacity) {
        super(initCapacity);
    }

    TwoDigitNotes() {
    }

    @Override
    protected int getNumberOfDigits() {
        return 2;
    }

    @Override
    protected void performElementChecks(Integer element) {
        Integers.validateRange(element, 0, 99, "Number of notes");
    }
}
