package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.transactiongroup;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cardreadergroup.CardReaderDataGroupAppender;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class TransactionGroupAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> {

    public TransactionGroupAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        super(nextAppender);
    }

    public TransactionGroupAppender() {
        super(new CardReaderDataGroupAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SupplyCountersExtendedBuilder stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final TransactionCounterGroup transactionCounterGroup = readTransactionCounterGroup(ndcCharBuffer);
        stateObject.withTransactionGroup(transactionCounterGroup);
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private TransactionCounterGroup readTransactionCounterGroup(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipNextChar(TransactionCounterGroup.ID)
                .ifPresent(errorMessage
                        -> onFieldParseError(SupplyCountersExtended.COMMAND_NAME, "'Transaction group id'", errorMessage, ndcCharBuffer));
        final int transactionSerialNumber = ndcCharBuffer.tryReadInt(4)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Transaction Serial Number (TSN)'", errorMessage, ndcCharBuffer));
        final int accumulatedTransactionCount = ndcCharBuffer.tryReadInt(7)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersExtended.COMMAND_NAME, "'Accumulated Transaction Count'", errorMessage, ndcCharBuffer));
        return new TransactionCounterGroup(transactionSerialNumber, accumulatedTransactionCount, null);
    }
}
