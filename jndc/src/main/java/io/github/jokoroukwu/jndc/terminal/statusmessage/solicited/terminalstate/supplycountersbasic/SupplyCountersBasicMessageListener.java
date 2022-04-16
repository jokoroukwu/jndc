package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface SupplyCountersBasicMessageListener {

    void onSolicitedSupplyCountersBasicMessage(SolicitedStatusMessage<SupplyCountersBasic> message);
}
