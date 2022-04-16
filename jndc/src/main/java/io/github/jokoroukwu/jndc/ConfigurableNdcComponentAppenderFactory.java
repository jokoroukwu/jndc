package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;

import java.util.Optional;

public interface ConfigurableNdcComponentAppenderFactory<K, V> {

    Optional<ConfigurableNdcComponentAppender<V>> getAppender(K key);
}
