package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.util.ByteUtils;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class IndirectNextStateIndexAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PSTDX (Indirect Next State Index)";

    public IndirectNextStateIndexAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public IndirectNextStateIndexAppender() {
        super(new AlgorithmBankIdAppender());
    }

    private static boolean isFirstDigitValid(int value) {
        return (value >> 4) <= 0xF;
    }

    private static boolean isSecondDigitValid(int value) {
        return (value & 0b00001111) <= 0x0E;
    }

    public static int validateNextStateIndex(int value) {
        ByteUtils.validateIsWithinUnsignedRange(value, FIELD_NAME);
        if (!isFirstDigitValid(value)) {
            final String message = String.format("%s first digit should be in range 0x0-0xF but actual value is 0x%X",
                    FIELD_NAME, value);
            throw new IllegalArgumentException(message);
        }
        if (!isSecondDigitValid(value)) {
            final String message = String.format("%s second digit should be in range 0x00-0x0E but actual value is 0x%X",
                    FIELD_NAME, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(IndirectNextStateIndexAppender::isFirstDigitValid, val -> ()
                        -> String.format("first digit should be in range 0x0-0xF but was 0x%X", val))
                .filter(IndirectNextStateIndexAppender::isSecondDigitValid, val -> ()
                        -> String.format("second digit should be in range 0x00-0x0E but was 0x%X", val))
                .resolve(stateObject::withIndirectNextStateIndex,
                        errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject);
    }
}
