package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum Ecd implements ConfigurationOption {
    /**
     * Default.
     */
    NONE(0),

    STANDARD(1),

    MAXIMUM(2);

    public static final int NUMBER = 46;

    private final int code;
    private final String ndcString;

    Ecd(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<Ecd> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(NONE);
        }
        if (code == 1) {
            return DescriptiveOptional.of(STANDARD);
        }
        if (code == 2) {
            return DescriptiveOptional.of(MAXIMUM);
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%d' is not a valid 'MCRW Enhanced Card Device (ECD) Security Jitter' option code", code));
    }

    public static DescriptiveOptional<Ecd> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'MCRW Enhanced Card Device (ECD) Security Jitter' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(Ecd::forCode);
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
        return new StringJoiner(", ", Ecd.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
