package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure;

import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface PowerFailureMessageListener {

    void onPowerFailureMessage(UnsolicitedStatusMessage<PowerFailure> message);
}
