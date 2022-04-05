package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum DataId implements NdcComponent {
    DEPOSIT_DATA('1'),
    DISPENSE_DATA('2');

    private final char value;

    DataId(char value) {
        this.value = value;
    }

    public static DescriptiveOptional<DataId> forValue(char value) {
        if (value == '1') {
            return DescriptiveOptional.of(DEPOSIT_DATA);
        }
        if (value == '2') {
            return DescriptiveOptional.of(DISPENSE_DATA);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid 'Data Identifier'", value));
    }

    public static DescriptiveOptional<DataId> forValue(int value) {
        return forValue((char) value);
    }


    public char getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }
}
