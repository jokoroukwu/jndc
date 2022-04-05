package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class AdditionalTrack2DataAppender implements NdcComponentAppender<TerminalApplicationIdEntryBuilder> {
    public static final String FIELD_NAME = "Additional Track 2 Data";
    private static final String lengthField = "Length of Additional Track 2 Data";

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        final Track2ICCData track2ICCData = stateObject.getTrackTwoIccData();
        //  Additional Track 2 Data is mandatory when Track 2 ICC Data is 3 or 4.
        //  Otherwise it should be omitted
        if (track2ICCData == Track2ICCData.USE_TAG_OR_SIMULATE || track2ICCData == Track2ICCData.SIMULATE) {
            final int length = readLength(ndcCharBuffer);
            final String data = readData(ndcCharBuffer, length);
            stateObject.withAdditionalTrackTwoData(data);
        }
    }

    private int readLength(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadHexInt(2)
                .filter(value -> value > 0 && value <= 33,
                        value -> () -> "length should be in range 1-33 characters but was" + value)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, lengthField, errorMessage, ndcCharBuffer));
    }

    private String readData(NdcCharBuffer ndcCharBuffer, int charCount) {
        return ndcCharBuffer.tryReadCharSequence(charCount)
                .filter(value -> Strings.isWithinCharRange(value, '0', '9'),
                        value -> () -> String.format("should be within character range '0'-'9' but was %s", value))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
    }
}
