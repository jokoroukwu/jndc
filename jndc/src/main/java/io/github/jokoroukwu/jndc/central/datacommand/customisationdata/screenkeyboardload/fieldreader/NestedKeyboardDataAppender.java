package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;

public class NestedKeyboardDataAppender extends ChainedNdcComponentAppender<ScreenKeyboardEntryBuilder> {
    public static final String NESTED_KEYBOARD_DATA_FIELD = "Nested Keyboard Data";

    public NestedKeyboardDataAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> nextFieldReader) {
        super(nextFieldReader);
    }

    public NestedKeyboardDataAppender() {
        super(new MiscKeyboardDataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder stateObject) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> onNoGroupSeparator(COMMAND_NAME, NESTED_KEYBOARD_DATA_FIELD, errorMessage, ndcCharBuffer));

        if (!ndcCharBuffer.hasRemaining()) {
            return;
        }

        final char nextChar = ndcCharBuffer.getCharAt(0);
        if (nextChar == NdcConstants.FIELD_SEPARATOR) {
            return;
        }
        if (nextChar == NdcConstants.GROUP_SEPARATOR) {
            callNextAppender(ndcCharBuffer, stateObject);
            return;
        }
        ndcCharBuffer.tryReadCharSequence(3)
                .resolve(stateObject::withNestedKeyboardData, errorMessage -> NdcMessageParseException.onFieldParseError(COMMAND_NAME,
                        NESTED_KEYBOARD_DATA_FIELD, errorMessage, ndcCharBuffer));

        if (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasNextCharMatching(NdcConstants.FIELD_SEPARATOR)) {
            callNextAppender(ndcCharBuffer, stateObject);
        }
    }
}
