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

public class TrackBufferAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {
    private final String commandName;
    private final String fieldName;
    private final FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private final FieldPresenceIndicator fieldPresenceIndicator;
    private final NdcComponentReader<DescriptiveOptional<String>> trackDataReader;
    private final BiConsumer<T, String> dataConsumer;

    public TrackBufferAppender(String commandName,
                               String fieldName,
                               FieldMetaSkipStrategy fieldMetaSkipStrategy,
                               FieldPresenceIndicator fieldPresenceIndicator,
                               NdcComponentReader<DescriptiveOptional<String>> trackDataReader,
                               BiConsumer<T, String> dataConsumer,
                               ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.fieldMetaSkipStrategy = ObjectUtils.validateNotNull(fieldMetaSkipStrategy, "fieldMetaSkipStrategy");
        this.fieldPresenceIndicator = ObjectUtils.validateNotNull(fieldPresenceIndicator, "fieldPresenceIndicator");
        this.trackDataReader = ObjectUtils.validateNotNull(trackDataReader, "Track data reader");
        this.dataConsumer = ObjectUtils.validateNotNull(dataConsumer, "Data consumer");
    }

    public static <T> TrackBufferAppenderBuilder<T> builder() {
        return new TrackBufferAppenderBuilder<>();

    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        fieldMetaSkipStrategy.skipFieldMeta(ndcCharBuffer)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));
        if (fieldPresenceIndicator.isFieldPresent(ndcCharBuffer, deviceConfiguration)) {
            //  skip buffer id
            ndcCharBuffer.skip(1);
            trackDataReader.readComponent(ndcCharBuffer)
                    .resolve(data -> dataConsumer.accept(stateObject, data),
                            errorMessage -> NdcMessageParseException.onFieldParseError(commandName, fieldName, errorMessage, ndcCharBuffer));
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
