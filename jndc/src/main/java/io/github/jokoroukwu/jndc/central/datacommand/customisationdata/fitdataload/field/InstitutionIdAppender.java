package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class InstitutionIdAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PFIID (Institution ID)";

    public InstitutionIdAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public InstitutionIdAppender() {
        this(new IndirectNextStateIndexAppender());
    }

    public static long validateInstitutionId(long value) {
        if (areDigitsValid(value)) {
            return value;
        }
        final String message = "each digit should be equal to 0xF or be in range 0x00-0x09 but actual value is 0x%X";
        throw new IllegalArgumentException(String.format(message, value));
    }

    private static boolean isDigitWithingRange(long value) {
        return value == 0xF || (value <= 0x09);
    }

    private static boolean areDigitsValid(long value) {
        if (!isDigitWithingRange(value & 0xF)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF0) >> 4)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF00) >> 8)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF000) >> 12)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF0000) >> 16)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF00000) >> 20)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF000000) >> 24)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF0000000L) >> 28)) {
            return false;
        }
        if (!isDigitWithingRange((value & 0xF00000000L) >> 32)) {
            return false;
        }
        return isDigitWithingRange((value & 0xF000000000L) >> 36);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        long value = 0;
        for (int i = 0; i < 5; i++) {
            final int nextDigitPair = ndcCharBuffer.tryReadInt(3)
                    .filter(val -> isDigitWithingRange(val >> 4),
                            val -> () -> getErrorMessage(val, ndcCharBuffer.position() - 3))
                    .filter(val -> isDigitWithingRange(val & 0b00001111),
                            val -> () -> getErrorMessage(val, ndcCharBuffer.position() - 3))
                    .getOrThrow(errorMessage -> withMessage(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

            value = (value << Byte.SIZE) | nextDigitPair;
        }
        stateObject.withInstitutionId(value);
        callNextAppender(ndcCharBuffer, stateObject);
    }

    private String getErrorMessage(int value, int position) {
        return String.format("each digit should be equal to 0xF or be in range 0x00-0x09 but found 0x%X at position %d",
                value, position);
    }

}
