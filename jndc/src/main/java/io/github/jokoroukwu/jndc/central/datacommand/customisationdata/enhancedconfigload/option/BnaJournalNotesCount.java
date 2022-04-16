package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;


import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;


public enum BnaJournalNotesCount implements ConfigurationOption {
    DEFAULT(0),

    VAULTED_NOTES(1),

    RETURNED_COUNTS(2),

    ALL(3);

    public static final int NUMBER = 44;
    private final int code;
    private final String ndcString;

    BnaJournalNotesCount(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<BnaJournalNotesCount> forCode(int code) {
        switch (code) {
            case 0:
                return DescriptiveOptional.of(DEFAULT);
            case 1:
                return DescriptiveOptional.of(VAULTED_NOTES);
            case 2:
                return DescriptiveOptional.of(RETURNED_COUNTS);
            case 3:
                return DescriptiveOptional.of(ALL);
            default: {
                return DescriptiveOptional.empty(() -> code + " is not a valid 'BNA Journal Notes Count' option code");
            }
        }
    }

    public static DescriptiveOptional<BnaJournalNotesCount> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'BNA Journal Notes Count' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(BnaJournalNotesCount::forCode);
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

}
