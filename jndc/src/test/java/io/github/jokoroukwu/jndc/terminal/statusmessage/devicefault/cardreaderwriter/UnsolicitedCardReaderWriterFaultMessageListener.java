package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader.CardReaderWriterStatusInfo;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface UnsolicitedCardReaderWriterFaultMessageListener {
    void onUnsolicitedCardReaderWriterStatusMessage(UnsolicitedStatusMessage<CardReaderWriterStatusInfo> deviceFaultMessage);
}
