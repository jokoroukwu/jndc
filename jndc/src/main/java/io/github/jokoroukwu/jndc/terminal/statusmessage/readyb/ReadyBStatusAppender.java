package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.SupplyModeReadyStatusAmountLength;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.Cassette;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.TransactionData;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.TransactionDataReader;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import java.util.Optional;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoFieldSeparator;

public class ReadyBStatusAppender
        implements ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final ReadyBStatusMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender;
    private final NdcComponentReader<TransactionData<? extends Cassette>> transactionDataReader;
    private final NdcComponentReader<Optional<CompletionData>> completionDataReader;

    public ReadyBStatusAppender(ReadyBStatusMessageListener messageListener,
                                NdcComponentReader<TransactionData<? extends Cassette>> transactionDataReader,
                                NdcComponentReader<Optional<CompletionData>> completionDataReader,
                                ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppender) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.transactionDataReader = ObjectUtils.validateNotNull(transactionDataReader, "transactionDataReader");
        this.completionDataReader = ObjectUtils.validateNotNull(completionDataReader, "completionDataReader");
        this.macAppender = ObjectUtils.validateNotNull(macAppender, "macAppender");
    }

    public ReadyBStatusAppender(ReadyBStatusMessageListener messageListener) {
        this(messageListener, new TransactionDataReader(), CompletionDataReader.DEFAULT, new MacAppender<>(ReadyBStatus.COMMAND_NAME));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        Result.of(() -> completionDataReader.readComponent(ndcCharBuffer))
                .getOrThrow(ex -> new NdcMessageParseException(ReadyBStatus.COMMAND_NAME + ": " + ex.getMessage()))
                .ifPresent(stateObject::withCompletionData);

        final ReadyBStatus readyBStatus = readReadyBStatus(ndcCharBuffer, deviceConfiguration);
        stateObject.withStatusInformation(readyBStatus);

        macAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);

        final SolicitedStatusMessage<? extends SolicitedStatusInformation> solicitedStatusMessage = stateObject.build();
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<ReadyBStatus> readyBMessage
                = (SolicitedStatusMessage<ReadyBStatus>) solicitedStatusMessage;

        messageListener.onReadyBStatusMessage(readyBMessage);
    }

    private ReadyBStatus readReadyBStatus(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        final ConfigurationOption readyStatusOption = deviceConfiguration.getConfigurationOptions()
                .getOption(SupplyModeReadyStatusAmountLength.NUMBER)
                .orElse(SupplyModeReadyStatusAmountLength.DEFAULT);
        //  the field is completely omitted
        //  when transaction status data is not configured
        //  via the corresponding option
        if (SupplyModeReadyStatusAmountLength.isTransactionStatusDataIncluded(readyStatusOption.getCode())) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> onNoFieldSeparator(ReadyBStatus.COMMAND_NAME, "Status Information'", errorMessage, ndcCharBuffer));

            final int transactionSerialNumber = ndcCharBuffer.tryReadInt(4)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(ReadyBStatus.COMMAND_NAME,
                            "'Transaction Serial Number (TSN)'", errorMessage, ndcCharBuffer));

            final TransactionData<? extends Cassette> transactionData = readTransactionData(ndcCharBuffer);
            return new ReadyBStatus(transactionSerialNumber, transactionData, null);
        }
        return null;
    }

    private TransactionData<? extends Cassette> readTransactionData(NdcCharBuffer ndcCharBuffer) {
        //  disambiguate MAC and Transaction Data fields
        if (ndcCharBuffer.remaining() > 9) {
            return transactionDataReader.readComponent(ndcCharBuffer);
        }
        return TransactionData.EMPTY;
    }
}
