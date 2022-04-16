package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public enum TerminalMessageClass implements NdcComponent {
    UNSOLICITED('1', "Unsolicited ('1')"),
    SOLICITED('2', "Solicited ('2')");

    private final char value;
    private final String displayedName;

    TerminalMessageClass(char value, String displayedName) {
        this.value = value;
        this.displayedName = displayedName;
    }

    public static DescriptiveOptional<TerminalMessageClass> forValue(char value) {
        switch (value) {
            case '1':
                return DescriptiveOptional.of(UNSOLICITED);
            case '2':
                return DescriptiveOptional.of(SOLICITED);
            default: {
                return DescriptiveOptional.empty(() -> String.format("'%c' does not match any 'Terminal Message Class'", value));
            }
        }
    }

    public static DescriptiveOptional<TerminalMessageClass> forValue(int value) {
        return forValue((char) value);
    }

    public char value() {
        return value;
    }

    @Override
    public String toNdcString() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        return displayedName;
    }
}
