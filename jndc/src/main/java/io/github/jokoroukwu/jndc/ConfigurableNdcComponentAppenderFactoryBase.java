package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;

public final class ConfigurableNdcComponentAppenderFactoryBase<K, V> implements ConfigurableNdcComponentAppenderFactory<K, V> {
    private final Map<K, ConfigurableNdcComponentAppender<V>> appenderMap;

    public ConfigurableNdcComponentAppenderFactoryBase(Map<K, ConfigurableNdcComponentAppender<V>> appenderMap) {
        this.appenderMap = ObjectUtils.validateNotNull(appenderMap, "appenderMap");
    }

    @Override
    public Optional<ConfigurableNdcComponentAppender<V>> getAppender(K key) {
        return Optional.ofNullable(appenderMap.get(key));
    }
}
