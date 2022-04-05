package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;


public final class BnaSettings implements ConfigurationOption {

    public static final BnaSettings DEFAULT = new BnaSettings(0b00000000);

    public static final BnaSettings INCLUDE_TRANSACTION_STATUS_COUNTS = new BnaSettings(0b00000001);

    public static final BnaSettings ACCEPT_MAX_NOTES = new BnaSettings(0b00000010);

    public static final BnaSettings RETRACT_NOTES = new BnaSettings(0b00000100);

    public static final BnaSettings USE_EXTENDED_MESSAGE = new BnaSettings(0b00001000);

    public static final BnaSettings REPORT_ON_ALL_CASSETTES_FULL = new BnaSettings(0b00010000);

    public static final BnaSettings ENHANCED_CASH_DEPOSIT = new BnaSettings(0b00100000);

    public static final BnaSettings USE_RECYCLING_CASSETTES = new BnaSettings(0b01000000);

    public static final BnaSettings ALL = new BnaSettings(0b01111111);

    public static final int NUMBER = 45;

    private final int code;
    private final String ndcString;

    private BnaSettings(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<BnaSettings> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new BnaSettings(code));
        }
        return DescriptiveOptional.empty(() ->
                String.format("value '%s' is not within valid 'BNA Settings' option code range", code));
    }

    public static DescriptiveOptional<BnaSettings> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'BNA Settings' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(BnaSettings::forCode);
    }

    public static BnaSettings unionOf(Set<BnaSettings> bnaSettings) {
        ObjectUtils.validateNotNull(bnaSettings, "bnaSettings");
        if (bnaSettings.contains(USE_RECYCLING_CASSETTES)) {
            if (!(bnaSettings.contains(USE_EXTENDED_MESSAGE) && bnaSettings.contains(ACCEPT_MAX_NOTES))) {
                throw new IllegalArgumentException("Bits 1 and 3 must be set if bit 6 is set");
            }
        } else if (bnaSettings.contains(USE_EXTENDED_MESSAGE) && !bnaSettings.contains(ACCEPT_MAX_NOTES)) {
            throw new IllegalArgumentException("Bit 1 must be set if bit 3 is set");
        }
        int code = 0;
        for (var setting : bnaSettings) {
            code |= setting.code;
        }
        return new BnaSettings(code);
    }

    public static BnaSettings unionOf(BnaSettings... bnaSettings) {
        return unionOf(Set.of(bnaSettings));
    }


    public static boolean isCodeValid(int code) {
        if (code < 0 || code > 127) {
            return false;
        }
        if (isRecyclingCassettes(code)) {
            return isExtendedMessage(code) && isAcceptMaxNotes(code);
        }
        if (isExtendedMessage(code)) {
            return isAcceptMaxNotes(code);
        }
        return true;
    }

    public static boolean areTransactionStatusCountsIncluded(int code) {
        return (code & INCLUDE_TRANSACTION_STATUS_COUNTS.code) != 0;
    }

    public static boolean isAcceptMaxNotes(int code) {
        return (code & ACCEPT_MAX_NOTES.code) != 0;
    }

    public static boolean isRetractNotes(int code) {
        return (code & RETRACT_NOTES.code) != 0;
    }

    public static boolean isExtendedMessage(int code) {
        return (code & USE_EXTENDED_MESSAGE.code) != 0;
    }

    public static boolean isReportOnAllCassettesFull(int code) {
        return (code & REPORT_ON_ALL_CASSETTES_FULL.code) != 0;
    }

    public static boolean isEnhancedCashDeposit(int code) {
        return (code & ENHANCED_CASH_DEPOSIT.code) != 0;
    }

    public static boolean isRecyclingCassettes(int code) {
        return (code & USE_RECYCLING_CASSETTES.code) != 0;
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
        return new StringJoiner(", ", BnaSettings.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BnaSettings that = (BnaSettings) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
