package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PinPadAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PINPD (PIN Pad)";

    public PinPadAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PinPadAppender() {
        super(new PanDataIndexAppender());
    }

    private boolean isValid(int intValue) {
        return intValue >= 0 && intValue <= 0xCF;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(this::isValid, val -> () -> String.format("should be in range 0x00-0xCF but was 0x%X", val))
                .resolve(stateObject::withPinPad,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
