package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class KeyboardData implements NdcComponent {
    public static final int MIN_NUMBER = 0;
    public static final int MAX_NUMBER = 999;
    private final int number;
    private final Map<Integer, Integer> dataEntries;

    public KeyboardData(int number, Map<Integer, Integer> dataEntries) {
        validateNumber(number);
        this.number = number;
        this.dataEntries = ObjectUtils.validateNotNull(dataEntries, "dataEntries");
    }

    public KeyboardData(int number) {
        this(number, Collections.emptyMap());
    }

    private void validateNumber(int value) {
        if (value < MIN_NUMBER || value > MAX_NUMBER) {
            final String errorMessage = String.format("Keyboard number must be a non-negative value but was: %d", value);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public int getNumber() {
        return number;
    }

    public Map<Integer, Integer> getDataEntries() {
        return Collections.unmodifiableMap(dataEntries);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", KeyboardData.class.getSimpleName() + ": {", "}")
                .add("number: " + number)
                .add("dataEntries: " + dataEntries)
                .toString();
    }

    @Override
    public String toNdcString() {
        return dataEntries.entrySet()
                .stream()
                .map(entry -> entry.getKey().toString() + Integer.toHexString(entry.getValue()).toUpperCase())
                .collect(Collectors.joining(Strings.EMPTY_STRING, Strings.leftPad(number, "0", 3), Strings.EMPTY_STRING));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyboardData that = (KeyboardData) o;
        return number == that.number && dataEntries.equals(that.dataEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, dataEntries);
    }
}
