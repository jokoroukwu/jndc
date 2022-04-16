package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.function.BiConsumer;


public class TrackDataAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {
    private final String commandName;
    private final String fieldName;
    private final FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private final FieldPresenceIndicator fieldPresenceIndicator;
    private final NdcComponentReader<DescriptiveOptional<String>> trackDataReader;
    private final BiConsumer<T, String> trackDataConsumer;

    public TrackDataAppender(String commandName,
                             String fieldName,
                             FieldMetaSkipStrategy fieldMetaSkipStrategy,
                             FieldPresenceIndicator fieldPresenceIndicator,
                             NdcComponentReader<DescriptiveOptional<String>> trackDataReader,
                             BiConsumer<T, String> trackDataConsumer,
                             ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "'Command Name'");
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "'Field Name'");
        this.fieldMetaSkipStrategy = ObjectUtils.validateNotNull(fieldMetaSkipStrategy, "fieldMetaSkipStrategy");
        this.fieldPresenceIndicator = ObjectUtils.validateNotNull(fieldPresenceIndicator, "fieldPresenceIndicator");
        this.trackDataReader = ObjectUtils.validateNotNull(trackDataReader, "Track Data Reader");
        this.trackDataConsumer = ObjectUtils.validateNotNull(trackDataConsumer, "Track Data Consumer");
    }

    public static <T> TrackDataAppenderBuilder<T> builder() {
        return new TrackDataAppenderBuilder<>();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        fieldMetaSkipStrategy.skipFieldMeta(ndcCharBuffer)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));

        if (fieldPresenceIndicator.isFieldPresent(ndcCharBuffer, deviceConfiguration)) {
            trackDataReader.readComponent(ndcCharBuffer)
                    .resolve(data -> trackDataConsumer.accept(stateObject, data),
                            errorMessage -> NdcMessageParseException.onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
