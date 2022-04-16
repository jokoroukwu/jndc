package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.secondarycardreadergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class SecondCardReaderData implements IdentifiableCounterGroup {
    public static final char ID = 'T';

    private final int cardsCaptured;

    public SecondCardReaderData(int cardsCaptured) {
        this.cardsCaptured = Integers.validateRange(cardsCaptured, 0, 99999, "'Card Captured'");
    }

    SecondCardReaderData(int cardsCaptured, Void unused) {
        this.cardsCaptured = cardsCaptured;
    }

    public int getCardsCaptured() {
        return cardsCaptured;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(6)
                .append(ID)
                .appendZeroPadded(cardsCaptured, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecondCardReaderData.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("cardsCaptured: " + cardsCaptured)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecondCardReaderData that = (SecondCardReaderData) o;
        return cardsCaptured == that.cardsCaptured;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardsCaptured);
    }
}
