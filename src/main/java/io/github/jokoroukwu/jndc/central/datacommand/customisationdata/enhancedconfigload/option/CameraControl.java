package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum CameraControl implements ConfigurationOption {
    /**
     * Default.
     */
    ON(1),

    OFF(2);

    public static final int NUMBER = 0;
    private final int code;
    private final String ndcString;

    CameraControl(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<CameraControl> forCode(int code) {
        if (code == 1) {
            return DescriptiveOptional.of(ON);
        }
        if (code == 2) {
            return DescriptiveOptional.of(OFF);
        }
        return DescriptiveOptional.empty(() -> code + " is not a valid 'Camera Control' option code");
    }

    public static DescriptiveOptional<CameraControl> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Camera Control' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(CameraControl::forCode);
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
        return new StringJoiner(", ", CameraControl.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
