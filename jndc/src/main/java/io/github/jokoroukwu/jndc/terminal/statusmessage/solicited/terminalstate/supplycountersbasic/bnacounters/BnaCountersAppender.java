package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.chequeprocessor.ChequeProcessorCountersAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppenderBuilder;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onNoGroupSeparator;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class BnaCountersAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersBasicContext> {
    public static final String FIELD_NAME = "BNA Counters";

    public BnaCountersAppender(ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        super(nextAppender);
    }

    public BnaCountersAppender() {
        super(getDefaultNextAppender());
    }

    private static ConfigurableNdcComponentAppender<SupplyCountersBasicContext> getDefaultNextAppender() {
        final ReservedCounterFieldAppender g110FieldAppender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(110)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(new ChequeProcessorCountersAppender())
                .build();

        final ReservedCounterFieldAppender g100FieldAppender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(100)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(g110FieldAppender)
                .build();

        final ReservedCounterFieldAppender g90FieldAppender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(90)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(g100FieldAppender)
                .build();

        return new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(80)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(5)
                .withMaxLength(5)
                .withNextAppender(g90FieldAppender)
                .build();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersBasicContext stateObject, DeviceConfiguration deviceConfiguration) {
        if (!ndcCharBuffer.hasFollowingFieldSeparator()) {
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> onNoGroupSeparator(TerminalState.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
            stateObject.getCountersBasicBuilder()
                    .withBnaCounters(readBnaCounters(ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private BnaCounters readBnaCounters(NdcCharBuffer ndcCharBuffer) {
        //  the field may be omitted
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return null;
        }
        final int notesRefunded = readValue(ndcCharBuffer, "'Notes Refunded'");
        final int notesRejected = readValue(ndcCharBuffer, "'Notes Rejected'");
        final int notesEncashed = readValue(ndcCharBuffer, "'Notes Encashed'");
        final int notesEscrowed = readValue(ndcCharBuffer, "'Notes Escrowed'");
        return new BnaCounters(notesRefunded, notesRejected, notesEncashed, notesEscrowed, null);
    }

    private int readValue(NdcCharBuffer buffer, String fieldName) {
        return buffer.tryReadInt(5)
                .getOrThrow(errorMessage -> withMessage(TerminalState.COMMAND_NAME, fieldName, errorMessage, buffer));
    }
}
