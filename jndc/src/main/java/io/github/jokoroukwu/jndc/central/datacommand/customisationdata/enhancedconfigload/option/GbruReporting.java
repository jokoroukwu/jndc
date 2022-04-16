package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum GbruReporting implements ConfigurationOption {
    REPORT_CDM(0),

    REPORT_GRBRU(1);

    public static final int NUMBER = 78;

    private final int code;
    private final String ndcString;

    GbruReporting(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<GbruReporting> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(REPORT_CDM);
        }
        if (code == 1) {
            return DescriptiveOptional.of(REPORT_GRBRU);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'GBRU variant reporting' option code", code));
    }

    public static DescriptiveOptional<GbruReporting> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(GbruReporting::forCode);
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
        return new StringJoiner(", ", GbruReporting.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
