package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface ReadyBStatusMessageListener {

    void onReadyBStatusMessage(SolicitedStatusMessage<ReadyBStatus> readyBStatusMessage);
}
