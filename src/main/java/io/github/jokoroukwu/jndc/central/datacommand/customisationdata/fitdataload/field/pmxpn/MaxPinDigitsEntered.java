package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class MaxPinDigitsEntered implements NdcComponent {
    private final int numberOfPinDigits;
    private final PinBlockType pinBlockType;

    public MaxPinDigitsEntered(PinBlockType pinBlockType, int numberOfPinDigits) {
        this.numberOfPinDigits = Integers.validateRange(numberOfPinDigits, 4, 16, "numberOfPinDigits");
        this.pinBlockType = ObjectUtils.validateNotNull(pinBlockType, "pinBlockType");
    }

    MaxPinDigitsEntered(PinBlockType pinBlockType, int numberOfPinDigits, Void unused) {
        this.numberOfPinDigits = numberOfPinDigits;
        this.pinBlockType = pinBlockType;
    }


    public int getNumberOfPinDigits() {
        return numberOfPinDigits;
    }

    public PinBlockType getPinBlockType() {
        return pinBlockType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MaxPinDigitsEntered.class.getSimpleName() + ": {", "}")
                .add("numberOfPinDigits: " + numberOfPinDigits)
                .add("pinBlockType: " + pinBlockType)
                .toString();
    }

    @Override
    public String toNdcString() {
        final int maxPinDigitsEnteredValue = (pinBlockType.getValue() << 6) | numberOfPinDigits;
        return Integers.toZeroPaddedString(maxPinDigitsEnteredValue, 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxPinDigitsEntered that = (MaxPinDigitsEntered) o;
        return numberOfPinDigits == that.numberOfPinDigits && pinBlockType == that.pinBlockType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPinDigits, pinBlockType);
    }
}
