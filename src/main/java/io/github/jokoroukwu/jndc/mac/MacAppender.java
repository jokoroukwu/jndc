package io.github.jokoroukwu.jndc.mac;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class MacAppender<T extends MacAcceptor<?>> extends ChainedConfigurableNdcComponentAppender<T> {
    private final String commandName;
    private final MacReader macReader;

    public MacAppender(String commandName, MacReader macReader) {
        super(null);
        this.commandName = ObjectUtils.validateNotNull(commandName, "Command Name");
        this.macReader = ObjectUtils.validateNotNull(macReader, "MAC reader");
    }

    public MacAppender(String commandName) {
        this(commandName, MacReaderBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration.isMacEnabled()) {
            macReader.tryReadMac(ndcCharBuffer)
                    .resolve(stateObject::withMac, errorMessage
                            -> onFieldParseError(commandName, MacReader.FIELD_NAME, errorMessage, ndcCharBuffer));
        }
        if (ndcCharBuffer.hasRemaining()) {
            throw NdcMessageParseException.withMessage(commandName, MacReader.FIELD_NAME,
                    String.format("unexpected MAC data: '%s'", ndcCharBuffer.subBuffer()), ndcCharBuffer);
        }
    }
}
