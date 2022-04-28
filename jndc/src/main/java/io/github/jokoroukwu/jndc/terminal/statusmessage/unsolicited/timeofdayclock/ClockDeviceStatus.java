package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum ClockDeviceStatus implements NdcComponent {
    RESET('1'),
    STOPPED('2');

    private final char value;
    private final String displayedName;

    ClockDeviceStatus(char value) {
        this.value = value;
        displayedName = String.format("%s ('%c')", name(), value);
    }

    public static DescriptiveOptional<ClockDeviceStatus> forValue(char value) {
        if (value == '1') {
            return DescriptiveOptional.of(RESET);
        }
        if (value == '2') {
            return DescriptiveOptional.of(STOPPED);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%c' is not a valid Clock Device Status", value));
    }

    public static DescriptiveOptional<ClockDeviceStatus> forValue(int value) {
        return forValue((char) value);
    }

    public char getValue() {
        return value;
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
