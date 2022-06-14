package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface UnsolicitedCardReaderWriterStatusInfoMessageListener {

    void onUnsolicitedCardReaderWriterStatusInfoMessage(UnsolicitedStatusMessage<CardReaderWriterStatusInfo> message);
}
