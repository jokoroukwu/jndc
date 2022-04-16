package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public interface NdcComponentAppenderFactory<K, V> {

    DescriptiveOptional<NdcComponentAppender<V>> getAppender(K value);
}
