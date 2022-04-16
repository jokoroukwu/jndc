package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public class RecycleCassette implements NdcComponent {
    private final int type;
    private final int numberOfNotes;

    RecycleCassette(int type, int numberOfNotes, Void unused) {
        this.type = type;
        this.numberOfNotes = numberOfNotes;
    }

    public RecycleCassette(int type, int numberOfNotes) {
        validateFields(type, numberOfNotes);
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
    public String toNdcString() {
        return Integers.toZeroPaddedString(type, 3) + Integers.toZeroPaddedString(numberOfNotes, 3);
    }

    private void validateFields(int cassetteType, int numberOfNotes) {
        if (cassetteType < 1 || cassetteType > 7) {
            throw new IllegalArgumentException(numberOfNotes + " is not within valid 'Cassette Type' range (1-7 decimal)");
        }
        if (numberOfNotes < 1 || numberOfNotes > 999) {
            throw new IllegalArgumentException(numberOfNotes + " is not within valid 'Number of Notes' range (1-999 decimal)");
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RecycleCassette.class.getSimpleName() + ": {", "}")
                .add("type: " + type)
                .add("numberOfNotes: " + numberOfNotes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecycleCassette that = (RecycleCassette) o;
        return type == that.type && numberOfNotes == that.numberOfNotes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberOfNotes);
    }
}
