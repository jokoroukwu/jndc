package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.trackdata.TrackDataAppender;
import io.github.jokoroukwu.jndc.trackdata.TrackDataReader;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public class TopOfReceiptTransactionFlagAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Top of Receipt Transaction Flag";

    public TopOfReceiptTransactionFlagAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public TopOfReceiptTransactionFlagAppender() {
        super(defaultAppender());
    }

    private static ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> defaultAppender() {
        return new MessageCoordinationNumberAppender<>(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), false,
                TrackDataAppender.<TransactionRequestMessageBuilder>builder()
                        .withCommandName(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                        .withFieldName("Track 2 Data")
                        .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.ALWAYS_SKIP_FS)
                        .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                        .withTrackDataReader(new TrackDataReader(39, '0', '?'))
                        .withTrackDataConsumer(TransactionRequestMessageBuilder::withTrack2Data)
                        .withNextAppender(TrackDataAppender.<TransactionRequestMessageBuilder>builder()
                                .withCommandName(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                                .withFieldName("Track 3 Data")
                                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.ALWAYS_SKIP_FS)
                                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                                .withTrackDataReader(new TrackDataReader(106, '0', '?'))
                                .withTrackDataConsumer(TransactionRequestMessageBuilder::withTrack3Data)
                                .withNextAppender(new OperationCodeAppender())
                                .build())
                        .build());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        FIELD_NAME, errorMessage, ndcCharBuffer));

        ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(this::toBooleanFlag)
                .resolve(stateObject::withTopOfReceipt,
                        errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                                FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private DescriptiveOptional<Boolean> toBooleanFlag(int flag) {
        if (flag == '1') {
            return DescriptiveOptional.of(Boolean.TRUE);
        }
        if (flag == '0') {
            return DescriptiveOptional.of(Boolean.FALSE);
        }
        return DescriptiveOptional.empty(() -> String.format("'%s' should be '0' or '1' but was: '%c'", FIELD_NAME, flag));
    }

}
