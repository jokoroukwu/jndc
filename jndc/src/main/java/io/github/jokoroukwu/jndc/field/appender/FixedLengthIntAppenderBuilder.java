package io.github.jokoroukwu.jndc.field.appender;

import io.github.jokoroukwu.jndc.field.IntFieldValidationStrategies;
import io.github.jokoroukwu.jndc.field.IntFieldValidationStrategy;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;

import java.util.function.ObjIntConsumer;

public final class FixedLengthIntAppenderBuilder<T> {
    private FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private FieldPresenceIndicator fieldPresenceIndicator;
    private String commandName;
    private String fieldName;
    private ObjIntConsumer<T> dataConsumer;
    private IntFieldValidationStrategy fieldValidationStrategy = IntFieldValidationStrategies.NO_VALIDATION;
    private int fieldLength;
    private int radix = 10;
    private ConfigurableNdcComponentAppender<T> nextAppender;

    public FixedLengthIntAppenderBuilder<T> withFieldMetaSkipStrategy(FieldMetaSkipStrategy fieldMetaSkipStrategy) {
        this.fieldMetaSkipStrategy = fieldMetaSkipStrategy;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withFieldPresenceIndicator(FieldPresenceIndicator fieldPresenceIndicator) {
        this.fieldPresenceIndicator = fieldPresenceIndicator;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withCommandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withDataConsumer(ObjIntConsumer<T> dataConsumer) {
        this.dataConsumer = dataConsumer;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withFieldValidationStrategy(IntFieldValidationStrategy fieldValidationStrategy) {
        this.fieldValidationStrategy = fieldValidationStrategy;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withRadix(int radix) {
        this.radix = radix;
        return this;
    }

    public FixedLengthIntAppenderBuilder<T> withNextAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public FixedLengthIntAppender<T> build() {
        return new FixedLengthIntAppender<>(fieldMetaSkipStrategy,
                fieldPresenceIndicator,
                commandName,
                fieldName,
                dataConsumer,
                fieldValidationStrategy,
                fieldLength,
                radix,
                nextAppender);
    }
}
