package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class BnaCounters implements NdcComponent {
    private final int notesRefunded;
    private final int notesRejected;
    private final int notesEncashed;
    private final int notesEscrowed;

    public BnaCounters(int notesRefunded, int notesRejected, int notesEncashed, int notesEscrowed) {
        this.notesRefunded = validateValue(notesRefunded, "'Notes Refunded'");
        this.notesRejected = validateValue(notesRejected, "'Notes Rejected'");
        this.notesEncashed = validateValue(notesEncashed, "'Notes Encashed'");
        this.notesEscrowed = validateValue(notesEscrowed, "'Notes Escrowed'");
    }

    BnaCounters(int notesRefunded, int notesRejected, int notesEncashed, int notesEscrowed, Void unused) {
        this.notesRefunded = notesRefunded;
        this.notesRejected = notesRejected;
        this.notesEncashed = notesEncashed;
        this.notesEscrowed = notesEscrowed;
    }

    private int validateValue(int value, String fieldName) {
        return Integers.validateRange(value, 0, 99999, fieldName);
    }

    public int getNotesRefunded() {
        return notesRefunded;
    }

    public int getNotesRejected() {
        return notesRejected;
    }

    public int getNotesEncashed() {
        return notesEncashed;
    }

    public int getNotesEscrowed() {
        return notesEscrowed;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BnaCounters.class.getSimpleName() + ": {", "}")
                .add("notesRefunded: " + notesRefunded)
                .add("notesRejected: " + notesRejected)
                .add("notesEncashed: " + notesEncashed)
                .add("notesEscrowed: " + notesEscrowed)
                .toString();
    }

    @Override
    public String toNdcString() {
        //  exact capacity can be evaluated
        return new NdcStringBuilder(20)
                .appendZeroPadded(notesRefunded, 5)
                .appendZeroPadded(notesRejected, 5)
                .appendZeroPadded(notesEncashed, 5)
                .appendZeroPadded(notesEscrowed, 5)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BnaCounters that = (BnaCounters) o;
        return notesRefunded == that.notesRefunded &&
                notesRejected == that.notesRejected &&
                notesEncashed == that.notesEncashed &&
                notesEscrowed == that.notesEscrowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notesRefunded, notesRejected, notesEncashed, notesEscrowed);
    }

}
