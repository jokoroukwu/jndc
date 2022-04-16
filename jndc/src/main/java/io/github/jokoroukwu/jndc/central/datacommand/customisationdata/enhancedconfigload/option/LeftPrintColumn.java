package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;

public final class LeftPrintColumn implements ConfigurationOption {
    public static final LeftPrintColumn DEFAULT = new LeftPrintColumn(8);

    public static final int NUMBER = 5;
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 40;

    private final int code;
    private final String ndcString;

    private LeftPrintColumn(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<LeftPrintColumn> forCode(int code) {
        if (isValid(code)) {
            return DescriptiveOptional.of(new LeftPrintColumn(code));
        }
        var template = "value '%d' is not within valid range of 'Left Print Column' option code";
        return DescriptiveOptional.empty(() -> String.format(template, code));
    }

    public static boolean isValid(int code) {
        return code >= MIN_VALUE && code <= MAX_VALUE;
    }

    public static DescriptiveOptional<LeftPrintColumn> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Left Print Column' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(LeftPrintColumn::forCode);
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
        return new StringJoiner(", ", LeftPrintColumn.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeftPrintColumn)) return false;
        LeftPrintColumn that = (LeftPrintColumn) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
