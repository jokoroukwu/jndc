package io.github.jokoroukwu.jndc;

import java.util.Optional;

public interface NdcMessageProcessorFactory<K> {

    Optional<NdcMessagePreProcessor> getMessageProcessor(K key);
}
