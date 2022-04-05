package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcComponent;

import java.util.Objects;
import java.util.StringJoiner;

public final class BufferB implements NdcComponent {
    private final String value;

    BufferB(String value, Void empty) {
        this.value = value;
    }

    public BufferB(String value) {
        this.value = validateValue(value);
    }

    public boolean isTimedOut() {
        return lastChar() == 'T';
    }

    public boolean isCancelled() {
        return lastChar() == 'E';
    }

    private char lastChar() {
        final int length = value.length();
        if (length == 0) {
            return (char) -1;
        }
        return value.charAt(length - 1);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BufferB.class.getSimpleName() + ": {", "}")
                .add("value: '" + value + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BufferB)) return false;
        BufferB that = (BufferB) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    private String validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("'Buffer B' value may not be null");
        }
        final int length = value.length();
        if (length < 3 || length > 32) {
            final String message = "'Buffer B' value length should be within 3-32 characters but was %d for value: %s";
            throw new IllegalArgumentException(String.format(message, length, value));
        }
        return value;
    }
}
