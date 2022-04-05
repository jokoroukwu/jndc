package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.function.BiConsumer;

public final class TrackDataAppenderBuilder<T> {
    private String commandName;
    private String fieldName;
    private FieldMetaSkipStrategy fieldMetaSkipStrategy;
    private FieldPresenceIndicator fieldPresenceIndicator;
    private NdcComponentReader<DescriptiveOptional<String>> trackDataReader;
    private BiConsumer<T, String> trackDataConsumer;
    private ConfigurableNdcComponentAppender<T> nextAppender;

    public TrackDataAppenderBuilder<T> withCommandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public TrackDataAppenderBuilder<T> withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public TrackDataAppenderBuilder<T> withFieldMetaSkipStrategy(FieldMetaSkipStrategy fieldMetaSkipStrategy) {
        this.fieldMetaSkipStrategy = fieldMetaSkipStrategy;
        return this;
    }

    public TrackDataAppenderBuilder<T> withFieldPresenceIndicator(FieldPresenceIndicator fieldPresenceIndicator) {
        this.fieldPresenceIndicator = fieldPresenceIndicator;
        return this;
    }

    public TrackDataAppenderBuilder<T> withTrackDataConsumer(BiConsumer<T, String> trackDataConsumer) {
        this.trackDataConsumer = trackDataConsumer;
        return this;
    }

    public TrackDataAppenderBuilder<T> withNextAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public TrackDataAppenderBuilder<T> withTrackDataReader(NdcComponentReader<DescriptiveOptional<String>> trackDataReader) {
        this.trackDataReader = trackDataReader;
        return this;
    }


    public TrackDataAppender<T> build() {
        return new TrackDataAppender<>(commandName,
                fieldName,
                fieldMetaSkipStrategy,
                fieldPresenceIndicator,
                trackDataReader,
                trackDataConsumer,
                nextAppender);
    }
}
