package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class ReservedCounterFieldAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersBasicContext> {
    private final FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private final FieldPresenceIndicator fieldPresenceIndicator;
    private final int fieldIndex;
    private final int minLength;
    private final int maxLength;
    private final String fieldName;

    public ReservedCounterFieldAppender(FieldMetaSkipStrategy fieldMetaSkipStrategy,
                                        FieldPresenceIndicator fieldPresenceIndicator,
                                        int fieldIndex,
                                        int minLength,
                                        int maxLength,
                                        ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        super(nextAppender);
        this.fieldMetaSkipStrategy = ObjectUtils.validateNotNull(fieldMetaSkipStrategy, "fieldMetaSkipStrategy");
        this.fieldPresenceIndicator = ObjectUtils.validateNotNull(fieldPresenceIndicator, "fieldPresenceIndicator");
        this.fieldIndex = Integers.validateRange(fieldIndex, 12, 150, "Field index");
        this.fieldName = "g" + fieldIndex;
        this.minLength = Integers.validateIsPositive(minLength, "MinLength");
        this.maxLength = validateMaxLength(minLength, maxLength);
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, SupplyCountersBasicContext stateObject, DeviceConfiguration deviceConfiguration) {
        fieldMetaSkipStrategy.skipFieldMeta(ndcCharBuffer)
                .ifPresent(errorMessage -> onFieldParseError(TerminalState.COMMAND_NAME, fieldName, errorMessage, ndcCharBuffer));

        if (fieldPresenceIndicator.isFieldPresent(ndcCharBuffer, deviceConfiguration)) {
            final String data = readFieldData(ndcCharBuffer);
            stateObject.addReservedField(fieldIndex, data);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private String readFieldData(NdcCharBuffer ndcCharBuffer) {
        final StringBuilder builder = new StringBuilder(Math.max(16, minLength));
        int charsConsumed = 0;
        do {
            builder.append(ndcCharBuffer.readNextChar());
            ++charsConsumed;
        } while (ndcCharBuffer.hasFieldDataRemaining() && shouldProceed(charsConsumed));
        if (charsConsumed < minLength) {
            final String errorMessage = String.format("should be at least %d characters long but was '%s'", minLength,
                    builder);
            throw NdcMessageParseException.withMessage(TerminalState.COMMAND_NAME, fieldName, errorMessage, ndcCharBuffer);
        }
        return builder.toString();
    }

    private boolean shouldProceed(int charsConsumed) {
        return maxLength < 0 || charsConsumed < maxLength;
    }

    private int validateMaxLength(int minLength, int maxLength) {
        if (maxLength < 0 || maxLength >= minLength) {
            return maxLength;
        }
        final String template = "Max length (%d) cannot be zero and should be greater or equal to min length (%d)";
        throw new IllegalArgumentException(String.format(template, maxLength, minLength));
    }
}
