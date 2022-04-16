package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;

public interface TerminalDataObjectsTableMessageListener {

    void onTerminalDataObjectsTableMessage(EmvConfigurationMessage<IccTerminalDataObjectsTable> emvConfigurationMessage);
}
