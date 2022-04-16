package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;


public enum AlphanumericStateEntry implements ConfigurationOption {
    OFF(0),

    ON(1);

    public static final int NUMBER = 80;
    private final int code;
    private final String ndcString;

    AlphanumericStateEntry(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<AlphanumericStateEntry> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(OFF);
        }
        if (code == 1) {
            return DescriptiveOptional.of(ON);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Alphanumeric State Entry' option code", code));
    }

    public static DescriptiveOptional<AlphanumericStateEntry> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(AlphanumericStateEntry::forCode);
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
}
