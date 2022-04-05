package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;


public final class Aid implements NdcComponent {
    private final int aidValueLength;
    private final String aidValue;

    //  internal no-validation constructor;
    //  is used by AID reader
    Aid(String aidValue, int aidValueLength) {
        this.aidValue = aidValue;
        this.aidValueLength = aidValueLength;
    }

    /**
     * Binary value stored as ASCII hex.
     * <p>
     * For example, an AID of 0xA000000026 is stored as the characterised
     * value ʹA000000026ʹ and has length ʹ05ʹ (field ʹf2ʹ).
     *
     * @param aidValue length range is 1‐32 hex characters.
     */
    public Aid(String aidValue) {
        Strings.validateLengthRange(aidValue, 1, 32, "AID value");
        Strings.validateIsHex(aidValue, "AID value");
        Integers.validateIsEven(aidValue.length(), "AID value");
        this.aidValue = aidValue;
        this.aidValueLength = aidValue.length() / 2;
    }

    public String getAidValue() {
        return aidValue;
    }

    public int getAidValueLength() {
        return aidValueLength;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Aid.class.getSimpleName() + ": {", "}")
                .add("aidValueLength: " + aidValueLength)
                .add("aidValue: '" + aidValue + "'")
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(2 + aidValue.length())
                .appendEvenZeroPaddedHex(aidValueLength)
                .append(aidValue)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aid aid = (Aid) o;
        return aidValueLength == aid.aidValueLength && aidValue.equals(aid.aidValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aidValueLength, aidValue);
    }

}
