package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;


public enum ChequeProcessingModule implements ConfigurationOption {

    DEFAULT(0),

    ALL_BINS(1),

    INCLUDE_LOCATION(2),

    SCAN_ENDORSED(3);

    public static final int NUMBER = 83;

    private final int code;
    private final String ndcString;

    ChequeProcessingModule(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<ChequeProcessingModule> forCode(int code) {
        switch (code) {
            case 0:
                return DescriptiveOptional.of(DEFAULT);
            case 1:
                return DescriptiveOptional.of(ALL_BINS);
            case 2:
                return DescriptiveOptional.of(INCLUDE_LOCATION);
            case 3:
                return DescriptiveOptional.of(SCAN_ENDORSED);
            default: {
                return DescriptiveOptional.empty(()
                        -> String.format("value '%s' is not a valid 'Cheque Processing Module' option code", code));
            }
        }
    }

    public static DescriptiveOptional<ChequeProcessingModule> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Cheque Processing Module' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(ChequeProcessingModule::forCode);
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
