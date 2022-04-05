package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;

public abstract class IdentifiableBufferAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {

    public IdentifiableBufferAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
    }

    protected void skipBufferMeta(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(getCommandName(), getFieldName() + " ID", errorMessage, ndcCharBuffer));

        ndcCharBuffer.tryReadNextCharMatching(getBufferId())
                .ifEmpty(errorMessage
                        -> NdcMessageParseException.onFieldParseError(getCommandName(), getFieldName() + " ID", errorMessage, ndcCharBuffer));
    }

    protected abstract char getBufferId();

    protected abstract String getFieldName();

    protected abstract String getCommandName();
}
