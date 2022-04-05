package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

public abstract class DeviceFaultFieldAppender<T> extends ChainedConfigurableNdcComponentAppender<T> {
    public DeviceFaultFieldAppender(ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
    }

    protected boolean hasFollowingMac(DeviceConfiguration deviceConfiguration, NdcCharBuffer ndcCharBuffer) {
        return deviceConfiguration.isMacEnabled() && ndcCharBuffer.remaining() <= 9;
    }

}
