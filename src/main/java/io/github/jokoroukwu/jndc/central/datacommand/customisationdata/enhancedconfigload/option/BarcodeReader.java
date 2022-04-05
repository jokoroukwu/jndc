package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;


public enum BarcodeReader implements ConfigurationOption {
    ON(0),

    OFF(1);

    public static final int NUMBER = 48;
    private final int code;
    private final String ndcString;

    BarcodeReader(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<BarcodeReader> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(ON);
        }
        if (code == 1) {
            return DescriptiveOptional.of(OFF);
        }
        return DescriptiveOptional.empty(() -> code + " is not a valid c");
    }

    public static DescriptiveOptional<BarcodeReader> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Barcode reader' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(BarcodeReader::forCode);
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
