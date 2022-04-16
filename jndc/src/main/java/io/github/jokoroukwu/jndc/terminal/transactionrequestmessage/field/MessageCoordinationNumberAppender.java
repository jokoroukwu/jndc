package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.MessageCoordinationNumberAcceptor;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.Chars;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class MessageCoordinationNumberAppender<T extends MessageCoordinationNumberAcceptor<?>> extends ChainedConfigurableNdcComponentAppender<T> {
    public static final String FIELD_NAME = "'Message Coordination Number'";
    private final String commandName;
    private final boolean withFieldSeparator;

    public MessageCoordinationNumberAppender(String commandName, boolean withFieldSeparator, ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "Command Name");
        this.withFieldSeparator = withFieldSeparator;
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        if (withFieldSeparator) {
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));
        }
        ndcCharBuffer.tryReadNextChar()
                .filter(value -> Chars.isInRange((char) value, '1', '~'), number -> ()
                        -> String.format("should be within range 0x31-0x7E but was: 0x%X", number))
                .resolve(number -> stateObject.withMessageCoordinationNumber((char) number),
                        errorMessage -> NdcMessageParseException.onFieldParseError(commandName, FIELD_NAME, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

}
