package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;


public final class JournalBackupTime implements ConfigurationOption {

    public static final JournalBackupTime DEFAULT = new JournalBackupTime(0);

    public static final int MAX_VALUE = 255;
    public static final int MIN_VALUE = 0;
    public static final int NUMBER = 16;

    private final int code;
    private final String ndcString;

    private JournalBackupTime(int code) {
        this.code = code;
        ndcString = String.format("%d03%d", NUMBER, code);
    }

    public static DescriptiveOptional<JournalBackupTime> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new JournalBackupTime(code));
        }
        return DescriptiveOptional.empty(() ->
                String.format("value '%d' is not within valid range of 'Journal Printer Backup Time' option code (%d-%d)",
                        code, MIN_VALUE, MAX_VALUE));
    }

    public static DescriptiveOptional<JournalBackupTime> forCode(String code) {
        return DescriptiveOptional.ofNullable(code)
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(JournalBackupTime::forCode);
    }

    public static boolean isCodeValid(int code) {
        return code >= MIN_VALUE && code <= MAX_VALUE;
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
        return new StringJoiner(", ", JournalBackupTime.class.getSimpleName() + ": {", "}")
                .add("number: " + NUMBER)
                .add("code: " + code)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalBackupTime)) return false;
        JournalBackupTime that = (JournalBackupTime) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(NUMBER, code);
    }
}
