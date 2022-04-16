package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.Fits;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class DecimalisationTableAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PDCTB (Decimalisation Table)";

    public DecimalisationTableAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public DecimalisationTableAppender() {
        this(new EncryptedPinKeyAppender());
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            final int nextDigitPair = ndcCharBuffer.tryReadInt(3)
                    .filter(val -> Fits.isDigitPairValid(val, 0, 9), val -> ()
                            -> String.format("each digit pair should be in range 0x00-0x99 but found 0x%X at position %d",
                            val, ndcCharBuffer.position() - 3))
                    .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
            value = (value << Byte.SIZE) | nextDigitPair;
        }
        stateObject.withDecimalisationTable(value);
        callNextAppender(ndcCharBuffer, stateObject);
    }

    public static boolean isValid(long value) {
        return Fits.isDigitPairValid((int) (value >> 56), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 48) & 0x00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 40) & 0x00_00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 32) & 0x00_00_00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 24) & 0x00_00_00_00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 16) & 0x00_00_00_00_00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) ((value >> 8) & 0x00_00_00_00_00_00_FF), 0, 9) &&
                Fits.isDigitPairValid((int) (value & 0x00_00_00_00_00_00_00_FF), 0, 9);
    }

    public static long validateDecimalisationTable(long value) {
        if (isValid(value)) {
            return value;
        }
        final String errorMessage = "Each '%s' digit should be in range 0x00-0x09 but actual value was 0x%X";
        throw new IllegalArgumentException(String.format(errorMessage, FIELD_NAME, value));
    }
}
