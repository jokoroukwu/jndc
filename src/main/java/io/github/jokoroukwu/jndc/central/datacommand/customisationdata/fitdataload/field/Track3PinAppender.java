package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class Track3PinAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PRCNT (Track 3 PIN)";

    protected Track3PinAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public Track3PinAppender() {
        this(new PinOffsetDataAppender());
    }

    public static int validateTrack3PinRetryCount(int value) {
        if (isValid(value)) {
            return value;
        }
        throw new IllegalArgumentException(String.format("%s %s", FIELD_NAME, getErrorMessage(value)));
    }

    private static boolean isValid(int value) {
        return (value >= 0 && value <= 0x7E) || (value >= 0x80 && value <= 0xFE);
    }

    private static String getErrorMessage(int value) {
        return String.format("should be in range 0x00-0x7E for 'MINTS' counting method or 0x80-0xFE for 'ISO' but was 0x%X", value);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(Track3PinAppender::isValid, val -> () -> getErrorMessage(val))
                .resolve(stateObject::withTrack3PinRetryCount,
                        errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
