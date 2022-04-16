package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;


public final class JournalPrintOperations implements ConfigurationOption {

    public static final JournalPrintOperations DEFAULT = new JournalPrintOperations(0);

    public static final int NUMBER = 17;
    public static final int MAX_VALUE = 225;
    public static final int MIN_VALUE = 0;

    private final int code;
    private final String ndcString;

    private JournalPrintOperations(int code) {
        this.code = code;
        ndcString = String.format("%d03%d", NUMBER, code);
    }

    public static DescriptiveOptional<JournalPrintOperations> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new JournalPrintOperations(code));
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%s' is not within valid range of 'Journal printer backup print operations' option code (%d-%d)",
                code, MIN_VALUE, MAX_VALUE));
    }

    public static DescriptiveOptional<JournalPrintOperations> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Journal printer backup print operations' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(JournalPrintOperations::forCode);
    }

    public static boolean isCodeValid(int code) {
        return code >= MIN_VALUE && code <= MAX_VALUE;

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
        return new StringJoiner(", ", JournalPrintOperations.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalPrintOperations)) return false;
        JournalPrintOperations that = (JournalPrintOperations) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
