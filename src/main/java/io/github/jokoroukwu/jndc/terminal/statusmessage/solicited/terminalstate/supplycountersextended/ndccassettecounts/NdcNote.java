package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class NdcNote implements NdcComponent {
    private final int noteTypeId;
    private final int numberOfNotes;

    public NdcNote(int noteTypeId, int numberOfNotes) {
        this.noteTypeId = Integers.validateHexRange(noteTypeId, 0x01, 0xFFFF, "'Note Type Identifier'");
        this.numberOfNotes = Integers.validateRange(numberOfNotes, 0, 99999, "'Number of notes'");
    }

    NdcNote(int noteTypeId, int numberOfNotes, Void unused) {
        this.noteTypeId = noteTypeId;
        this.numberOfNotes = numberOfNotes;
    }

    public int getNoteTypeId() {
        return noteTypeId;
    }

    public int getNumberOfNotes() {
        return numberOfNotes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NdcNote.class.getSimpleName() + ": {", "}")
                .add("noteTypeId: " + Integers.toZeroPaddedHexString(noteTypeId, 4))
                .add("numberOfNotes: " + numberOfNotes)
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(9)
                .appendZeroPaddedHex(noteTypeId, 4)
                .appendZeroPadded(numberOfNotes, 5)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdcNote ndcNote = (NdcNote) o;
        return noteTypeId == ndcNote.noteTypeId && numberOfNotes == ndcNote.numberOfNotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteTypeId, numberOfNotes);
    }
}
