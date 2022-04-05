package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PanDataIndexAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PANDX (PAN Data Index)";

    public PanDataIndexAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public PanDataIndexAppender() {
        this(new PanDataLengthAppender());
    }


    private boolean isValid(int intValue) {
        return intValue >= 0 && intValue <= 0x7F;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(this::isValid, val -> () -> String.format("should be in range  0x00-0x7F but was 0x%X", val))
                .resolve(stateObject::withPanDataIndex,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
