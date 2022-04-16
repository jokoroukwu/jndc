package io.github.jokoroukwu.jndc.central.terminalcommand.commandcode;

import java.util.Objects;
import java.util.StringJoiner;

public final class BaseCommandModifier implements CommandModifier {
    private final int value;

    public BaseCommandModifier(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseCommandModifier.class.getSimpleName() + ": {", "}")
                .add("value: " + value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCommandModifier that = (BaseCommandModifier) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
