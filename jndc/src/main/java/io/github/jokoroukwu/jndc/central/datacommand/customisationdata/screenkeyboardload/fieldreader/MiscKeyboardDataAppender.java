package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand.COMMAND_NAME;

public class MiscKeyboardDataAppender extends AbstractScreenKeyboardFieldAppender {
    public static final String MISC_KEYBOARD_DATA_FIELD = "Miscellaneous Keyboard Data";

    public MiscKeyboardDataAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> nextFieldReader) {
        super(nextFieldReader);
    }

    public MiscKeyboardDataAppender() {
        super(null);
    }

    @Override
    protected void readField(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder builder) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoGroupSeparator(COMMAND_NAME, MISC_KEYBOARD_DATA_FIELD,
                        errorMessage, ndcCharBuffer));

        final StringBuilder miscDataBuilder = new StringBuilder();
        while (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasNextCharMatching(NdcConstants.FIELD_SEPARATOR)) {
            miscDataBuilder.append(ndcCharBuffer.readNextChar());
        }
        if (miscDataBuilder.length() > 0) {
            builder.withMiscKeyboardData(miscDataBuilder.toString());
        }
    }
}
