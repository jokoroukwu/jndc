package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class GroupedCounterValuesAppenderBuilder {
    private String fieldName;
    private FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private FieldPresenceIndicator fieldPresenceIndicator;
    private BiConsumer<GroupedCounterValues, SupplyCountersBasicContext> dataConsumer;
    private ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender;
    private ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader = GroupedCountersReader.INSTANCE;

    public GroupedCounterValuesAppenderBuilder withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public GroupedCounterValuesAppenderBuilder withFieldMetaSkipStrategy(FieldMetaSkipStrategy fieldMetaSkipStrategy) {
        this.fieldMetaSkipStrategy = fieldMetaSkipStrategy;
        return this;
    }

    public GroupedCounterValuesAppenderBuilder withFieldPresenceIndicator(FieldPresenceIndicator fieldPresenceIndicator) {
        this.fieldPresenceIndicator = fieldPresenceIndicator;
        return this;
    }

    public GroupedCounterValuesAppenderBuilder withDataConsumer(BiConsumer<GroupedCounterValues, SupplyCountersBasicContext> dataConsumer) {
        this.dataConsumer = dataConsumer;
        return this;
    }

    public GroupedCounterValuesAppenderBuilder withNextAppender(ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public GroupedCounterValuesAppenderBuilder withGroupedCountersReader(ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReader) {
        this.groupedCountersReader = groupedCountersReader;
        return this;
    }

    public GroupedCounterValuesAppender build() {
        return new GroupedCounterValuesAppender(fieldName,
                groupedCountersReader,
                dataConsumer,
                fieldMetaSkipStrategy,
                fieldPresenceIndicator,
                nextAppender);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GroupedCounterValuesAppenderBuilder.class.getSimpleName() + ": {", "}")
                .add("fieldName: '" + fieldName + '\'')
                .add("fieldMetaSkipStrategy: " + fieldMetaSkipStrategy)
                .add("fieldPresenceIndicator: " + fieldPresenceIndicator)
                .add("dataConsumer: " + dataConsumer)
                .add("nextAppender: " + nextAppender)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupedCounterValuesAppenderBuilder that = (GroupedCounterValuesAppenderBuilder) o;
        return Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(fieldMetaSkipStrategy, that.fieldMetaSkipStrategy) &&
                Objects.equals(fieldPresenceIndicator, that.fieldPresenceIndicator) &&
                Objects.equals(dataConsumer, that.dataConsumer) &&
                Objects.equals(nextAppender, that.nextAppender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldMetaSkipStrategy, fieldPresenceIndicator, dataConsumer, nextAppender);
    }
}
