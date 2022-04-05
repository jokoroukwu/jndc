package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;


public final class RollWidth implements ConfigurationOption {
    public static final RollWidth DEFAULT = new RollWidth(25);

    public static final int NUMBER = 4;
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 40;

    private final int code;
    private final String ndcString;

    private RollWidth(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<RollWidth> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new RollWidth(code));
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%d' is not within valid range of 'Roll Width' option code (%d-%d)", code, MIN_VALUE, MAX_VALUE));
    }

    public static DescriptiveOptional<RollWidth> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Roll Width' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(RollWidth::forCode);
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
        return new StringJoiner(", ", RollWidth.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RollWidth)) return false;
        RollWidth rollWidth = (RollWidth) o;
        return code == rollWidth.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
