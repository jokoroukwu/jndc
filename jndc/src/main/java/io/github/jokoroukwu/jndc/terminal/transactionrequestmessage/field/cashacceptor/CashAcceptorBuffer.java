package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents device ID ‘w’ field and the following two
 * fields containing cash acceptor data.
 * The field is optional.
 */
public final class CashAcceptorBuffer implements IdentifiableBuffer {
    public static final char ID = 'w';
    public static final CashAcceptorBuffer EMPTY = new CashAcceptorBuffer(List.of());
    private final List<CashAcceptorNote> notes;

    CashAcceptorBuffer(List<CashAcceptorNote> notes) {
        this.notes = notes;
    }

    public CashAcceptorBuffer(Collection<CashAcceptorNote> notes) {
        this.notes = List.copyOf(notes);
    }

    public List<CashAcceptorNote> getNotes() {
        return notes;
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder(5 * notes.size() + 1)
                .append(ID);

        for (CashAcceptorNote note : notes) {
            builder.append(note.toNdcString());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CashAcceptorBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("notes: " + notes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashAcceptorBuffer that = (CashAcceptorBuffer) o;
        return notes.equals(that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, notes);
    }
}
