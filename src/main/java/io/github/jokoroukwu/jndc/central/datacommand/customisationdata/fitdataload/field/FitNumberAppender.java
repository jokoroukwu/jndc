package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;

public class FitNumberAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "FIT Number";

    public FitNumberAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public FitNumberAppender() {
        super(new InstitutionIdIndexAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
        ndcCharBuffer.tryReadInt(3)
                .resolve(stateObject::withFitNumber,
                        errorMessage -> onFieldParseError(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject);
    }
}
