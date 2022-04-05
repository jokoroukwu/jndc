package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum PrintTrackTwo implements ConfigurationOption {
    /**
     * Default.
     */
    ON(0),

    OFF(1);

    public static final int NUMBER = 37;

    private final int code;
    private final String ndcString;

    PrintTrackTwo(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<PrintTrackTwo> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(ON);
        }
        if (code == 1) {
            return DescriptiveOptional.of(OFF);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Print Track 2 to Journal' option code", code));
    }

    public static DescriptiveOptional<PrintTrackTwo> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Print Track 2 to Journal' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(PrintTrackTwo::forCode);
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
        return new StringJoiner(", ", PrintTrackTwo.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
