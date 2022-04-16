package io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.NextStateIdAppender;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Longs;

import java.util.function.Supplier;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withFieldName;

public class TimeVariantSequenceNumberAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    public static final String FIELD_NAME = "Message Sequence/Time Variant Number";

    public TimeVariantSequenceNumberAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender) {
        super(nextAppender);
    }

    public TimeVariantSequenceNumberAppender() {
        super(new NextStateIdAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(FIELD_NAME, errorMessage, ndcCharBuffer));
        //  field may be empty
        if (ndcCharBuffer.hasFieldDataRemaining()) {

            final String threeFirstChars = ndcCharBuffer.tryReadCharSequence(3)
                    .getOrThrow(errorMessage -> withFieldName(FIELD_NAME, errorMessage, ndcCharBuffer));

            final SequenceTimeVariantNumber sequenceTimeVariantNumber = ndcCharBuffer.hasFieldDataRemaining()
                    ? readTimeVariantNumber(threeFirstChars, ndcCharBuffer)
                    : readSequenceNumber(threeFirstChars, ndcCharBuffer);
            stateObject.withSequenceTimeVariantNumber(sequenceTimeVariantNumber);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private SequenceTimeVariantNumber readTimeVariantNumber(String threeFirstChars, NdcCharBuffer ndcCharBuffer) {
        //  time variant number has length of 8 bytes
        return ndcCharBuffer.tryReadCharSequence(5)
                .map(threeFirstChars::concat)
                .flatMapToLong(Longs::tryParseHexLong)
                .filter(Longs::isNotNegative, this::onNegativeNumber)
                .mapToObject(TimeVariantNumber::new)
                .getOrThrow(errorMessage -> withFieldName("'Time Variant Number'", errorMessage, ndcCharBuffer));
    }

    private SequenceTimeVariantNumber readSequenceNumber(String threeFirstChars, NdcCharBuffer ndcCharBuffer) {
        return Integers.tryParseInt(threeFirstChars)
                .filter(Integers::isNotNegative, this::onNegativeNumber)
                .mapToObject(SequenceNumber::new)
                .getOrThrow(errorMessage -> withFieldName("'Message Sequence Number'", errorMessage, ndcCharBuffer));
    }

    private Supplier<String> onNegativeNumber(long value) {
        return () -> "value cannot be negative but was " + value;
    }
}
