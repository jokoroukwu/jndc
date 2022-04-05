package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum PinBlockType {
    DIEBOLD(0),
    PBFMT(1),
    ISO_FORMAT_0(2),
    BANKSYS(3);

    private final int value;
    private final String displayedName;

    PinBlockType(int value) {
        this.value = value;
        displayedName = String.format("%s (%d)", name(), value);
    }

    public static DescriptiveOptional<PinBlockType> forValue(int value) {
        switch (value) {
            case 0:
                return DescriptiveOptional.of(DIEBOLD);
            case 1:
                return DescriptiveOptional.of(PBFMT);
            case 2:
                return DescriptiveOptional.of(ISO_FORMAT_0);
            case 3:
                return DescriptiveOptional.of(BANKSYS);
            default: {
                return DescriptiveOptional.empty(() -> " is not a valid 'PIN Block Type'");
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
