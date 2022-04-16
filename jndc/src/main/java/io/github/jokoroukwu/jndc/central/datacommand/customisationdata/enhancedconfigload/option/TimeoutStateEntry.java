package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;


public final class TimeoutStateEntry implements ConfigurationOption {
    public static final int NUMBER = 71;
    public static final int MAX_CODE_VALUE = 9;
    public static final int MIN_CODE_VALUE = 0;
    public static final int UNLIMITED_CODE_VALUE = 255;

    public static final TimeoutStateEntry NEVER = new TimeoutStateEntry(MIN_CODE_VALUE);

    /**
     * Default.
     */
    public static final TimeoutStateEntry UNLIMITED = new TimeoutStateEntry(MAX_CODE_VALUE);


    private final int code;
    private final String ndcString;

    private TimeoutStateEntry(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<TimeoutStateEntry> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new TimeoutStateEntry(code));
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%d' is not within valid range of 'Time‐Out State entry' option code (%d-%d or %d)",
                code, MIN_CODE_VALUE, MAX_CODE_VALUE, UNLIMITED_CODE_VALUE));
    }

    public static DescriptiveOptional<TimeoutStateEntry> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Time‐Out State entry' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(TimeoutStateEntry::forCode);
    }

    public static boolean isCodeValid(int code) {
        return code == UNLIMITED_CODE_VALUE || (code >= MIN_CODE_VALUE && code <= MAX_CODE_VALUE);
    }

    @Override
    public int getNumber() {
        return NUMBER;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toNdcString() {
        return ndcString;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TimeoutStateEntry.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeoutStateEntry that = (TimeoutStateEntry) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
