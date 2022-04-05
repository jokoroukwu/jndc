package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcCharBuffer;

public interface ConfigurableNdcComponentAppender<T> {

    void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration);
}
