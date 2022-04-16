package io.github.jokoroukwu.jndc.terminal.statusmessage.defaultstatusmessage;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface CommandRejectStatusMessageListener {
    void onCommandRejectStatusMessage(SolicitedStatusMessage<SolicitedStatusInformation> commandRejectMessage);
}
