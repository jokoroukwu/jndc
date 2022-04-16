package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;

public enum DateFormat implements ConfigurationOption {
    MM_DD_YY(1),
    DD_MM_YY(2);

    public static final int NUMBER = 3;
    private final int code;
    private final String ndcString;

    DateFormat(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<DateFormat> forCode(int code) {
        if (code == 1) {
            return DescriptiveOptional.of(MM_DD_YY);
        }
        if (code == 2) {
            return DescriptiveOptional.of(DD_MM_YY);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%d' is not a valid 'Date Format' option code", code));
    }

    public static DescriptiveOptional<DateFormat> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Date Format' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(DateFormat::forCode);
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
        return new StringJoiner(", ", DateFormat.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
