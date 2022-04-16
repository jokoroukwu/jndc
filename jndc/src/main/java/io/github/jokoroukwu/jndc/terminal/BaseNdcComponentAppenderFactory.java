package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcComponentAppenderFactory;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Map;

public class BaseNdcComponentAppenderFactory<K, T> implements NdcComponentAppenderFactory<K, T> {
    private final Map<K, NdcComponentAppender<T>> appenderMap;

    public BaseNdcComponentAppenderFactory(Map<K, NdcComponentAppender<T>> appenderMap) {
        this.appenderMap = ObjectUtils.validateNotNull(appenderMap, "appenderMap cannot be null");
    }

    @Override
    public DescriptiveOptional<NdcComponentAppender<T>> getAppender(K value) {
        return DescriptiveOptional.ofNullable(appenderMap.get(value), () -> "No Appender for value: " + value);
    }
}
