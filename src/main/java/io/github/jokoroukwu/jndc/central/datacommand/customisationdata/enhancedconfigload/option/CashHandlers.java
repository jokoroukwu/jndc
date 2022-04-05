package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;


public final class CashHandlers implements ConfigurationOption {

    public static final CashHandlers DEFAULT = new CashHandlers(0b00000000);
    public static final CashHandlers BIT_ZERO = new CashHandlers(0b00000001);
    public static final CashHandlers BIT_TWO = new CashHandlers(0b00000100);
    public static final CashHandlers COMBINED = new CashHandlers(0b00000101);

    public static final int NUMBER = 76;

    private final int code;
    private final String ndcString;

    CashHandlers(int code) {
        this.code = code;
        this.ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<CashHandlers> forCode(int code) {
        switch (code) {
            case 0b00000000:
                return DescriptiveOptional.of(DEFAULT);
            case 0b00000001:
                return DescriptiveOptional.of(BIT_ZERO);
            case 0b00000100:
                return DescriptiveOptional.of(BIT_TWO);
            case 0b00000101:
                return DescriptiveOptional.of(COMBINED);
            default: {
                return DescriptiveOptional.empty(() -> String.format("value '%s' is not a valid 'Cash Handlers' option code", code));
            }
        }
    }

    public static DescriptiveOptional<CashHandlers> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Cash Handlers' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(CashHandlers::forCode);
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
        return new StringJoiner(", ", CashHandlers.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashHandlers that = (CashHandlers) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }


    public boolean hasBit0() {
        return (code & BIT_ZERO.getCode()) != 0;
    }

    public boolean hasBit2() {
        return (code & BIT_TWO.getCode()) != 0;
    }
}
