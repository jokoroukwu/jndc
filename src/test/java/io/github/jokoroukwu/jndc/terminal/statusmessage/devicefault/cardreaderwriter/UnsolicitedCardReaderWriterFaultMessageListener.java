package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface UnsolicitedCardReaderWriterFaultMessageListener {
    void onUnsolicitedCardReaderWriterStatusMessage(UnsolicitedStatusMessage<CardReaderWriterFault> deviceFaultMessage);
}
