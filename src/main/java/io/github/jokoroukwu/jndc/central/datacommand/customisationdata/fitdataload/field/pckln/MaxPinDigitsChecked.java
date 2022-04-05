package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class MaxPinDigitsChecked implements NdcComponent {
    private final PinVerificationType pinVerificationType;
    private final int pinDigitsChecked;

    MaxPinDigitsChecked(PinVerificationType pinVerificationType, int pinDigitsChecked, Void unused) {
        this.pinVerificationType = pinVerificationType;
        this.pinDigitsChecked = pinDigitsChecked;
    }

    public MaxPinDigitsChecked(PinVerificationType pinVerificationType, int pinDigitsChecked) {
        this.pinVerificationType = ObjectUtils.validateNotNull(pinVerificationType, "pinVerificationType");
        validateValues(pinVerificationType, pinDigitsChecked);
        this.pinDigitsChecked = pinDigitsChecked;
    }


    public PinVerificationType getPinVerificationType() {
        return pinVerificationType;
    }

    public boolean isRemote() {
        return pinDigitsChecked == 0;
    }

    public int getPinDigitsChecked() {
        return pinDigitsChecked;
    }


    @Override
    public String toNdcString() {
        return Integers.toZeroPaddedString(((pinVerificationType.getValue() << 5) | pinDigitsChecked), 3);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MaxPinDigitsChecked.class.getSimpleName() + ": {", "}")
                .add("pinVerificationType: " + pinVerificationType)
                .add("pinDigitsChecked: " + pinDigitsChecked)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxPinDigitsChecked that = (MaxPinDigitsChecked) o;
        return pinDigitsChecked == that.pinDigitsChecked && pinVerificationType == that.pinVerificationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pinVerificationType, pinDigitsChecked);
    }

    private void validateValues(PinVerificationType pinVerificationType, int pinDigitsChecked) {
        if (pinVerificationType == PinVerificationType.DES) {
            validateDes(pinDigitsChecked);
        } else {
            validateRange(pinVerificationType, pinDigitsChecked);
        }
    }

    private void validateRange(PinVerificationType pinVerificationType, int pinDigitsChecked) {
        if (!Integers.isInRange(pinDigitsChecked, 4, 16)) {
            throw new IllegalArgumentException(pinDigitsErrorSupplier(pinVerificationType, pinDigitsChecked));
        }
    }

    private void validateDes(int pinDigitsChecked) {
        if (pinDigitsChecked != 0 && !Integers.isInRange(pinDigitsChecked, 4, 16)) {
            throw new IllegalArgumentException(pinDigitsErrorSupplier(PinVerificationType.DES, pinDigitsChecked));
        }
        throw new IllegalArgumentException(pinDigitsErrorSupplier(PinVerificationType.DES, pinDigitsChecked));
    }


    private String pinDigitsErrorSupplier(PinVerificationType pinVerificationType, int pinDigitsChecked) {
        return String.format("number of PIN digits to check should be in range 1-16 for '%s' verification type but was %d",
                pinDigitsChecked, pinDigitsChecked);
    }
}
