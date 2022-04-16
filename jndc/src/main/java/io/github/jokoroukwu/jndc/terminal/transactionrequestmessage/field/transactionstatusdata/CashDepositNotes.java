package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class CashDepositNotes implements NdcComponent {
    private final int notesRefunded;
    private final int notesRejected;
    private final int notesEncashed;
    private final int notesEscrowed;

    public CashDepositNotes(int notesRefunded, int notesRejected, int notesEncashed, int notesEscrowed) {
        this.notesRefunded = Integers.validateRange(notesRefunded, 0, 99999, "Number of Notes Refunded");
        this.notesRejected = Integers.validateRange(notesRejected, 0, 99999, "Number of Notes Rejected");
        this.notesEncashed = Integers.validateRange(notesEncashed, 0, 99999, "Number of Notes Encashed");
        this.notesEscrowed = Integers.validateRange(notesEscrowed, 0, 99999, "Number of Notes Escrowed");
    }

    CashDepositNotes(int notesRefunded, int notesRejected, int notesEncashed, int notesEscrowed, Void unused) {
        this.notesRefunded = notesRefunded;
        this.notesRejected = notesRejected;
        this.notesEncashed = notesEncashed;
        this.notesEscrowed = notesEscrowed;
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
        return new StringJoiner(", ", CashDepositNotes.class.getSimpleName() + ": {", "}")
                .add("notesRefunded: " + notesRefunded)
                .add("notesRejected: " + notesRejected)
                .add("notesEncashed: " + notesEncashed)
                .add("notesEscrowed: " + notesEscrowed)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashDepositNotes)) return false;
        CashDepositNotes that = (CashDepositNotes) o;
        return notesRefunded == that.notesRefunded &&
                notesRejected == that.notesRejected &&
                notesEncashed == that.notesEncashed &&
                notesEscrowed == that.notesEscrowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notesRefunded, notesRejected, notesEncashed, notesEscrowed);
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(20)
                .appendZeroPadded(notesRefunded, 5)
                .appendZeroPadded(notesRejected, 5)
                .appendZeroPadded(notesEncashed, 5)
                .appendZeroPadded(notesEscrowed, 5)
                .toString();
    }
}
