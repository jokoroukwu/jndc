package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class IndexReferencePointAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PINDX (Index Reference Point)";

    protected IndexReferencePointAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public IndexReferencePointAppender() {
        this(new LanguageCodeIndexAppender());
    }

    public static int validateValue(int value) {
        final boolean isValid = isWithinRange(value >> 20)
                && isWithinRange((value >> 16) & 0x0F)
                && isWithinRange((value >> 12) & 0x00F)
                && isWithinRange((value >> 8) & 0x00_0F)
                && isWithinRange((value >> 4) & 0x00_00F)
                && isWithinRange(value & 0x00_00_0F);
        if (isValid) {
            return value;
        }
        final String errorMessage = String.format("Each '%s' digit pair should be in range 0x00-0xAA but actual value was: 0x%X",
                FIELD_NAME, value);
        throw new IllegalArgumentException(errorMessage);
    }

    private static boolean isWithinRange(int digit) {
        return digit <= 0xA;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        int value = 0;
        for (int i = 0; i < 3; i++) {
            final int nextDigitPair = ndcCharBuffer.tryReadInt(3)
                    .filter(this::areDigitsValid, val -> ()
                            -> String.format("each digit pair should be in range 0x00-0xAA but found 0x%X at position %d",
                            val, ndcCharBuffer.position() - 3))
                    .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
            value = (value << Byte.SIZE) | nextDigitPair;
        }

        stateObject.withIndexReferencePoint(value);
        callNextAppender(ndcCharBuffer, stateObject);
    }

    private boolean areDigitsValid(int digitPair) {
        return isWithinRange(digitPair >> 4) && isWithinRange(digitPair & 0b00001111);
    }
}
