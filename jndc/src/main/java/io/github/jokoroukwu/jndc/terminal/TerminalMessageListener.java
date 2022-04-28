package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInitialisationData;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.EncryptorInitialisationDataMessageListener;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic.GenericEncryptorInformation;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv.NewKvv;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.defaultstatusmessage.CommandRejectStatusMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.defaultstatusmessage.ReadyStatusMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.SolicitedCardReaderWriterFaultMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.UnsolicitedCardReaderWriterFaultMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.GenericDeviceFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.SolicitedGenericDeviceFaultMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault.UnsolicitedGenericDeviceFaultMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.generic.GenericSolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.generic.GenericSolicitedStatusMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.ReadyBStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.ReadyBStatusMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasic;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtended;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedMessageListener;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClockFailure;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClockFailureMessageListener;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessage;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageListener;

public interface TerminalMessageListener extends TransactionRequestMessageListener, UnsolicitedGenericDeviceFaultMessageListener,
        UnsolicitedCardReaderWriterFaultMessageListener, EncryptorInitialisationDataMessageListener,
        SupplyCountersBasicMessageListener, SupplyCountersExtendedMessageListener, ReadyBStatusMessageListener,
        CommandRejectStatusMessageListener, ReadyStatusMessageListener, GenericSolicitedStatusMessageListener,
        SolicitedGenericDeviceFaultMessageListener, SolicitedCardReaderWriterFaultMessageListener, TimeOfDayClockFailureMessageListener {

    @Override
    default void onTransactionRequestMessage(TransactionRequestMessage message) {
    }

    @Override
    default void onReadyBStatusMessage(SolicitedStatusMessage<ReadyBStatus> message) {

    }

    @Override
    default void onSolicitedCardReaderWriterFaultMessage(SolicitedStatusMessage<CardReaderWriterFault> message) {

    }

    @Override
    default void onSolicitedGenericStatusMessage(SolicitedStatusMessage<GenericSolicitedStatusInformation> message) {

    }

    @Override
    default void onCommandRejectStatusMessage(SolicitedStatusMessage<SolicitedStatusInformation> message) {

    }

    @Override
    default void onReadyStatusMessage(SolicitedStatusMessage<SolicitedStatusInformation> message) {

    }

    @Override
    default void onNewKeyVerificationValueMessage(EncryptorInitialisationData<NewKvv> message) {

    }

    @Override
    default void onGenericEncryptorDataMessage(EncryptorInitialisationData<GenericEncryptorInformation> message) {

    }

    @Override
    default void onSolicitedGenericDeviceFaultMessage(SolicitedStatusMessage<GenericDeviceFault> message) {

    }

    @Override
    default void onUnsolicitedGenericDeviceFaultMessage(UnsolicitedStatusMessage<GenericDeviceFault> message) {

    }

    @Override
    default void onUnsolicitedCardReaderWriterStatusMessage(UnsolicitedStatusMessage<CardReaderWriterFault> message) {

    }

    @Override
    default void onSolicitedSupplyCountersBasicMessage(SolicitedStatusMessage<SupplyCountersBasic> message) {

    }

    @Override
    default void onSupplyCountersExtendedMessage(SolicitedStatusMessage<SupplyCountersExtended> message) {

    }

    @Override
    default void onTimeOfDayClockFailureMessage(UnsolicitedStatusMessage<TimeOfDayClockFailure> message) {

    }
}
