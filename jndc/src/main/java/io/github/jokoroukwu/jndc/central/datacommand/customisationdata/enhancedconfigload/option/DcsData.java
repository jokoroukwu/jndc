package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum DcsData implements ConfigurationOption {
    PAN_INCLUDED(0),

    PAN_NOT_INCLUDED(1);

    public static final int NUMBER = 30;

    private final int code;
    private final String ndcString;

    DcsData(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<DcsData> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(PAN_INCLUDED);
        }
        if (code == 1) {
            return DescriptiveOptional.of(PAN_NOT_INCLUDED);
        }
        return DescriptiveOptional.empty(() ->
                String.format("value %d is not a valid 'Include PAN in DCS Data' option code", code));
    }

    public static DescriptiveOptional<DcsData> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Include PAN in DCS Data' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(DcsData::forCode);
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
        return new StringJoiner(", ", DcsData.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
