package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;

public interface SolicitedCardReaderWriterStatusInfoMessageListener {
    void onSolicitedCardReaderWriterStatusInfoMessage(SolicitedStatusMessage<CardReaderWriterStatusInfo> message);
}
