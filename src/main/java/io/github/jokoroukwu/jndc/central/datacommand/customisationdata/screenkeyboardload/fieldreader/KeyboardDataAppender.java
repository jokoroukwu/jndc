package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.fieldreader;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.HashMap;
import java.util.Map;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;

public class KeyboardDataAppender extends ChainedNdcComponentAppender<ScreenKeyboardEntryBuilder> {
    public static final String NUMBER_AND_DATA_FIELD = "Keyboard Number and Keyboard Data";

    public KeyboardDataAppender(NdcComponentAppender<ScreenKeyboardEntryBuilder> nextFieldReader) {
        super(nextFieldReader);
    }

    public KeyboardDataAppender() {
        this(new TouchScreenDataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, ScreenKeyboardEntryBuilder stateObject) {
        ndcCharBuffer.trySkipGroupSeparator()
                .ifPresent(errorMessage -> onNoGroupSeparator(COMMAND_NAME, NUMBER_AND_DATA_FIELD, errorMessage, ndcCharBuffer));

        if (ndcCharBuffer.hasNextCharMatching(NdcConstants.GROUP_SEPARATOR)) {
            //  field is not present
            //  proceed to the next field
            callNextAppender(ndcCharBuffer, stateObject);
            return;
        }
        //  not followed by a group separator.
        //  means it should have at least keyboard number
        final int keyboardNumber = ndcCharBuffer.tryReadInt(3)
                .ifEmpty(errorMessage -> onFieldParseError(COMMAND_NAME, "Keyboard Number", errorMessage, ndcCharBuffer))
                .get();

        final Map<Integer, Integer> dataEntries = new HashMap<>();
        int dataEntryIndex = 0;
        while (ndcCharBuffer.hasRemaining()) {
            final char nextChar = ndcCharBuffer.getCharAt(0);
            if (nextChar == NdcConstants.FIELD_SEPARATOR) {
                break;
            }
            if (nextChar == NdcConstants.GROUP_SEPARATOR) {
                callNextAppender(ndcCharBuffer, stateObject);
                break;
            }
            readDataEntry(ndcCharBuffer, dataEntries, dataEntryIndex++);
        }

        stateObject.withKeyboardData(new KeyboardData(keyboardNumber, Map.copyOf(dataEntries)));
    }


    private void readDataEntry(NdcCharBuffer ndcCharBuffer, Map<Integer, Integer> dataEntries, int entryIndex) {
        final int keyPosition = ndcCharBuffer.tryReadInt(2)
                .filter(this::isKeyPositionValid, value -> ()
                        -> String.format("value '%d' is not within valid range (17-24 decimal)", value))
                .ifEmpty(errorMessage -> onFieldParseError(COMMAND_NAME,
                        String.format("Keyboard Data entry %d: Key position", entryIndex), errorMessage, ndcCharBuffer))
                .get();

        final int keyCode = ndcCharBuffer.tryReadInt(2, 16)
                .filter(this::isKeyCodeValid, value ->
                        () -> String.format("value '%d' is not within valid range (F3-FA hex)", value))
                .ifEmpty(errorMessage -> onFieldParseError(COMMAND_NAME,
                        String.format("Keyboard Data entry %d: Key code", entryIndex), errorMessage, ndcCharBuffer))
                .get();
        dataEntries.put(keyPosition, keyCode);
    }

    private boolean isKeyPositionValid(int value) {
        return value >= 17 && value <= 24;
    }

    private boolean isKeyCodeValid(int value) {
        return value >= 0xF3 && value <= 0xFA;
    }
}
