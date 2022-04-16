package io.github.jokoroukwu.jndc.terminal.statusmessage.generic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface GenericSolicitedStatusMessageListener {
    void onSolicitedGenericStatusMessage(SolicitedStatusMessage<GenericSolicitedStatusInformation> genericStatusMessage);
}
