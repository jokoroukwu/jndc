package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface UnsolicitedGenericDeviceFaultMessageListener {

    void onUnsolicitedGenericDeviceFaultMessage(UnsolicitedStatusMessage<GenericDeviceFault> unsolicitedStatusMessage);

}
