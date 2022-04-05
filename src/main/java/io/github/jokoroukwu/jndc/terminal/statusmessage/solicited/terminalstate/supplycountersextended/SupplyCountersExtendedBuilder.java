package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

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

public final class SupplyCountersExtendedBuilder {
    private TransactionCounterGroup transactionGroup;
    private CardReaderDataGroup cardReaderDataGroup;
    private CashHandlerDataGroup cashHandler0DataGroup;
    private CashHandlerDataGroup cashHandler1DataGroup;
    private CoinDispenserDataGroup coinDispenserDataGroup;
    private EnvelopeDepositoryDataGroup envelopeDepositoryDataGroup;
    private CameraDataGroup cameraDataGroup;
    private DpmDataGroup dpmDataGroup;
    private NdcCassetteCountsGroup ndcCassetteCountsGroup;
    private ChequeProcessorDataGroup chequeProcessorDataGroup;
    private BnaEmulationDepositDataGroup bnaEmulationDepositDataGroup;
    private DualDispenserDataGroup dualDispenserDataGroup;
    private NdcCassetteCountsGroup ecbCategory2NotesData;
    private NdcCassetteCountsGroup ecbCategory3NotesData;
    private SecondCardReaderData secondCardReaderData;

    SupplyCountersExtendedBuilder() {
    }

    public SupplyCountersExtendedBuilder withTransactionGroup(TransactionCounterGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withCardReaderDataGroup(CardReaderDataGroup cardReaderDataGroup) {
        this.cardReaderDataGroup = cardReaderDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withCashHandler0DataGroup(CashHandlerDataGroup cashHandler0DataGroup) {
        this.cashHandler0DataGroup = cashHandler0DataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withCashHandler1DataGroup(CashHandlerDataGroup cashHandler1DataGroup) {
        this.cashHandler1DataGroup = cashHandler1DataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withCoinDispenserDataGroup(CoinDispenserDataGroup coinDispenserDataGroup) {
        this.coinDispenserDataGroup = coinDispenserDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withEnvelopeDepositoryDataGroup(EnvelopeDepositoryDataGroup envelopeDepositoryDataGroup) {
        this.envelopeDepositoryDataGroup = envelopeDepositoryDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withCameraDataGroup(CameraDataGroup cameraDataGroup) {
        this.cameraDataGroup = cameraDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withDpmDataGroup(DpmDataGroup dpmDataGroup) {
        this.dpmDataGroup = dpmDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withBnaCassetteCountsGroup(NdcCassetteCountsGroup ndcCassetteCountsGroup) {
        this.ndcCassetteCountsGroup = ndcCassetteCountsGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withChequeProcessorDataGroup(ChequeProcessorDataGroup chequeProcessorDataGroup) {
        this.chequeProcessorDataGroup = chequeProcessorDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withBnaEmulationDepositDataGroup(BnaEmulationDepositDataGroup bnaEmulationDepositDataGroup) {
        this.bnaEmulationDepositDataGroup = bnaEmulationDepositDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withDualDispenserDataGroup(DualDispenserDataGroup dualDispenserDataGroup) {
        this.dualDispenserDataGroup = dualDispenserDataGroup;
        return this;
    }

    public SupplyCountersExtendedBuilder withEcbCategory2NotesData(NdcCassetteCountsGroup ecbCategory2NotesData) {
        this.ecbCategory2NotesData = ecbCategory2NotesData;
        return this;
    }

    public SupplyCountersExtendedBuilder withEcbCategory3NotesData(NdcCassetteCountsGroup ecbCategory3NotesData) {
        this.ecbCategory3NotesData = ecbCategory3NotesData;
        return this;
    }

    public SupplyCountersExtendedBuilder withSecondCardReaderData(SecondCardReaderData secondCardReaderData) {
        this.secondCardReaderData = secondCardReaderData;
        return this;
    }

    public TransactionCounterGroup getTransactionGroup() {
        return transactionGroup;
    }

    public CardReaderDataGroup getCardReaderDataGroup() {
        return cardReaderDataGroup;
    }

    public CashHandlerDataGroup getCashHandler0DataGroup() {
        return cashHandler0DataGroup;
    }

    public CashHandlerDataGroup getCashHandler1DataGroup() {
        return cashHandler1DataGroup;
    }

    public CoinDispenserDataGroup getCoinDispenserDataGroup() {
        return coinDispenserDataGroup;
    }

    public EnvelopeDepositoryDataGroup getEnvelopeDepositoryDataGroup() {
        return envelopeDepositoryDataGroup;
    }

    public CameraDataGroup getCameraDataGroup() {
        return cameraDataGroup;
    }

    public DpmDataGroup getDpmDataGroup() {
        return dpmDataGroup;
    }

    public NdcCassetteCountsGroup getBnaCassetteCounts() {
        return ndcCassetteCountsGroup;
    }

    public ChequeProcessorDataGroup getChequeProcessorDataGroup() {
        return chequeProcessorDataGroup;
    }

    public BnaEmulationDepositDataGroup getBnaEmulationDepositDataGroup() {
        return bnaEmulationDepositDataGroup;
    }

    public DualDispenserDataGroup getDualDispenserCombinedDataGroup() {
        return dualDispenserDataGroup;
    }

    public NdcCassetteCountsGroup getEcbCategory2NotesData() {
        return ecbCategory2NotesData;
    }

    public NdcCassetteCountsGroup getEcbCategory3NotesData() {
        return ecbCategory3NotesData;
    }

    public SecondCardReaderData getSecondCardReaderData() {
        return secondCardReaderData;
    }

    public SupplyCountersExtended build() {
        return new SupplyCountersExtended(
                transactionGroup,
                cardReaderDataGroup,
                cashHandler0DataGroup,
                cashHandler1DataGroup,
                coinDispenserDataGroup,
                envelopeDepositoryDataGroup,
                cameraDataGroup,
                dpmDataGroup,
                ndcCassetteCountsGroup,
                chequeProcessorDataGroup,
                bnaEmulationDepositDataGroup,
                dualDispenserDataGroup,
                ecbCategory2NotesData,
                ecbCategory3NotesData,
                secondCardReaderData);
    }
}
