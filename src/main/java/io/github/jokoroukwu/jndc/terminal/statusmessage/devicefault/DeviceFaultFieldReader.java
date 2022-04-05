package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

public abstract class DeviceFaultFieldReader<T> implements ConfigurableNdcComponentReader<T> {

    protected boolean hasFollowingMac(DeviceConfiguration deviceConfiguration, NdcCharBuffer ndcCharBuffer) {
        return deviceConfiguration.isMacEnabled() && ndcCharBuffer.remaining() == 9;
    }

    protected String getCommandName() {
        return TerminalMessageClass.SOLICITED + ": " + TerminalMessageSubClass.STATUS_MESSAGE;
    }
}
