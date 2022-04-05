package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum PinVerificationType {

    DES(0, "DES (0)"),
    VISA(1, "VISA (1)"),
    DIEBOLD(2, "DIEBOLD (2)"),
    RESERVED_3(3, "Reserved (3)"),
    RESERVED_4(4, "Reserved (4)"),
    RESERVED_5(5, "Reserved (5)"),
    RESERVED_6(6, "Reserved (6)"),
    RESERVED_7(7, "Reserved (7)");

    private final int value;
    private final String displayedName;

    PinVerificationType(int value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<PinVerificationType> forValue(int value) {
        switch (value) {
            case 0:
                return DescriptiveOptional.of(DES);
            case 1:
                return DescriptiveOptional.of(VISA);
            case 2:
                return DescriptiveOptional.of(DIEBOLD);
            case 3:
                return DescriptiveOptional.of(RESERVED_3);
            case 4:
                return DescriptiveOptional.of(RESERVED_4);
            case 5:
                return DescriptiveOptional.of(RESERVED_5);
            case 6:
                return DescriptiveOptional.of(RESERVED_6);
            case 7:
                return DescriptiveOptional.of(RESERVED_7);
            default: {
                return DescriptiveOptional.empty(() -> value + " is not a valid 'PIN Verification Type'");
            }
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
