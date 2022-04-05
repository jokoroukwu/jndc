package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface UnsolicitedCardReaderWriterFaultMessageListener {

    void onUnsolicitedCardReaderWriterStatusMessage(UnsolicitedStatusMessage<CardReaderWriterFault> deviceFaultMessage);
}
