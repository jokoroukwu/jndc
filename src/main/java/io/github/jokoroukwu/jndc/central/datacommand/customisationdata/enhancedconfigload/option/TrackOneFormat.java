package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum TrackOneFormat implements ConfigurationOption {
    /**
     * Default.
     */
    ISO(1),

    VISA(2),

    AUTOSEARCH_BACKWARDS(3),

    AUTOSEARCH_FORWARDS(4);

    public static final int NUMBER = 7;
    private final int code;
    private final String ndcString;

    TrackOneFormat(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<TrackOneFormat> forCode(int code) {
        switch (code) {
            case 1:
                return DescriptiveOptional.of(ISO);
            case 2:
                return DescriptiveOptional.of(VISA);
            case 3:
                return DescriptiveOptional.of(AUTOSEARCH_BACKWARDS);
            case 4:
                return DescriptiveOptional.of(AUTOSEARCH_FORWARDS);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%d' is not a valid 'Track 1 format' option code", code));
            }
        }
    }

    public static DescriptiveOptional<TrackOneFormat> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Track 1 format' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(TrackOneFormat::forCode);
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
        return new StringJoiner(", ", TrackOneFormat.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
