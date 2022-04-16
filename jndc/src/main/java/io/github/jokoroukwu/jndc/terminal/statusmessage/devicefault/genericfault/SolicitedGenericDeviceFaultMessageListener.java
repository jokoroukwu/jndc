package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface SolicitedGenericDeviceFaultMessageListener {
    void onSolicitedGenericDeviceFaultMessage(SolicitedStatusMessage<GenericDeviceFault> genericDeviceFaultMessage);
}
