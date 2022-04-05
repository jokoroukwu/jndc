package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class Cassette implements NdcComponent {
    private final int cassetteType;
    private final int numberOfNotes;
    private final String additionalData;


    Cassette(int cassetteType, int numberOfNotes, String additionalData) {
        this.cassetteType = validateCassetteType(cassetteType);
        this.numberOfNotes = Integers.validateRange(numberOfNotes, 1, 999, "Number of Notes");
        this.additionalData = Strings.validateNotNullNotEmpty(additionalData, "additionalData");
    }

    Cassette(int cassetteType, int numberOfNotes, String additionalData, Void unused) {
        this.cassetteType = cassetteType;
        this.numberOfNotes = numberOfNotes;
        this.additionalData = additionalData;
    }

    Cassette(int cassetteType, int numberOfNotes) {
        this(cassetteType, numberOfNotes, Strings.EMPTY_STRING);
    }

    public int getCassetteType() {
        return cassetteType;
    }

    public int getNumberOfNotes() {
        return numberOfNotes;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    protected abstract int validateCassetteType(int cassetteType);

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(6 + additionalData.length())
                .appendZeroPadded(cassetteType, 3)
                .appendZeroPadded(numberOfNotes, 3)
                .append(additionalData)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cassette.class.getSimpleName() + ": {", "}")
                .add("cassetteType: " + cassetteType)
                .add("numberOfNotes: " + numberOfNotes)
                .add("additionalData: '" + additionalData + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cassette cassette = (Cassette) o;
        return cassetteType == cassette.cassetteType && numberOfNotes == cassette.numberOfNotes && additionalData.equals(cassette.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassetteType, numberOfNotes, additionalData);
    }
}
