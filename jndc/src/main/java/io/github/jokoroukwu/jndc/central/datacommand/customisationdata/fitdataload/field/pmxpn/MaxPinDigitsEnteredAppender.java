package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pmxpn;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field.pckln.MaxPinDigitsCheckedAppender;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class MaxPinDigitsEnteredAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PMXPN (Maximum PIN Digits Entered)";

    public MaxPinDigitsEnteredAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public MaxPinDigitsEnteredAppender() {
        this(new MaxPinDigitsCheckedAppender());
    }

    private boolean isPinLengthValid(int value) {
        return value >= 0x04 && value <= 0x10;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        final int value = ndcCharBuffer.tryReadInt(3)
                .getOrThrow(errorMessage -> withMessage(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        final int pinLength = value & 0b00111111;
        if (!isPinLengthValid(pinLength)) {
            final String errorMessage = String.format("'PIN length' should be in range 0x04-0x10 but was 0x%X", pinLength);
            throw withMessage(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer);
        }

        PinBlockType.forValue(value >> 6)
                .map(val -> new MaxPinDigitsEntered(val, pinLength, null))
                .resolve(stateObject::withMaxPinDigitsEntered,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }

}
