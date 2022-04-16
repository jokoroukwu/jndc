package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Collection;
import java.util.StringJoiner;


public final class SupplyModeReadyStatusAmountLength implements ConfigurationOption {
    /**
     * Default.
     */
    public static final SupplyModeReadyStatusAmountLength DEFAULT = new SupplyModeReadyStatusAmountLength(0b00000000);
    public static final SupplyModeReadyStatusAmountLength READY_STATUS = new SupplyModeReadyStatusAmountLength(0b00000001);
    public static final SupplyModeReadyStatusAmountLength SUPPLY_MODE = new SupplyModeReadyStatusAmountLength(0b00000010);
    public static final SupplyModeReadyStatusAmountLength AMOUNT_BUFFER_LENGTH = new SupplyModeReadyStatusAmountLength(0b00001000);
    public static final SupplyModeReadyStatusAmountLength TRANSACTION_STATUS_DATA = new SupplyModeReadyStatusAmountLength(0b00010000);

    public static final int NUMBER = 1;

    private final int code;
    private final String ndcString;

    SupplyModeReadyStatusAmountLength(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<SupplyModeReadyStatusAmountLength> forCode(int code) {
        if (isValidCode(code)) {
            return DescriptiveOptional.of(new SupplyModeReadyStatusAmountLength(code));
        }
        return DescriptiveOptional.empty(() -> code + " is not a valid 'Supply Mode, Ready Status & Amount Buffer Length' code");
    }

    public static DescriptiveOptional<SupplyModeReadyStatusAmountLength> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Supply Mode, Ready Status & Amount Buffer Length' code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(SupplyModeReadyStatusAmountLength::forCode);
    }

    public static SupplyModeReadyStatusAmountLength unionOf(Collection<SupplyModeReadyStatusAmountLength> options) {
        ObjectUtils.validateNotNull(options, "options cannot be null");
        int value = 0;
        for (SupplyModeReadyStatusAmountLength option : options) {
            value |= option.value();
        }
        return new SupplyModeReadyStatusAmountLength(value);
    }

    public static boolean isValidCode(int code) {
        final int mask = 0b00011011;
        return (code | mask) == mask;
    }

    private static void checkCodeIsValid(int code) {
        if (!isValidCode(code)) {
            throw new IllegalArgumentException(code + " is not a valid 'Supply Mode, Ready Status & Amount Buffer Length' code");
        }
    }

    public static boolean isReadyStatusEnabled(int code) {
        checkCodeIsValid(code);
        return (code & READY_STATUS.value()) != 0;
    }

    public static boolean isSupplyModeEnabled(int code) {
        checkCodeIsValid(code);
        return (code & SUPPLY_MODE.value()) != 0;
    }

    public static boolean hasExtendedAmountBufferLength(int code) {
        checkCodeIsValid(code);
        return (code & AMOUNT_BUFFER_LENGTH.value()) != 0;
    }

    public static boolean isTransactionStatusDataIncluded(int code) {
        checkCodeIsValid(code);
        return (code & TRANSACTION_STATUS_DATA.value()) != 0;
    }

    public static boolean isDefault(int code) {
        checkCodeIsValid(code);
        return code == DEFAULT.code;
    }

    public int value() {
        return code;
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
        return new StringJoiner(", ", SupplyModeReadyStatusAmountLength.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplyModeReadyStatusAmountLength)) return false;
        SupplyModeReadyStatusAmountLength that = (SupplyModeReadyStatusAmountLength) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return code;
    }
}
