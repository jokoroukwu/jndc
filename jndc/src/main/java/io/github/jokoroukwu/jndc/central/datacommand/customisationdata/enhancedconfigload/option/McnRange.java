package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum McnRange implements ConfigurationOption {

    DEFAULT_RANGE_REJECT_INCORRECT(0),

    EXT_RANGE_REJECT_INCORRECT(1),

    DEFAULT_RANGE_IGNORE_INCORRECT(4),

    EXT_RANGE_IGNORE_INCORRECT(5);

    public static final int NUMBER = 34;
    private final int code;
    private final String ndcString;

    McnRange(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<McnRange> forCode(int code) {
        switch (code) {
            case 0:
                return DescriptiveOptional.of(DEFAULT_RANGE_REJECT_INCORRECT);
            case 1:
                return DescriptiveOptional.of(EXT_RANGE_REJECT_INCORRECT);
            case 4:
                return DescriptiveOptional.of(DEFAULT_RANGE_IGNORE_INCORRECT);
            case 5:
                return DescriptiveOptional.of(EXT_RANGE_IGNORE_INCORRECT);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'MCN range' option code", code));
            }
        }
    }

    public static DescriptiveOptional<McnRange> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'MCN range' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(McnRange::forCode);
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
        return new StringJoiner(", ", McnRange.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
