package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;

public interface TerminalAcceptableAidsTableMessageListener {

    void onTerminalAcceptableAidsTableMessage(EmvConfigurationMessage<IccTerminalAcceptableAppIdsTable> message);
}
