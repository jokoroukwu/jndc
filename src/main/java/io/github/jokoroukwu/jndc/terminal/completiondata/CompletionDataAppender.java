package io.github.jokoroukwu.jndc.terminal.completiondata;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.Result;

import java.util.Optional;

public class CompletionDataAppender<T extends CompletionDataAcceptor<?>> extends ChainedConfigurableNdcComponentAppender<T> {
    private final String commandName;
    private final NdcComponentReader<Optional<CompletionData>> completionDataReader;

    public CompletionDataAppender(String commandName, NdcComponentReader<Optional<CompletionData>> completionDataReader,
                                  ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
        this.completionDataReader = completionDataReader;
    }

    public CompletionDataAppender(String commandName, ConfigurableNdcComponentAppender<T> nextAppender) {
        this(commandName, CompletionDataReader.DEFAULT, nextAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        Result.of(() -> completionDataReader.readComponent(ndcCharBuffer))
                .getOrThrow(ex -> new NdcMessageParseException(commandName + ": " + ex.getMessage()))
                .ifPresent(stateObject::withCompletionData);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
