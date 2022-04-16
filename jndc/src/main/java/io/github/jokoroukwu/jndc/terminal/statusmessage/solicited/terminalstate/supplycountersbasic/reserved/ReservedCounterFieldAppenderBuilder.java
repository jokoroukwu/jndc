package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;

import java.util.Objects;
import java.util.StringJoiner;

public class ReservedCounterFieldAppenderBuilder {
    private int fieldIndex;
    private int minLength;
    private int maxLength;
    private FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private FieldPresenceIndicator fieldPresenceIndicator;
    private ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender;

    public ReservedCounterFieldAppenderBuilder withFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
        return this;
    }

    public ReservedCounterFieldAppenderBuilder withMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public ReservedCounterFieldAppenderBuilder withMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public ReservedCounterFieldAppenderBuilder withFieldMetaSkipStrategy(FieldMetaSkipStrategy fieldMetaSkipStrategy) {
        this.fieldMetaSkipStrategy = fieldMetaSkipStrategy;
        return this;
    }

    public ReservedCounterFieldAppenderBuilder withFieldPresenceIndicator(FieldPresenceIndicator fieldPresenceIndicator) {
        this.fieldPresenceIndicator = fieldPresenceIndicator;
        return this;
    }

    public ReservedCounterFieldAppenderBuilder withNextAppender(ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public ReservedCounterFieldAppender build() {
        return new ReservedCounterFieldAppender(fieldMetaSkipStrategy, fieldPresenceIndicator, fieldIndex, minLength, maxLength, nextAppender);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ReservedCounterFieldAppenderBuilder.class.getSimpleName() + ": {", "}")
                .add("fieldIndex: " + fieldIndex)
                .add("minLength: " + minLength)
                .add("maxLength: " + maxLength)
                .add("fieldMetaSkipStrategy: " + fieldMetaSkipStrategy)
                .add("fieldPresenceIndicator: " + fieldPresenceIndicator)
                .add("nextAppender: " + nextAppender)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedCounterFieldAppenderBuilder that = (ReservedCounterFieldAppenderBuilder) o;
        return fieldIndex == that.fieldIndex &&
                minLength == that.minLength &&
                maxLength == that.maxLength &&
                Objects.equals(fieldMetaSkipStrategy, that.fieldMetaSkipStrategy) &&
                Objects.equals(fieldPresenceIndicator, that.fieldPresenceIndicator) &&
                Objects.equals(nextAppender, that.nextAppender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldIndex, minLength, maxLength, fieldMetaSkipStrategy, fieldPresenceIndicator, nextAppender);
    }
}
