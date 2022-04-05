package io.github.jokoroukwu.jndc.field.appender;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.field.IntFieldValidationStrategy;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.function.ObjIntConsumer;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class FixedLengthIntAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {
    private final FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private final FieldPresenceIndicator fieldPresenceIndicator;
    private final String commandName;
    private final String fieldName;
    private final ObjIntConsumer<T> dataConsumer;
    private final IntFieldValidationStrategy fieldValidationStrategy;
    private final int fieldLength;
    private final int radix;

    public FixedLengthIntAppender(FieldMetaSkipStrategy fieldMetaSkipStrategy,
                                  FieldPresenceIndicator fieldPresenceIndicator,
                                  String commandName,
                                  String fieldName,
                                  ObjIntConsumer<T> dataConsumer,
                                  IntFieldValidationStrategy fieldValidationStrategy, int fieldLength,
                                  int radix,
                                  ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.fieldMetaSkipStrategy = ObjectUtils.validateNotNull(fieldMetaSkipStrategy, "fieldMetaSkipStrategy");
        this.fieldPresenceIndicator = ObjectUtils.validateNotNull(fieldPresenceIndicator, "fieldPresenceIndicator");
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.dataConsumer = ObjectUtils.validateNotNull(dataConsumer, "dataConsumer");
        this.fieldValidationStrategy = ObjectUtils.validateNotNull(fieldValidationStrategy, "fieldValidationStrategy");
        this.fieldLength = Integers.validateMinValue(fieldLength, 1, "fieldLength");
        this.radix = Integers.validateNotNegative(radix, "radix");
    }

    public static <T> FixedLengthIntAppenderBuilder<T> builder() {
        return new FixedLengthIntAppenderBuilder<>();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        fieldMetaSkipStrategy.skipFieldMeta(ndcCharBuffer)
                .ifPresent(errorMessage -> onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));

        if (fieldPresenceIndicator.isFieldPresent(ndcCharBuffer, deviceConfiguration)) {
            ndcCharBuffer.tryReadInt(fieldLength, radix)
                    .ifPresent(value -> fieldValidationStrategy.validateFieldValue(value)
                            .ifPresent(errorMessage -> onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer)))
                    .resolve(value -> dataConsumer.accept(stateObject, value),
                            errorMessage -> onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }


}
