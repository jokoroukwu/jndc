package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum TransactionStatusInfo implements ConfigurationOption {
    /**
     * Default.
     */
    DO_NOT_APPEND(0),

    APPEND(1);

    public static final int NUMBER = 15;
    private final int code;
    private final String ndcString;

    TransactionStatusInfo(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<TransactionStatusInfo> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(DO_NOT_APPEND);
        }
        if (code == 1) {
            return DescriptiveOptional.of(APPEND);
        }
        return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Transaction status information' option code", code));
    }

    public static DescriptiveOptional<TransactionStatusInfo> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Transaction status information' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(TransactionStatusInfo::forCode);
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
        return new StringJoiner(", ", TransactionStatusInfo.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
