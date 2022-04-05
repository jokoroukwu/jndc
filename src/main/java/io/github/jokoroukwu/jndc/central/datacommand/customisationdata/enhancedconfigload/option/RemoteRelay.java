package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum RemoteRelay implements ConfigurationOption {
    /**
     * Default.
     */
    DEFAULT(0),

    /**
     * Remote relay is active only when the SST is in service.
     */
    SST(1);

    public static final int NUMBER = 27;

    private final int code;
    private final String ndcString;

    RemoteRelay(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }


    public static DescriptiveOptional<RemoteRelay> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(DEFAULT);
        }
        if (code == 1) {
            return DescriptiveOptional.of(SST);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Remote relay' option code", code));
    }

    public static DescriptiveOptional<RemoteRelay> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Remote relay' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(RemoteRelay::forCode);
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
        return new StringJoiner(", ", RemoteRelay.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
