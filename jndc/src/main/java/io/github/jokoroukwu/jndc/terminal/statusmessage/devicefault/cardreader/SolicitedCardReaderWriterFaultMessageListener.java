package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface SolicitedCardReaderWriterFaultMessageListener {
    void onSolicitedCardReaderWriterFaultMessage(SolicitedStatusMessage<CardReaderWriterFault> deviceFaultMessage);
}
