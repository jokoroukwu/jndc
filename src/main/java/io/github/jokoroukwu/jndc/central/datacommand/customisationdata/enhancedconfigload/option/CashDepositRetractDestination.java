package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum CashDepositRetractDestination implements ConfigurationOption {

    DEFAULT(0),

    CASH_IN_CASSETTE(1);

    public static final int NUMBER = 74;

    private final int code;
    private final String ndcString;

    CashDepositRetractDestination(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<CashDepositRetractDestination> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(DEFAULT);
        }
        if (code == 1) {
            return DescriptiveOptional.of(CASH_IN_CASSETTE);
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%s' is not a valid 'Cash Deposit Retract Destination' option code", code));
    }

    public static DescriptiveOptional<CashDepositRetractDestination> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(CashDepositRetractDestination::forCode);
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
        return new StringJoiner(", ", CashDepositRetractDestination.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }
}
