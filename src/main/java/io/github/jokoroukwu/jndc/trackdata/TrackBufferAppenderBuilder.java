package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.function.BiConsumer;

public class TrackBufferAppenderBuilder<T> {
    private String fieldName;
    private String commandName;
    private FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private FieldPresenceIndicator fieldPresenceIndicator;
    private NdcComponentReader<DescriptiveOptional<String>> trackDataReader;
    private BiConsumer<T, String> dataConsumer;
    private ConfigurableNdcComponentAppender<T> nextAppender;

    public TrackBufferAppenderBuilder<T> withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withCommandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withFieldMetaSkipStrategy(FieldMetaSkipStrategy fieldMetaSkipStrategy) {
        this.fieldMetaSkipStrategy = fieldMetaSkipStrategy;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withFieldPresenceIndicator(FieldPresenceIndicator fieldPresenceIndicator) {
        this.fieldPresenceIndicator = fieldPresenceIndicator;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withTrackDataReader(NdcComponentReader<DescriptiveOptional<String>> trackDataReader) {
        this.trackDataReader = trackDataReader;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withDataConsumer(BiConsumer<T, String> dataConsumer) {
        this.dataConsumer = dataConsumer;
        return this;
    }

    public TrackBufferAppenderBuilder<T> withNextAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public TrackBufferAppender<T> build() {
        return new TrackBufferAppender<T>(
                commandName,
                fieldName,
                fieldMetaSkipStrategy,
                fieldPresenceIndicator,
                trackDataReader,
                dataConsumer,
                nextAppender);
    }
}
