package io.github.jokoroukwu.jndc.field.fieldindicator;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;

public interface FieldPresenceIndicator {

    boolean isFieldPresent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration);

}
