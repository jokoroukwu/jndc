package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum EjBackup implements ConfigurationOption {
    /**
     * Default.
     */
    STANDARD(0),

    MULTIPLE(1);

    public static final int NUMBER = 36;

    private final int code;
    private final String ndcString;

    EjBackup(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<EjBackup> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(STANDARD);
        }
        if (code == 1) {
            return DescriptiveOptional.of(MULTIPLE);
        }
        var errorMessageTemplate = "value '%s' is not a valid 'Enhanced EJ backup' option code";
        return DescriptiveOptional.empty(() -> String.format(errorMessageTemplate, code));
    }

    public static DescriptiveOptional<EjBackup> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Enhanced EJ backup' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(EjBackup::forCode);
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
        return new StringJoiner(", ", EjBackup.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
