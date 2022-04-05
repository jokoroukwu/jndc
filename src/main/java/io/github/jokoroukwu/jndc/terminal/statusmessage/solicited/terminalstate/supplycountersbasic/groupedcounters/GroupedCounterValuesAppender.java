package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.function.BiConsumer;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class GroupedCounterValuesAppender extends ChainedConfigurableNdcComponentAppender<SupplyCountersBasicContext> {
    private final String fieldName;
    private final FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private final FieldPresenceIndicator fieldPresenceIndicator;
    private final ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader;
    private final BiConsumer<GroupedCounterValues, SupplyCountersBasicContext> dataConsumer;

    public GroupedCounterValuesAppender(String fieldName,
                                        ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader,
                                        BiConsumer<GroupedCounterValues, SupplyCountersBasicContext> dataConsumer,
                                        FieldMetaSkipStrategy fieldMetaSkipStrategy,
                                        FieldPresenceIndicator fieldPresenceIndicator,
                                        ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        super(nextAppender);
        this.groupedCountersReader = ObjectUtils.validateNotNull(groupedCountersReader, "groupedCountersReader");
        this.dataConsumer = ObjectUtils.validateNotNull(dataConsumer, "dataConsumer");
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.fieldMetaSkipStrategy = ObjectUtils.validateNotNull(fieldMetaSkipStrategy, "fieldMetaReadingStrategy");
        this.fieldPresenceIndicator = ObjectUtils.validateNotNull(fieldPresenceIndicator, "fieldPresenceIndicator");
    }

    public static GroupedCounterValuesAppenderBuilder builder() {
        return new GroupedCounterValuesAppenderBuilder();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                SupplyCountersBasicContext stateObject,
                                DeviceConfiguration deviceConfiguration) {

        fieldMetaSkipStrategy.skipFieldMeta(ndcCharBuffer)
                .ifPresent(errorMessage -> onFieldParseError(fieldName, TerminalState.COMMAND_NAME, errorMessage, ndcCharBuffer));

        if (fieldPresenceIndicator.isFieldPresent(ndcCharBuffer, deviceConfiguration)) {
            groupedCountersReader.readComponent(ndcCharBuffer, deviceConfiguration)
                    .resolve(value -> dataConsumer.accept(value, stateObject),
                            errorMessage -> onFieldParseError(fieldName, TerminalState.COMMAND_NAME, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
