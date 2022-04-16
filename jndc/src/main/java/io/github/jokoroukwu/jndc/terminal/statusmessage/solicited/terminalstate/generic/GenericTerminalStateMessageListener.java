package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.generic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface GenericTerminalStateMessageListener {

    void onGenericTerminalStateMessage(SolicitedStatusMessage<GenericTerminalState> message);
}
