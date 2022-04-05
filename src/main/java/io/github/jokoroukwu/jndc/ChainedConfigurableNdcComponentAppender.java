package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

public abstract class ChainedConfigurableNdcComponentAppender<T> implements ConfigurableNdcComponentAppender<T> {
    private final ConfigurableNdcComponentAppender<T> nextAppender;

    public ChainedConfigurableNdcComponentAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        this.nextAppender = nextAppender;
    }

    protected final void callNextAppender(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        if (nextAppender != null) {
            nextAppender.appendComponent(ndcCharBuffer, stateObject, deviceConfiguration);
        }
    }

    protected void callNextAppenderIfDataRemains(NdcCharBuffer ndcCharBuffer, T collector, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining()) {
            callNextAppender(ndcCharBuffer, collector, deviceConfiguration);
        }
    }
}
