package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class LanguageCodeIndexAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PLNDX (Language Code Index)";

    protected LanguageCodeIndexAppender(NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
    }

    public LanguageCodeIndexAppender() {
        this(GenericFitDataFieldAppender.builder()
                .withFieldName("PMMSR (MM Sensor Flag)")
                .withFieldLength(3)
                .withDataConsumer(FitBuilder::withMmSensorFlag)
                .withNextAppender(GenericFitDataFieldAppender.builder()
                        .withFieldName("Reserved")
                        .withFieldLength(9)
                        .withDataConsumer(FitBuilder::withReservedField)
                        .withNextAppender(new PinBlockFormatAppender())
                        .build()
                ).build());
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        ndcCharBuffer.tryReadInt(3)
                .filter(this::isValid, val -> () -> String.format("should be in range 0x00-0x7F but was 0x%X", val))
                .resolve(stateObject::withLanguageCodeIndex,
                        errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject);
    }

    private boolean isValid(int value) {
        return value >= 0 && value <= 0x7F;
    }
}
