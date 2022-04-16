package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.*;

public final class NotesDataBuffer implements IdentifiableBuffer {
    public static final char SUSPECT_NOTES_DATA_ID = 'c';
    public static final NotesDataBuffer SUSPECT_EMPTY = suspectNotes(null);
    public static final char COUNTERFEIT_NOTES_DATA_ID = 'd';
    public static final NotesDataBuffer COUNTERFEIT_EMPTY = counterfeitNotes(null);
    private final char id;
    private final List<Note> notes;

    NotesDataBuffer(char id, List<Note> notes) {
        this.notes = notes;
        this.id = id;
    }

    public static NotesDataBuffer suspectNotes(Collection<Note> notes) {
        return new NotesDataBuffer(SUSPECT_NOTES_DATA_ID, validateAndCopy(notes));
    }

    public static NotesDataBuffer counterfeitNotes(Collection<Note> notes) {
        return new NotesDataBuffer(COUNTERFEIT_NOTES_DATA_ID, validateAndCopy(notes));
    }

    private static List<Note> validateAndCopy(Collection<Note> notes) {
        if (notes == null) {
            return null;
        }
        if (notes.size() > 99) {
            final String message = "Number of note types should be within valid range (0-99 dec) but was: " + notes.size();
            throw new IllegalArgumentException(message);
        }
        return List.copyOf(notes);
    }

    public Optional<List<Note>> getNotes() {
        return Optional.ofNullable(notes);
    }

    public int getNumberOfNoteTypes() {
        return notes.size();
    }

    @Override
    public char getId() {
        return id;
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder((7 * notes.size()) + 3)
                .append(id)
                .append(Integers.toZeroPaddedString(notes.size(), 2));
        for (Note note : notes) {
            builder.append(note.toNdcString());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NotesDataBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + id + "'")
                .add("notes: " + notes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotesDataBuffer notesDataBuffer = (NotesDataBuffer) o;
        return id == notesDataBuffer.id && Objects.equals(notes, notesDataBuffer.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notes);
    }
}
