package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum ReportDualModeAndHardcopy implements ConfigurationOption {
    /**
     * Default.
     */
    OFF(0),

    DUAL_MODE_ONLY(1),

    ALL(2);

    public static final int NUMBER = 35;

    private final int code;
    private final String ndcString;

    ReportDualModeAndHardcopy(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<ReportDualModeAndHardcopy> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(OFF);
        }
        if (code == 1) {
            return DescriptiveOptional.of(DUAL_MODE_ONLY);
        }
        if (code == 2) {
            return DescriptiveOptional.of(ALL);
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%d' is not a valid 'Report Dual Mode EJ and Hardcopy Backup Unsolicited Messages' option code", code));
    }

    public static DescriptiveOptional<ReportDualModeAndHardcopy> forCode(String code) {
        return DescriptiveOptional.ofNullable(code,
                () -> "'Report Dual Mode EJ and Hardcopy Backup Unsolicited Messages' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(ReportDualModeAndHardcopy::forCode);
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
        return new StringJoiner(", ", ReportDualModeAndHardcopy.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
