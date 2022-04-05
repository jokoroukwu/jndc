package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand.COMMAND_NAME;

public class TouchScreenDataAppender extends ChainedNdcComponentAppender<ScreenKeyboardEntryBuilder> {
    public static final String TOUCH_SCREEN_DATA_FIELD = "Touch Screen Data";

    public TouchScreenDataAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> nextAppender) {
        super(nextAppender);
    }

    public TouchScreenDataAppender() {
        super(new NestedKeyboardDataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder stateObject) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoGroupSeparator(COMMAND_NAME, TOUCH_SCREEN_DATA_FIELD,
                        errorMessage, ndcCharBuffer));

        var touchScreenData = new StringBuilder(32);
        while (ndcCharBuffer.hasRemaining()) {
            final char nextChar = ndcCharBuffer.getCharAt(0);
            if (nextChar == NdcConstants.FIELD_SEPARATOR) {
                break;
            }
            if (nextChar == NdcConstants.GROUP_SEPARATOR) {
                //  proceed to the next field
                callNextAppender(ndcCharBuffer, stateObject);
                break;
            }
            touchScreenData.append(nextChar);
            ndcCharBuffer.skip(1);
        }
        //  touch screen data is considered present if at least a single char has been acquired
        if (touchScreenData.length() > 0) {
            stateObject.withTouchScreenData(touchScreenData.toString());
        }
    }
}
