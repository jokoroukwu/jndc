package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum SendTrackDetails implements ConfigurationOption {
    /**
     * Default.
     */
    NO_CARD_DATA(0),

    WITH_CARD_DATA(1);

    public static final int NUMBER = 41;

    private final int code;
    private final String ndcString;

    SendTrackDetails(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<SendTrackDetails> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(NO_CARD_DATA);
        }
        if (code == 1) {
            return DescriptiveOptional.of(WITH_CARD_DATA);
        }
        var errorMessageTemplate = "value '%s' is not a valid 'Send Track Details on Card Retract' option code";
        return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, code));
    }

    public static DescriptiveOptional<SendTrackDetails> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Send Track Details on Card Retract' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(SendTrackDetails::forCode);
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
        return new StringJoiner(", ", SendTrackDetails.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
