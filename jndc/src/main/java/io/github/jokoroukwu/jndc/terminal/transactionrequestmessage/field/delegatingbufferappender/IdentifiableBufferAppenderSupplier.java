package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;

import java.util.Optional;

public interface IdentifiableBufferAppenderSupplier<T> {

    Optional<ConfigurableNdcComponentAppender<T>> getBufferAppender(char bufferId);
}
