package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.field.IntFieldValidationStrategies;
import io.github.jokoroukwu.jndc.field.appender.FixedLengthIntAppender;
import io.github.jokoroukwu.jndc.field.appender.FixedLengthIntAppenderBuilder;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicators;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategies;
import io.github.jokoroukwu.jndc.mac.MacAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters.CoinCountersAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValuesAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValuesAppenderBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFieldAppenderBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasic.COMMAND_NAME;

public class SupplyCountersBasicAppender
        extends ChainedConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> {

    private final SupplyCountersBasicMessageListener messageListener;
    private final ConfigurableNdcComponentAppender<SupplyCountersBasicContext> fieldAppender;

    public SupplyCountersBasicAppender(SupplyCountersBasicMessageListener messageListener,
                                       ConfigurableNdcComponentAppender<SupplyCountersBasicContext> fieldAppender,
                                       ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> nextAppender) {
        super(nextAppender);
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.fieldAppender = ObjectUtils.validateNotNull(fieldAppender, "fieldAppender");
    }

    public SupplyCountersBasicAppender(SupplyCountersBasicMessageListener messageListener) {
        this(messageListener, getDefaultFieldAppender(), new MacAppender<>(COMMAND_NAME));
    }

    private static ConfigurableNdcComponentAppender<SupplyCountersBasicContext> getDefaultFieldAppender() {
        final CoinCountersAppender coinCountersAppender = new CoinCountersAppender();
        final ReservedCounterFieldAppender g50Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(50)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(1)
                .withMaxLength(20)
                .withNextAppender(coinCountersAppender)
                .build();

        final ReservedCounterFieldAppender g40Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(40)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(1)
                //  unlimited
                .withMaxLength(-1)
                .withNextAppender(g50Appender)
                .build();

        final ReservedCounterFieldAppender g30Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(30)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(1)
                .withMaxLength(15)
                .withNextAppender(g40Appender)
                .build();

        final ReservedCounterFieldAppender g20Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(20)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.OPTIONALLY_SKIP_GS)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(1)
                .withMaxLength(15)
                .withNextAppender(g30Appender)
                .build();

        final ReservedCounterFieldAppender g12Appender = new ReservedCounterFieldAppenderBuilder()
                .withFieldIndex(12)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.FIELD_DATA_REMAINING)
                .withMinLength(1)
                .withMaxLength(1)
                .withNextAppender(g20Appender)
                .build();

        final FixedLengthIntAppender<SupplyCountersBasicContext> lastEnvelopeSerialAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(COMMAND_NAME)
                .withFieldName("Last Envelope Serial Number")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldValidationStrategy(IntFieldValidationStrategies.exactValueValidation(0))
                .withFieldLength(5)
                .withDataConsumer((ctx, val) -> { })
                .withNextAppender(g12Appender)
                .build();

        final FixedLengthIntAppender<SupplyCountersBasicContext> cameraFilmRemainingAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(COMMAND_NAME)
                .withFieldName("Camera Film Remaining")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldValidationStrategy(IntFieldValidationStrategies.exactValueValidation(0))
                .withFieldLength(5)
                .withDataConsumer((ctx, val) -> { })
                .withNextAppender(lastEnvelopeSerialAppender)
                .build();

        final FixedLengthIntAppender<SupplyCountersBasicContext> envelopesDepositedAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(COMMAND_NAME)
                .withFieldName("Envelopes Deposited")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldLength(5)
                .withDataConsumer((ctx, val) -> ctx.getCountersBasicBuilder().withEnvelopesDeposited(val))
                .withNextAppender(cameraFilmRemainingAppender)
                .build();

        final FixedLengthIntAppender<SupplyCountersBasicContext> cardsCapturedAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withFieldName("Cards Captured")
                .withCommandName(COMMAND_NAME)
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldLength(5)
                .withDataConsumer((context, value) -> context.getCountersBasicBuilder().withCardsCaptured(value))
                .withNextAppender(envelopesDepositedAppender)
                .build();

        final GroupedCounterValuesAppender lastTransactionNotesDispensedAppender = new GroupedCounterValuesAppenderBuilder()
                .withFieldName("Last Transaction Notes Dispensed")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withDataConsumer((data, context) -> context.getCountersBasicBuilder().withLastTransactionNotesDispensed(data))
                .withNextAppender(cardsCapturedAppender)
                .build();

        final GroupedCounterValuesAppender notesDispensedAppender = new GroupedCounterValuesAppenderBuilder()
                .withFieldName("Notes Dispensed")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withDataConsumer((data, context) -> context.getCountersBasicBuilder().withNotesDispensed(data))
                .withNextAppender(lastTransactionNotesDispensedAppender)
                .build();

        final GroupedCounterValuesAppender notesRejectedAppender = new GroupedCounterValuesAppenderBuilder()
                .withFieldName("Notes Rejected")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withDataConsumer((data, context) -> context.getCountersBasicBuilder().withNotesRejected(data))
                .withNextAppender(notesDispensedAppender)
                .build();

        final GroupedCounterValuesAppender notesInCassettesAppender = new GroupedCounterValuesAppenderBuilder()
                .withFieldName("Notes In Cassettes")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withDataConsumer((data, context) -> context.getCountersBasicBuilder().withNotesInCassettes(data))
                .withNextAppender(notesRejectedAppender)
                .build();

        final FixedLengthIntAppender<SupplyCountersBasicContext> accumulatedTransactionCountAppender
                = new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(COMMAND_NAME)
                .withFieldName("Accumulated Transaction Count")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldLength(7)
                .withDataConsumer((context, data) -> context.getCountersBasicBuilder().withAccumulatedTransactionCount(data))
                .withNextAppender(notesInCassettesAppender)
                .build();

        return new FixedLengthIntAppenderBuilder<SupplyCountersBasicContext>()
                .withCommandName(COMMAND_NAME)
                .withFieldName("Transaction Serial Number (TSN)")
                .withFieldMetaSkipStrategy(FieldMetaSkipStrategies.NO_META)
                .withFieldPresenceIndicator(FieldPresenceIndicators.MANDATORY)
                .withFieldLength(4)
                .withDataConsumer((context, data) -> context.getCountersBasicBuilder().withTransactionSerialNumber(data))
                .withNextAppender(accumulatedTransactionCountAppender)
                .build();
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SolicitedStatusMessageBuilder<SolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {
        final SupplyCountersBasicContext supplyCountersContext = new SupplyCountersBasicContext();
        fieldAppender.appendComponent(ndcCharBuffer, supplyCountersContext, deviceConfiguration);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
        stateObject.withStatusInformation(supplyCountersContext.buildWithNoValidation());
        final SolicitedStatusMessage<? extends SolicitedStatusInformation> message =
                stateObject.withStatusInformation(supplyCountersContext.buildWithNoValidation())
                        .build();
        @SuppressWarnings("unchecked") final SolicitedStatusMessage<SupplyCountersBasic> suppliedCountersBasicMessage
                = (SolicitedStatusMessage<SupplyCountersBasic>) message;
        messageListener.onSolicitedSupplyCountersBasicMessage(suppliedCountersBasicMessage);
    }
}
