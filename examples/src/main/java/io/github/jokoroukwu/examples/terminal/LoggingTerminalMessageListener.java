package io.github.jokoroukwu.examples.terminal;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageListener;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInitialisationData;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic.GenericEncryptorInformation;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv.NewKvv;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.GenericDeviceFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.generic.GenericSolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.ReadyBStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasic;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure.PowerFailure;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClock;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This listener implementation is just a demonstration of
 * how central messages are consumed.
 */
public class LoggingTerminalMessageListener implements TerminalMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingTerminalMessageListener.class);

    @Override
    public void onTransactionRequestMessage(TransactionRequestMessage message) {
        LOGGER.info("Transaction Request message received: {}", message);
    }

    @Override
    public void onReadyBStatusMessage(SolicitedStatusMessage<ReadyBStatus> message) {
        LOGGER.info("ReadyB Status message received: {}", message);
    }

    @Override
    public void onSolicitedCardReaderWriterFaultMessage(SolicitedStatusMessage<CardReaderWriterFault> message) {
        LOGGER.info("Card Reader/ Writer Solicited Fault message received: {}", message);
    }

    @Override
    public void onSolicitedGenericStatusMessage(SolicitedStatusMessage<GenericSolicitedStatusInformation> message) {
        LOGGER.info("Generic Status message received: {}", message);
    }

    @Override
    public void onCommandRejectStatusMessage(SolicitedStatusMessage<SolicitedStatusInformation> message) {
        LOGGER.info("Command Reject Status message received: {}", message);
    }

    @Override
    public void onReadyStatusMessage(SolicitedStatusMessage<SolicitedStatusInformation> message) {
        LOGGER.info("Ready Status message received: {}", message);
    }

    @Override
    public void onNewKeyVerificationValueMessage(EncryptorInitialisationData<NewKvv> message) {
        LOGGER.info("New Key Verification Value message received: {}", message);
    }

    @Override
    public void onGenericEncryptorDataMessage(EncryptorInitialisationData<GenericEncryptorInformation> message) {
        LOGGER.info("Generic Encryptor Initialisation Data message received: {}", message);
    }

    @Override
    public void onSolicitedGenericDeviceFaultMessage(SolicitedStatusMessage<GenericDeviceFault> message) {
        LOGGER.info("Generic Solicited Device Fault message received: {}", message);
    }

    @Override
    public void onUnsolicitedGenericDeviceFaultMessage(UnsolicitedStatusMessage<GenericDeviceFault> message) {
        LOGGER.info("Generic Unsolicited Device Fault message received: {}", message);
    }

    @Override
    public void onUnsolicitedCardReaderWriterStatusMessage(UnsolicitedStatusMessage<CardReaderWriterFault> message) {
        LOGGER.info("Card Reader/ Writer Unsolicited Fault message received: {}", message);
    }

    @Override
    public void onSolicitedSupplyCountersBasicMessage(SolicitedStatusMessage<SupplyCountersBasic> message) {
        LOGGER.info("Supply Counters Basic message received: {}", message);
    }

    @Override
    public void onSupplyCountersExtendedMessage(SolicitedStatusMessage<SupplyCountersExtended> message) {
        LOGGER.info("Supply Counters Extended message received: {}", message);
    }

    @Override
    public void onTimeOfDayClockStatusMessage(UnsolicitedStatusMessage<TimeOfDayClock> message) {
        LOGGER.info("Time of Day Clock Failure Unsolicited Status message received: {}", message);
    }

    @Override
    public void onPowerFailureMessage(UnsolicitedStatusMessage<PowerFailure> message) {
        LOGGER.info("Power Failure Unsolicited Status message received: {}", message);
    }
}
