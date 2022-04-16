package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum CoinDispenser implements ConfigurationOption {
    DEFAULT(0),

    EXTENDED(1);

    public static final int NUMBER = 79;

    private final int code;
    private final String ndcString;

    CoinDispenser(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<CoinDispenser> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(DEFAULT);
        }
        if (code == 1) {
            return DescriptiveOptional.of(EXTENDED);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Coin Dispenser' option code", code));
    }

    public static DescriptiveOptional<CoinDispenser> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(CoinDispenser::forCode);
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
        return new StringJoiner(", ", CoinDispenser.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
