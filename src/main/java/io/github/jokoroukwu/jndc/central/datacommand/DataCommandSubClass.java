package io.github.jokoroukwu.jndc.central.datacommand;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum DataCommandSubClass implements NdcComponent {
    CUSTOMISATION_DATA('1', "Customisation Data (1)"),
    ENCRYPTION_KEY_INFO('3', "Encryption Key Information (3)"),
    EXTENDED_ENCRYPTION_KEY_INFO('4', "Extended Encryption Key Information (4)");

    private final char value;
    private final String displayedName;

    DataCommandSubClass(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<DataCommandSubClass> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(CUSTOMISATION_DATA);
            case '3':
                return DescriptiveOptional.of(ENCRYPTION_KEY_INFO);
            case '4':
                return DescriptiveOptional.of(EXTENDED_ENCRYPTION_KEY_INFO);
            default: {
                var errorMessageTemplate = "value '%s' does not match any 'Data Command Message Sub-Class'";
                return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, value));
            }
        }
    }

    public static DescriptiveOptional<DataCommandSubClass> forValue(int value) {
        return forValue((char) value);
    }

    public char value() {
        return value;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    @Override
    public String toString() {
        return displayedName;
    }

    @Override
    public String toNdcString() {
        return Character.toString(value);
    }
}
