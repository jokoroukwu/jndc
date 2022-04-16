package io.github.jokoroukwu.jndc;

public interface PatternValidator {

    boolean isAlphanumeric(String value);

    boolean isHex(String value);

    boolean isDecimal(String value);
}
