package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.delegatingbufferappender;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;

public class BufferAppenderSupplierBase<T> implements IdentifiableBufferAppenderSupplier<T> {
    private final Map<Character, ConfigurableNdcComponentAppender<T>> appenderMap;
    private final ConfigurableNdcComponentAppender<T> defaultAppender;

    public BufferAppenderSupplierBase(Map<Character, ConfigurableNdcComponentAppender<T>> appenderMap,
                                      ConfigurableNdcComponentAppender<T> defaultAppender) {
        this.appenderMap = ObjectUtils.validateNotNull(appenderMap, "appenderMap cannot be null");
        this.defaultAppender = ObjectUtils.validateNotNull(defaultAppender, "defaultAppender cannot be null");
    }

    @Override
    public Optional<ConfigurableNdcComponentAppender<T>> getBufferAppender(char bufferId) {
        return Optional.of(appenderMap.getOrDefault(bufferId, defaultAppender));
    }
}
