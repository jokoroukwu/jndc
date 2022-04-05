package io.github.jokoroukwu.jndc;

import java.util.regex.Pattern;

public enum PatternValidatorBase implements PatternValidator {
    INSTANCE;

    public static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("[a-zA-Z\\d]+");
    public static final Pattern HEX_PATTERN = Pattern.compile("[a-fA-F\\d]+");
    public static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+");

    @Override
    public boolean isAlphanumeric(String value) {
        return value != null && ALPHANUMERIC_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean isHex(String value) {
        return value != null && HEX_PATTERN.matcher(value).matches();
    }

    public String validateIsHex(String value, String fieldName) {
        if (isHex(value)) {
            return value;
        }
        throw new IllegalArgumentException(String.format("'%s' should be hexadecimal but was: %s", fieldName, value));
    }

    @Override
    public boolean isDecimal(String value) {
        return value != null && DECIMAL_PATTERN.matcher(value).matches();
    }
}
