package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class NdcCassetteCounts implements NdcComponent {
    private final int cassetteType;
    private final int totalNotesInCassette;
    private final int numberOfNoteTypesReported;
    private final List<NdcNote> notes;

    NdcCassetteCounts(int cassetteType, int totalNotesInCassette, int numberOfNoteTypesReported, List<NdcNote> notes) {
        this.cassetteType = cassetteType;
        this.totalNotesInCassette = totalNotesInCassette;
        this.numberOfNoteTypesReported = numberOfNoteTypesReported;
        this.notes = notes;
    }

    public NdcCassetteCounts(int cassetteType, Collection<NdcNote> notes) {
        this.cassetteType = Integers.validateRange(cassetteType, 1, 999, "'NDC Cassette Type'");
        this.notes = List.copyOf(notes);
        this.numberOfNoteTypesReported = Integers.validateRange(notes.size(), 0, 999,
                "'Number of Note Types Reported'");
        this.totalNotesInCassette = notes.stream()
                .mapToInt(NdcNote::getNumberOfNotes)
                .sum();
        Integers.validateMaxValue(totalNotesInCassette, 99999, "'Total Notes In Cassette'");
    }

    public int getCassetteType() {
        return cassetteType;
    }

    public int getTotalNotesInCassette() {
        return totalNotesInCassette;
    }

    public int getNumberOfNoteTypesReported() {
        return numberOfNoteTypesReported;
    }

    public List<NdcNote> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NdcCassetteCounts.class.getSimpleName() + ": {", "}")
                .add("cassetteType: " + cassetteType)
                .add("totalNotesInCassette: " + totalNotesInCassette)
                .add("numberOfNoteTypesReported: " + numberOfNoteTypesReported)
                .add("notes: " + notes)
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(11 + 9 * notes.size())
                .appendZeroPadded(cassetteType, 3)
                .appendZeroPadded(totalNotesInCassette, 5)
                .appendZeroPadded(numberOfNoteTypesReported, 3)
                .appendComponents(notes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdcCassetteCounts that = (NdcCassetteCounts) o;
        return cassetteType == that.cassetteType &&
                totalNotesInCassette == that.totalNotesInCassette &&
                numberOfNoteTypesReported == that.numberOfNoteTypesReported &&
                notes.equals(that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassetteType, totalNotesInCassette, numberOfNoteTypesReported, notes);
    }
}
