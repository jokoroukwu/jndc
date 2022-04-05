package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup.BnaEmulationDepositDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cameragroup.CameraDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cardreadergroup.CardReaderDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup.CashHandlerDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup.ChequeProcessorDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinDispenserDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dpmgroup.DpmDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dualdispensergroup.DualDispenserDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup.EnvelopeDepositoryDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.secondarycardreadergroup.SecondCardReaderData;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.transactiongroup.TransactionCounterGroup;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class SupplyCountersExtended implements TerminalState {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " +
            StatusDescriptor.TERMINAL_STATE + ": " + TerminalStateMessageId.SEND_SUPPLY_COUNTERS_EXTENDED;

    private final TransactionCounterGroup transactionGroup;
    private final CardReaderDataGroup cardReaderDataGroup;
    private final CashHandlerDataGroup cashHandler0DataGroup;
    private final CashHandlerDataGroup cashHandler1DataGroup;
    private final CoinDispenserDataGroup coinDispenserDataGroup;
    private final EnvelopeDepositoryDataGroup envelopeDepositoryDataGroup;
    private final CameraDataGroup cameraDataGroup;
    private final DpmDataGroup dpmDataGroup;
    private final NdcCassetteCountsGroup bnaCassetteCountsGroup;
    private final ChequeProcessorDataGroup chequeProcessorDataGroup;
    private final BnaEmulationDepositDataGroup bnaEmulationDepositDataGroup;
    private final DualDispenserDataGroup dualDispenserDataGroup;
    private final NdcCassetteCountsGroup ecbCategory2NotesData;
    private final NdcCassetteCountsGroup ecbCategory3NotesData;
    private final SecondCardReaderData secondCardReaderData;

    public SupplyCountersExtended(TransactionCounterGroup transactionGroup,
                                  CardReaderDataGroup cardReaderDataGroup,
                                  CashHandlerDataGroup cashHandler0DataGroup,
                                  CashHandlerDataGroup cashHandler1DataGroup,
                                  CoinDispenserDataGroup coinDispenserDataGroup,
                                  EnvelopeDepositoryDataGroup envelopeDepositoryDataGroup,
                                  CameraDataGroup cameraDataGroup,
                                  DpmDataGroup dpmDataGroup,
                                  NdcCassetteCountsGroup bnaCassetteCountsGroup,
                                  ChequeProcessorDataGroup chequeProcessorDataGroup,
                                  BnaEmulationDepositDataGroup bnaEmulationDepositDataGroup,
                                  DualDispenserDataGroup dualDispenserDataGroup,
                                  NdcCassetteCountsGroup ecbCategory2NotesData,
                                  NdcCassetteCountsGroup ecbCategory3NotesData,
                                  SecondCardReaderData secondCardReaderData) {
        this.transactionGroup = ObjectUtils.validateNotNull(transactionGroup, "Transaction group id 'A'");
        this.cardReaderDataGroup = cardReaderDataGroup;
        this.cashHandler0DataGroup = cashHandler0DataGroup;
        this.cashHandler1DataGroup = cashHandler1DataGroup;
        this.coinDispenserDataGroup = coinDispenserDataGroup;
        this.envelopeDepositoryDataGroup = envelopeDepositoryDataGroup;
        this.cameraDataGroup = cameraDataGroup;
        this.dpmDataGroup = dpmDataGroup;
        this.bnaCassetteCountsGroup = bnaCassetteCountsGroup;
        this.chequeProcessorDataGroup = chequeProcessorDataGroup;
        this.bnaEmulationDepositDataGroup = bnaEmulationDepositDataGroup;
        this.dualDispenserDataGroup = dualDispenserDataGroup;
        this.ecbCategory2NotesData = ecbCategory2NotesData;
        this.ecbCategory3NotesData = ecbCategory3NotesData;
        this.secondCardReaderData = secondCardReaderData;
    }

    public static SupplyCountersExtendedBuilder builder() {
        return new SupplyCountersExtendedBuilder();
    }

    public SupplyCountersExtendedBuilder copy() {
        return new SupplyCountersExtendedBuilder()
                .withTransactionGroup(transactionGroup)
                .withCardReaderDataGroup(cardReaderDataGroup)
                .withCashHandler0DataGroup(cashHandler0DataGroup)
                .withCashHandler1DataGroup(cashHandler1DataGroup)
                .withCoinDispenserDataGroup(coinDispenserDataGroup)
                .withEnvelopeDepositoryDataGroup(envelopeDepositoryDataGroup)
                .withCameraDataGroup(cameraDataGroup)
                .withBnaCassetteCountsGroup(bnaCassetteCountsGroup)
                .withChequeProcessorDataGroup(chequeProcessorDataGroup)
                .withBnaEmulationDepositDataGroup(bnaEmulationDepositDataGroup)
                .withDualDispenserDataGroup(dualDispenserDataGroup)
                .withEcbCategory2NotesData(ecbCategory2NotesData)
                .withEcbCategory3NotesData(ecbCategory3NotesData)
                .withSecondCardReaderData(secondCardReaderData);
    }

    @Override
    public TerminalStateMessageId getMessageId() {
        return TerminalStateMessageId.SEND_SUPPLY_COUNTERS_EXTENDED;
    }

    public TransactionCounterGroup getTransactionGroup() {
        return transactionGroup;
    }

    public Optional<CardReaderDataGroup> getCardReaderDataGroup() {
        return Optional.ofNullable(cardReaderDataGroup);
    }

    public Optional<CashHandlerDataGroup> getCashHandler0DataGroup() {
        return Optional.ofNullable(cashHandler0DataGroup);
    }

    public Optional<CashHandlerDataGroup> getCashHandler1DataGroup() {
        return Optional.ofNullable(cashHandler1DataGroup);
    }

    public Optional<CoinDispenserDataGroup> getCoinDispenserDataGroup() {
        return Optional.ofNullable(coinDispenserDataGroup);
    }

    public Optional<EnvelopeDepositoryDataGroup> getEnvelopeDepositoryDataGroup() {
        return Optional.ofNullable(envelopeDepositoryDataGroup);
    }

    public Optional<CameraDataGroup> getCameraDataGroup() {
        return Optional.ofNullable(cameraDataGroup);
    }

    public Optional<DpmDataGroup> getDpmDataGroup() {
        return Optional.ofNullable(dpmDataGroup);
    }

    public Optional<NdcCassetteCountsGroup> getBnaCassetteCountsGroup() {
        return Optional.ofNullable(bnaCassetteCountsGroup);
    }

    public Optional<ChequeProcessorDataGroup> getChequeProcessorDataGroup() {
        return Optional.ofNullable(chequeProcessorDataGroup);
    }

    public Optional<BnaEmulationDepositDataGroup> getBnaEmulationDepositDataGroup() {
        return Optional.ofNullable(bnaEmulationDepositDataGroup);
    }

    public Optional<DualDispenserDataGroup> getDualDispenserDataGroup() {
        return Optional.ofNullable(dualDispenserDataGroup);
    }

    public Optional<NdcCassetteCountsGroup> getEcbCategory2NotesData() {
        return Optional.ofNullable(ecbCategory2NotesData);
    }

    public Optional<NdcCassetteCountsGroup> getEcbCategory3NotesData() {
        return Optional.ofNullable(ecbCategory3NotesData);
    }

    public Optional<SecondCardReaderData> getSecondCardReaderData() {
        return Optional.ofNullable(secondCardReaderData);
    }


    @Override
    public String toNdcString() {
        return new NdcStringBuilder(512)
                .append(TerminalStateMessageId.SEND_SUPPLY_COUNTERS_EXTENDED.getValue())
                .appendComponent(transactionGroup)
                .appendFieldGroup(cardReaderDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(cashHandler0DataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(cashHandler1DataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(coinDispenserDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(envelopeDepositoryDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(cameraDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(dpmDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(bnaCassetteCountsGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(chequeProcessorDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(bnaEmulationDepositDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(dualDispenserDataGroup, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(ecbCategory2NotesData, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(ecbCategory3NotesData, NdcConstants.GROUP_SEPARATOR)
                .appendFieldGroup(secondCardReaderData, NdcConstants.GROUP_SEPARATOR)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SupplyCountersExtended.class.getSimpleName() + ": {", "}")
                .add("transactionGroup: " + transactionGroup)
                .add("cardReaderDataGroup: " + cardReaderDataGroup)
                .add("cashHandler0DataGroup: " + cashHandler0DataGroup)
                .add("cashHandler1DataGroup: " + cashHandler1DataGroup)
                .add("coinDispenserDataGroup: " + coinDispenserDataGroup)
                .add("envelopeDepositoryDataGroup: " + envelopeDepositoryDataGroup)
                .add("cameraDataGroup: " + cameraDataGroup)
                .add("dpmDataGroup: " + dpmDataGroup)
                .add("bnaCassetteCountsGroup: " + bnaCassetteCountsGroup)
                .add("chequeProcessorDataGroup: " + chequeProcessorDataGroup)
                .add("bnaEmulationDepositDataGroup: " + bnaEmulationDepositDataGroup)
                .add("dualDispenserDataGroup: " + dualDispenserDataGroup)
                .add("ecbCategory2NotesData: " + ecbCategory2NotesData)
                .add("ecbCategory3NotesData: " + ecbCategory3NotesData)
                .add("secondCardReaderData: " + secondCardReaderData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplyCountersExtended that = (SupplyCountersExtended) o;
        return transactionGroup.equals(that.transactionGroup) &&
                cameraDataGroup == that.cameraDataGroup &&
                Objects.equals(cardReaderDataGroup, that.cardReaderDataGroup) &&
                Objects.equals(cashHandler0DataGroup, that.cashHandler0DataGroup) &&
                Objects.equals(cashHandler1DataGroup, that.cashHandler1DataGroup) &&
                Objects.equals(coinDispenserDataGroup, that.coinDispenserDataGroup) &&
                Objects.equals(envelopeDepositoryDataGroup, that.envelopeDepositoryDataGroup) &&
                Objects.equals(dpmDataGroup, that.dpmDataGroup) &&
                Objects.equals(bnaCassetteCountsGroup, that.bnaCassetteCountsGroup) &&
                Objects.equals(chequeProcessorDataGroup, that.chequeProcessorDataGroup) &&
                Objects.equals(bnaEmulationDepositDataGroup, that.bnaEmulationDepositDataGroup) &&
                Objects.equals(dualDispenserDataGroup, that.dualDispenserDataGroup) &&
                Objects.equals(ecbCategory2NotesData, that.ecbCategory2NotesData) &&
                Objects.equals(ecbCategory3NotesData, that.ecbCategory3NotesData) &&
                Objects.equals(secondCardReaderData, that.secondCardReaderData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionGroup, cardReaderDataGroup, cashHandler0DataGroup, cashHandler1DataGroup,
                coinDispenserDataGroup, envelopeDepositoryDataGroup, cameraDataGroup, dpmDataGroup,
                bnaCassetteCountsGroup, chequeProcessorDataGroup, bnaEmulationDepositDataGroup, dualDispenserDataGroup,
                ecbCategory2NotesData, ecbCategory3NotesData, secondCardReaderData);
    }
}
