package io.github.jokoroukwu.jndc.screen;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class ScreenAppender<T extends ScreenAcceptor<?>> extends ChainedNdcComponentAppender<T> {
    public static final String FIELD_NAME = "Screen Number";
    private final String commandName;


    public ScreenAppender(String commandName, NdcComponentAppender<T> nextReader) {
        super(nextReader);
        this.commandName = commandName;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject) {
        ObjectUtils.validateNotNull(ndcCharBuffer, "charBuffer");
        ObjectUtils.validateNotNull(stateObject, "builder");
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));

        if (!ndcCharBuffer.hasNextCharMatching(NdcConstants.GROUP_SEPARATOR)) {
            doAppend(ndcCharBuffer, stateObject);
        } else {
            //  no screen data is present.
            //  proceed to the next field
            callNextAppender(ndcCharBuffer, stateObject);
        }
    }

    private void doAppend(NdcCharBuffer ndcCharBuffer, T builder) {
        final String screenNumber = Screen.readScreenNumber(ndcCharBuffer)
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withMessage(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));

        final StringBuilder screenData = new StringBuilder(50);
        while (ndcCharBuffer.hasRemaining()) {
            final char nextChar = ndcCharBuffer.getCharAt(0);
            if (nextChar == NdcConstants.FIELD_SEPARATOR) {
                break;
            }
            if (nextChar == NdcConstants.GROUP_SEPARATOR) {
                callNextAppender(ndcCharBuffer, builder);
                break;
            }
            screenData.append(nextChar);
            ndcCharBuffer.skip(1);
        }
        builder.withScreen(new Screen(screenNumber, screenData.toString()));
    }
}
