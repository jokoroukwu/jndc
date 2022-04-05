package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.util.ByteUtils;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.function.ObjIntConsumer;

import static io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class GenericFitDataFieldAppender extends ChainedNdcComponentAppender<FitBuilder> {
    private final String fieldName;
    private final ObjIntConsumer<FitBuilder> dataConsumer;
    private final int numberOfPairs;

    public GenericFitDataFieldAppender(String fieldName,
                                       ObjIntConsumer<FitBuilder> dataConsumer,
                                       int fieldLength,
                                       NdcComponentAppender<FitBuilder> nextAppender) {
        super(nextAppender);
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.dataConsumer = ObjectUtils.validateNotNull(dataConsumer, "dataConsumer");
        this.numberOfPairs = Integers.validateIsPositive(fieldLength, "fieldLength") / 3;
    }

    public static GenericFitDataFieldAppenderBuilder builder() {
        return new GenericFitDataFieldAppenderBuilder();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        int value = 0;
        for (int i = 0; i < numberOfPairs; i++) {
            final int nextDigitPair = ndcCharBuffer.tryReadInt(3)
                    .filter(ByteUtils::isWithinUnsignedRange, val -> () ->
                            String.format("each digit pair should be in range 0x00-0xFF but found 0x%X at position %d",
                                    val, ndcCharBuffer.position() - 3))
                    .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, fieldName, errorMessage, ndcCharBuffer));
            value = (nextDigitPair << Byte.SIZE) | nextDigitPair;
        }
        dataConsumer.accept(stateObject, value);
        callNextAppender(ndcCharBuffer, stateObject);
    }

    public static final class GenericFitDataFieldAppenderBuilder {
        private String fieldName;
        private ObjIntConsumer<FitBuilder> dataConsumer;
        private int fieldLength;
        private NdcComponentAppender<FitBuilder> nextAppender;

        public GenericFitDataFieldAppenderBuilder withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public GenericFitDataFieldAppenderBuilder withDataConsumer(ObjIntConsumer<FitBuilder> dataConsumer) {
            this.dataConsumer = dataConsumer;
            return this;
        }

        public GenericFitDataFieldAppenderBuilder withFieldLength(int fieldLength) {
            this.fieldLength = fieldLength;
            return this;
        }

        public GenericFitDataFieldAppenderBuilder withNextAppender(NdcComponentAppender<FitBuilder> nextAppender) {
            this.nextAppender = nextAppender;
            return this;
        }

        public GenericFitDataFieldAppender build() {
            return new GenericFitDataFieldAppender(fieldName, dataConsumer, fieldLength, nextAppender);
        }
    }
}
