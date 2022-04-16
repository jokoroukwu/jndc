package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PinBlockFormatAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PBFMT (PIN Block Format)";

    public PinBlockFormatAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PinBlockFormatAppender() {
        super(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(this::isValid, val -> () -> String.format("should be in range 0x00-0x05 but was 0x%X", val))
                .resolve(stateObject::withPinBlockFormat,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

    }

    private boolean isValid(int value) {
        return value >= 0 && value <= 0x05;
    }

}
