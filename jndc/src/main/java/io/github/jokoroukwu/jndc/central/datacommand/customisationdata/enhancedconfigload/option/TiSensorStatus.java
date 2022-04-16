package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum TiSensorStatus implements ConfigurationOption {
    OFF(0),
    ENHANCED_TI(1),
    FLEXIBLE_TI(2);

    public static final int NUMBER = 24;
    private final int code;
    private final String ndcString;

    TiSensorStatus(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<TiSensorStatus> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(OFF);
        }
        if (code == 1) {
            return DescriptiveOptional.of(ENHANCED_TI);
        }
        if (code == 2) {
            return DescriptiveOptional.of(FLEXIBLE_TI);
        }

        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Enhanced TI/Sensor status' option code", code));
    }

    public static DescriptiveOptional<TiSensorStatus> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Enhanced TI/Sensor status' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(TiSensorStatus::forCode);
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
        return new StringJoiner(", ", TiSensorStatus.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
