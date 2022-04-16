package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public final class Note implements NdcComponent {
    private final int type;
    private final int numberOfNotes;

    public Note(int type, int numberOfNotes) {
        this.type = validateNoteType(type);
        this.numberOfNotes = validateNumberOfNotes(numberOfNotes);
    }

    Note(int type, int numberOfNotes, Void unused) {
        this.type = type;
        this.numberOfNotes = numberOfNotes;
    }


    public int getType() {
        return type;
    }

    public int getNumberOfNotes() {
        return numberOfNotes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Note.class.getSimpleName() + ": {", "}")
                .add("type: " + Integers.toEvenLengthHexString(type))
                .add("numberOfNotes: " + numberOfNotes)
                .toString();
    }

    @Override
    public String toNdcString() {
        return Integers.toZeroPaddedHexString(type, 4)
                + Integers.toZeroPaddedString(numberOfNotes, 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return type == note.type && numberOfNotes == note.numberOfNotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberOfNotes);
    }

    private int validateNoteType(int noteType) {
        if (noteType < 1 || noteType > 0xFF) {
            final String message = "Note type identifier should be within valid range (0x01-0xFF) but was: %#X";
            throw new IllegalArgumentException(String.format(message, noteType));
        }
        return noteType;
    }

    private int validateNumberOfNotes(int numberOfNotes) {
        if (numberOfNotes < 1 || numberOfNotes > 999) {
            final String message = "Number of notes should be within valid range (1-999 dec) but was: " + numberOfNotes;
            throw new IllegalArgumentException(message);
        }
        return numberOfNotes;
    }
}
