package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class IncompleteNdcMessageException extends NdcMessageParseException {

    public IncompleteNdcMessageException(String message, Object... args) {
        super(Strings.format(message, args));
    }
}
