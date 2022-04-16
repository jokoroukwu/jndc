package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum MediaEntryExitIndicators implements ConfigurationOption {
    /**
     * Default.
     */
    FAST(0),

    MEDIUM(1),

    SLOW(3),

    CONTINUOUSLY_ON(5);

    public static final int NUMBER = 25;

    private final int code;
    private final String ndcString;

    MediaEntryExitIndicators(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<MediaEntryExitIndicators> forCode(int code) {
        switch (code) {
            case 0:
                return DescriptiveOptional.of(FAST);
            case 1:
            case 2:
                return DescriptiveOptional.of(MEDIUM);
            case 3:
            case 4:
                return DescriptiveOptional.of(SLOW);
            case 5:
                return DescriptiveOptional.of(CONTINUOUSLY_ON);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%d' is not a valid 'Media entry/exit indicators flash rate' option code", code));
            }
        }
    }

    public static DescriptiveOptional<MediaEntryExitIndicators> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Media entry/exit indicators flash rate' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(MediaEntryExitIndicators::forCode);
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
        return new StringJoiner(", ", MediaEntryExitIndicators.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
