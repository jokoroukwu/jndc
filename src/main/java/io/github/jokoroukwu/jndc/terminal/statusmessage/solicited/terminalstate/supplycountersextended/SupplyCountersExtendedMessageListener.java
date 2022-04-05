package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface SupplyCountersExtendedMessageListener {

    void onSupplyCountersExtendedMessage(SolicitedStatusMessage<SupplyCountersExtended> supplyCountersExtendedMessage);
}
