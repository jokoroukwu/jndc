package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class NdcException extends RuntimeException {

    public NdcException(Throwable cause, String message, Object... args) {
        super(Strings.format(message, args), cause);
    }

    public NdcException(String message, Object... args) {
        super(Strings.format(message, args));
    }
}
