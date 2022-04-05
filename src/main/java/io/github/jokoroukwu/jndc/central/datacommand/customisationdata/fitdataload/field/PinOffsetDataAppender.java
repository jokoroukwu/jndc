package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PinOffsetDataAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "POFDX (PIN Offset Data)";

    protected PinOffsetDataAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PinOffsetDataAppender() {
        this(new DecimalisationTableAppender());
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(this::isValid, val -> () -> String.format("should be 0xFF or be in range 0x00-0x7F but was 0x%X", val))
                .resolve(stateObject::withPinOffsetData,
                        errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }

    private boolean isValid(int value) {
        return value == 0xFF || (value >= 0 && value <= 0x7F);
    }
}
