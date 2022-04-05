package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessage;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.BufferB;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.generalpurpose.GenericBufferAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.LastTransactionStatusDataAppender;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.TrackBufferAppender;
import io.github.jokoroukwu.jndc.trackdata.TrackDataReader;
import io.github.jokoroukwu.jndc.util.Longs;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class AmountEntryFieldAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Amount Entry Field";

    public AmountEntryFieldAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public AmountEntryFieldAppender() {
        super(defaultAppender());
    }

    private static ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> defaultAppender() {
        final TrackBufferAppender<TransactionRequestMessageBuilder> track1BufferAppender = TrackBufferAppender.<TransactionRequestMessageBuilder>builder()
                .withCommandName(TransactionRequestMessage.COMMAND_NAME)
                .withFieldName("Track 1 data Buffer")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_FS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withTrackDataReader(new TrackDataReader(Track1DataBuffer.MAX_DATA_LENGTH, ' ', '_'))
                .withDataConsumer((builder, data) -> builder.withTrack1Data(Track1DataBuffer.requestBuffer(data)))
                .withNextAppender(new LastTransactionStatusDataAppender())
                .build();

        final GenericBufferAppender bufferCAppender = GenericBufferAppender.builder()
                .withFieldName("General Purpose Buffer C")
                .withDataConsumer(TransactionRequestMessageBuilder::withBufferC)
                .withNextAppender(track1BufferAppender)
                .build();

        final GenericBufferAppender bufferBAppender = GenericBufferAppender.builder()
                .withFieldName("General Purpose Buffer B")
                .withDataConsumer((builder, value) -> {
                    if (!value.isEmpty()) builder.withBufferB(new BufferB(value));
                })
                .withMinDataLength(3)
                .withNextAppender(bufferCAppender)
                .build();

        return GenericBufferAppender.builder()
                .withFieldName("PIN Buffer (Buffer A)")
                .withMinDataLength(16)
                .withDataConsumer(TransactionRequestMessageBuilder::withPinBufferA)
                .withNextAppender(bufferBAppender)
                .build();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer));

        final AmountEntry amountEntryField = readAmountEntry(ndcCharBuffer);
        stateObject.withAmountEntry(amountEntryField);
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private AmountEntry readAmountEntry(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return null;
        }
        final String eightDigits = ndcCharBuffer.tryReadCharSequence(8)
                .getOrThrow(errorMessage
                        -> withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer));

        if (ndcCharBuffer.hasFieldDataRemaining()) {
            //  amount entry field is 12 characters long
            return ndcCharBuffer.tryReadCharSequence(4)
                    .map(eightDigits::concat)
                    .flatMapToLong(Longs::tryParseLong)
                    .mapToObject(value -> new ExtendedAmountEntry(value, null))
                    .getOrThrow(errorMessage
                            -> NdcMessageParseException.withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer));

        }
        //  amount entry field is 8 characters long
        return Longs.tryParseLong(eightDigits)
                .mapToObject(value -> new BaseAmountEntry(value, null))
                .getOrThrow(errorMessage
                        -> NdcMessageParseException.withMessage(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), FIELD_NAME, errorMessage, ndcCharBuffer));
    }

}
