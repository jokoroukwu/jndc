package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcCharBuffer;

public interface ConfigurableNdcComponentReader<T> {

    T readComponent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration);
}
