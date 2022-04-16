package io.github.jokoroukwu.jndc.genericbuffer;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.function.BiConsumer;

public class GenericBufferAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {
    private final BiConsumer<T, GenericBuffer> bufferConsumer;

    public GenericBufferAppender(BiConsumer<T, GenericBuffer> bufferConsumer, ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.bufferConsumer = ObjectUtils.validateNotNull(bufferConsumer, "Buffer consumer");
    }

    public GenericBufferAppender(BiConsumer<T, GenericBuffer> bufferConsumer) {
        this(bufferConsumer, null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), "buffer ID",
                        errorMessage, ndcCharBuffer));

        final char id = readBufferId(ndcCharBuffer);
        final String bufferData = readBufferData(ndcCharBuffer);

        bufferConsumer.accept(stateObject, new GenericBuffer(id, bufferData));
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private char readBufferId(NdcCharBuffer ndcCharBuffer) {
        return (char) ndcCharBuffer.tryReadNextChar()
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(),
                        "buffer ID", errorMessage, ndcCharBuffer))
                .get();
    }

    private String readBufferData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());
        return builder.toString();
    }
}
