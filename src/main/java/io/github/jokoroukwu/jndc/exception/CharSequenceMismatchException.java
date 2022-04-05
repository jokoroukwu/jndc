package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class CharSequenceMismatchException extends NdcMessageParseException {
    public CharSequenceMismatchException(String message, Object... args) {
        super(Strings.format(message, args));
    }

}
