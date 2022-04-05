package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.nextstatenumber;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public final class DecimalNextStateNumber extends NextStateNumber {
    public static final DecimalNextStateNumber DEFAULT = new DecimalNextStateNumber(0);
    public static final int RESERVED_VALUE = 255;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 999;

    DecimalNextStateNumber(int code) {
        super(code);
    }

    public static DescriptiveOptional<NextStateNumber> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Next State Number' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt)
                .flatMapToObject(DecimalNextStateNumber::forCode);
    }

    public static DescriptiveOptional<NextStateNumber> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new DecimalNextStateNumber(code));
        }
        final String errorMessage = "value '%d' is not within valid 'Next State Number' option code range (0-254 or 256-999 decimal)";
        return DescriptiveOptional.empty(() -> String.format(errorMessage, code));
    }

    public static boolean isCodeValid(int code) {
        return code != RESERVED_VALUE && (code >= MIN_VALUE && code <= MAX_VALUE);
    }

    @Override
    public String toNdcString() {
        return String.format("%02d%03d", NUMBER, getCode());
    }
}
