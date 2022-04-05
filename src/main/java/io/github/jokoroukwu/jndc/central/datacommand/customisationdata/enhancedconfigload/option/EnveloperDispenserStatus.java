package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum EnveloperDispenserStatus implements ConfigurationOption {
    OFF(0),
    ON(2);

    public static final int NUMBER = 23;
    private final int code;
    private final String ndcString;

    EnveloperDispenserStatus(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }


    public static DescriptiveOptional<EnveloperDispenserStatus> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(OFF);
        }
        if (code == 2 || code == 3) {
            return DescriptiveOptional.of(ON);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Envelope dispenser status' option code", code));
    }

    public static DescriptiveOptional<EnveloperDispenserStatus> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Envelope dispenser status' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(EnveloperDispenserStatus::forCode);
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
        return new StringJoiner(", ", EnveloperDispenserStatus.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
