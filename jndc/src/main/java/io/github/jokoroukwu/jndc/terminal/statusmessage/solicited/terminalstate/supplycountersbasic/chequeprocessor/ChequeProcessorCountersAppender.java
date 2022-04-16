package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.chequeprocessor;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.field.appender.FixedLengthIntAppenderBuilder;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasic;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppenderBuilder;

import java.util.ArrayList;
import java.util.Collections;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class ChequeProcessorCountersAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersBasicContext> {
    public static final String FIELD_NAME = "Cheque processor cheques deposited";

    public ChequeProcessorCountersAppender(ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        super(nextAppender);
    }

    public ChequeProcessorCountersAppender() {
        super(getDefaultNextAppender());
    }

    private static ConfigurableNdcComponentAppender<SupplyCountersBasicContext> getDefaultNextAppender() {
        final ConfigurableNdcComponentAppender<SupplyCountersBasicContext> passBooksCapturedAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(SupplyCountersBasic.COMMAND_NAME)
                .withFieldName("Number of passbooks captured")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.PRESENT_UNLESS_FS_FOLLOWS)
                .withFieldLength(5)
                .withDataConsumer((builder, data) -> builder.getCountersBasicBuilder().withNumberOfPassbooksCaptured(data))
                .build();
        final ReservedCounterFieldAppender g150Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(150)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(passBooksCapturedAppender)
                .build();

        final ReservedCounterFieldAppender g140Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(140)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(g150Appender)
                .build();

        return new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(130)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(g140Appender)
                .build();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersBasicContext stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasFollowingFieldSeparator()) {
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> onNoGroupSeparator(SupplyCountersBasic.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
            final ChequeProcessorCounters chequeProcessorCounters = readChequeProcessorCounters(ndcCharBuffer);
            stateObject.getCountersBasicBuilder().withChequeProcessorCounters(chequeProcessorCounters);
        }

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private ChequeProcessorCounters readChequeProcessorCounters(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return ChequeProcessorCounters.EMPTY;
        }
        final ArrayList<Integer> counters = new ArrayList<>();
        int binNumber = 1;
        do {
            final int nextBinCounter = readBinCounterValue(ndcCharBuffer, binNumber);
            counters.add(nextBinCounter);
        } while (ndcCharBuffer.hasFieldDataRemaining());
        counters.trimToSize();
        return new ChequeProcessorCounters(Collections.unmodifiableList(counters));
    }

    private int readBinCounterValue(NdcCharBuffer ndcCharBuffer, int binNumber) {
        return ndcCharBuffer.tryReadInt(5)
                .getOrThrow(errorMessage
                        -> withMessage(SupplyCountersBasic.COMMAND_NAME, "BIN " + binNumber + FIELD_NAME, errorMessage, ndcCharBuffer));
    }


}
