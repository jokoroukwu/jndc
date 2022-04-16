package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class CashAcceptorNote implements NdcComponent {
    protected final int type;
    protected final int number;

    protected CashAcceptorNote(int type, int number) {
        this.type = type;
        this.number = number;
    }

    /**
     * A two‐digit number (01 ‐ 90) note.
     * The note should be used when
     * option 45 is not set to report more than 90 notes.
     *
     * @param type          two‐digit hexadecimal number (01 ‐ 32) representing a note type
     * @param numberOfNotes number of notes in the escrow, or vaulted if using direct deposit to
     *                      cassettes
     * @return new {@link CashAcceptorNote} instance
     */
    public static CashAcceptorNote twoDigit(int type, int numberOfNotes) {
        validateNoteType(type);
        validateNumberOfNotes(numberOfNotes, 90);
        return new TwoDigitNote(type, numberOfNotes);
    }

    /**
     * A three‐digit number (01 ‐ 999) note.
     * The note should be used when
     * option 45 is set to report more than 90 notes.
     *
     * @param type          two‐digit hexadecimal number (01 ‐ 32) representing a note type
     * @param numberOfNotes number of notes in the escrow, or vaulted if using direct deposit to
     *                      cassettes
     * @return new {@link CashAcceptorNote} instance
     */
    public static CashAcceptorNote threeDigit(int type, int numberOfNotes) {
        validateNoteType(type);
        validateNumberOfNotes(numberOfNotes, 999);
        return new ThreeDigitNote(type, numberOfNotes);
    }

    private static void validateNoteType(int noteType) {
        if (noteType < 0 || noteType > 0x32) {
            throw new IllegalArgumentException("Note Type value should be within valid range (0-50 dec) but was: " + noteType);
        }
    }

    private static void validateNumberOfNotes(int numberOfNotes, int maxValue) {
        if (numberOfNotes < 0 || numberOfNotes > maxValue) {
            final String message = String.format("Number of notes should be within valid range (0-%d dec) but was: %d",
                    maxValue, numberOfNotes);
            throw new IllegalArgumentException(message);
        }
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CashAcceptorNote.class.getSimpleName() + ": {", "}")
                .add("noteType: " + Integers.toEvenLengthHexString(type))
                .add("numberOfNotes: " + number)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashAcceptorNote)) return false;
        CashAcceptorNote note = (CashAcceptorNote) o;
        return type == note.type && number == note.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

}
