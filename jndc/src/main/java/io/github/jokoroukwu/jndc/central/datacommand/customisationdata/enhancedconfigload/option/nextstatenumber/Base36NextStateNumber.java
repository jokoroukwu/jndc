package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.nextstatenumber;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class Base36NextStateNumber extends NextStateNumber {
    public static final int RESERVED_VALUE = 0xAD9;
    public static final int MAX_VALUE = 0xB63F;
    public static final int MIN_VALUE = 0;

    Base36NextStateNumber(int code) {
        super(code);
    }

    public static DescriptiveOptional<NextStateNumber> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Next State Number' option code cannot be null")
                .flatMapToInt(value -> Integers.tryParseInt(value, 36))
                .flatMapToObject(Base36NextStateNumber::forCode);
    }

    public static DescriptiveOptional<NextStateNumber> forCode(int code) {
        if (isCodeValid(code)) {
            return DescriptiveOptional.of(new Base36NextStateNumber(code));
        }
        final String errorMessage = "value '%d' is not within valid 'Next State Number' option code range (000-254 or 256-ZZZ base36)";
        return DescriptiveOptional.empty(() -> String.format(errorMessage, code));
    }

    public static boolean isCodeValid(int code) {
        return code != RESERVED_VALUE && (code >= MIN_VALUE && code <= MAX_VALUE);
    }


    @Override
    public String toNdcString() {
        return String.format("%02d", NUMBER) +
                Strings.leftPad(Integer.toString(getCode(), 36).toUpperCase(), "0", 3);
    }
}
