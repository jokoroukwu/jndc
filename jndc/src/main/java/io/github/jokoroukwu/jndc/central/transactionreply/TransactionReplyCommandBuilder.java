package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.MessageCoordinationNumberAcceptor;
import io.github.jokoroukwu.jndc.central.transactionreply.cardflag.CardFlag;
import io.github.jokoroukwu.jndc.central.transactionreply.chequedestination.ChequeDestinationBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense.CoinsToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.depositlimit.DepositLimitsBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.FunctionId;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.MultiChequeBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.notestodispense.NotesToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterDataList;
import io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate.ScreenDisplayUpdate;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.SequenceTimeVariantNumber;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.TransactionReplySmartCardBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TransactionUpdateBuffer;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBuffer;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track2DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track3DataBuffer;
import io.github.jokoroukwu.jndc.tsn.TransactionSerialNumberAcceptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class TransactionReplyCommandBuilder implements MessageCoordinationNumberAcceptor<TransactionReplyCommandBuilder>,
        MacAcceptor<TransactionReplyCommandBuilder>, TransactionSerialNumberAcceptor<TransactionReplyCommandBuilder> {
    private char responseFlag = '0';
    private Luno luno;
    private SequenceTimeVariantNumber sequenceTimeVariantNumber;
    private String nextStateIdData;
    private NotesToDispense notesToDispense;
    private CoinsToDispense coinsToDispense = CoinsToDispense.EMPTY;
    private int transactionSerialNumber;
    private FunctionId functionId;
    private ScreenDisplayUpdate screenDisplayUpdate;
    private char messageCoordinationNumber;
    private CardFlag cardFlag;
    private PrinterDataList printerDataList;
    private Track3DataBuffer track3DataBuffer;
    private Track1DataBuffer track1DataBuffer;
    private Track2DataBuffer track2DataBuffer;
    private TransactionReplySmartCardBuffer smartCardBuffer;
    private ChequeDestinationBuffer chequeDestinationBuffer;
    private MultiChequeBuffer multiChequeBuffer;
    private TransactionUpdateBuffer transactionUpdateBuffer;
    private DepositLimitsBuffer depositLimitsBuffer;
    private GenericBuffer bufferS;
    private Map<Character, IdentifiableBuffer> exitsBufferMap = Collections.emptyMap();
    private String mac = Strings.EMPTY_STRING;


    @Override
    public TransactionReplyCommandBuilder withMessageCoordinationNumber(char messageCoordinationNumber) {
        this.messageCoordinationNumber = messageCoordinationNumber;
        return this;
    }

    @Override
    public TransactionReplyCommandBuilder withMac(String mac) {
        this.mac = mac;
        return this;
    }

    @Override
    public TransactionReplyCommandBuilder withTransactionSerialNumber(int transactionSerialNumber) {
        this.transactionSerialNumber = transactionSerialNumber;
        return this;
    }

    public TransactionReplyCommandBuilder withResponseFlag(char responseFlag) {
        this.responseFlag = responseFlag;
        return this;
    }

    public TransactionReplyCommandBuilder withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public TransactionReplyCommandBuilder withSequenceTimeVariantNumber(SequenceTimeVariantNumber sequenceTimeVariantNumber) {
        this.sequenceTimeVariantNumber = sequenceTimeVariantNumber;
        return this;
    }

    public TransactionReplyCommandBuilder withNextStateIdData(String nextStateIdData) {
        this.nextStateIdData = nextStateIdData;
        return this;
    }

    public TransactionReplyCommandBuilder withNotesToDispense(NotesToDispense notesToDispense) {
        this.notesToDispense = notesToDispense;
        return this;
    }

    public TransactionReplyCommandBuilder withCoinsToDispense(CoinsToDispense coinsToDispense) {
        this.coinsToDispense = coinsToDispense;
        return this;
    }

    public TransactionReplyCommandBuilder withFunctionId(FunctionId functionId) {
        this.functionId = functionId;
        return this;
    }


    public TransactionReplyCommandBuilder withScreenDisplayUpdate(ScreenDisplayUpdate screenDisplayUpdate) {
        this.screenDisplayUpdate = screenDisplayUpdate;
        return this;
    }

    public TransactionReplyCommandBuilder withCardFlag(CardFlag cardFlag) {
        this.cardFlag = cardFlag;
        return this;
    }

    public TransactionReplyCommandBuilder withPrinterDataList(PrinterDataList printerDataList) {
        this.printerDataList = printerDataList;
        return this;
    }

    public TransactionReplyCommandBuilder withTrack3DataBuffer(Track3DataBuffer track3DataBuffer) {
        this.track3DataBuffer = track3DataBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withTrack1DataBuffer(Track1DataBuffer track1DataBuffer) {
        this.track1DataBuffer = track1DataBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withTrack2DataBuffer(Track2DataBuffer track2DataBuffer) {
        this.track2DataBuffer = track2DataBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withSmartCardBuffer(TransactionReplySmartCardBuffer smartCardBuffer) {
        this.smartCardBuffer = smartCardBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withChequeDestinationBuffer(ChequeDestinationBuffer chequeDestinationBuffer) {
        this.chequeDestinationBuffer = chequeDestinationBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withMultiChequeBuffer(MultiChequeBuffer multiChequeBuffer) {
        this.multiChequeBuffer = multiChequeBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withTransactionUpdateBuffer(TransactionUpdateBuffer transactionUpdateBuffer) {
        this.transactionUpdateBuffer = transactionUpdateBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withDepositLimitsBuffer(DepositLimitsBuffer depositLimitsBuffer) {
        this.depositLimitsBuffer = depositLimitsBuffer;
        return this;
    }

    public TransactionReplyCommandBuilder withBufferS(GenericBuffer bufferS) {
        this.bufferS = bufferS;
        return this;
    }

    public TransactionReplyCommandBuilder withExitsBufferMap(Map<Character, IdentifiableBuffer> exitsBufferMap) {
        this.exitsBufferMap = exitsBufferMap;
        return this;
    }

    public TransactionReplyCommandBuilder putExitsBuffer(IdentifiableBuffer identifiableBuffer) {
        ObjectUtils.validateNotNull(identifiableBuffer, "identifiableBuffer");
        if (exitsBufferMap == Collections.EMPTY_MAP) {
            exitsBufferMap = new HashMap<>();
        }
        exitsBufferMap.put(identifiableBuffer.getId(), identifiableBuffer);
        return this;
    }

    public char getResponseFlag() {
        return responseFlag;
    }

    public Luno getLuno() {
        return luno;
    }

    public SequenceTimeVariantNumber getSequenceTimeVariantNumber() {
        return sequenceTimeVariantNumber;
    }

    public String getNextStateIdData() {
        return nextStateIdData;
    }

    public NotesToDispense getNotesToDispense() {
        return notesToDispense;
    }

    public CoinsToDispense getCoinsToDispense() {
        return coinsToDispense;
    }

    public int getTransactionSerialNumber() {
        return transactionSerialNumber;
    }

    public FunctionId getFunctionId() {
        return functionId;
    }

    public ScreenDisplayUpdate getScreenDisplayUpdate() {
        return screenDisplayUpdate;
    }

    public char getMessageCoordinationNumber() {
        return messageCoordinationNumber;
    }

    public CardFlag getCardFlag() {
        return cardFlag;
    }

    public PrinterDataList getPrinterDataList() {
        return printerDataList;
    }

    public Track3DataBuffer getTrack3DataBuffer() {
        return track3DataBuffer;
    }

    public Track1DataBuffer getTrack1DataBuffer() {
        return track1DataBuffer;
    }

    public Track2DataBuffer getTrack2DataBuffer() {
        return track2DataBuffer;
    }

    public TransactionReplySmartCardBuffer getSmartCardBuffer() {
        return smartCardBuffer;
    }

    public ChequeDestinationBuffer getChequeDestinationBuffer() {
        return chequeDestinationBuffer;
    }

    public MultiChequeBuffer getMultiChequeBuffer() {
        return multiChequeBuffer;
    }

    public TransactionUpdateBuffer getTransactionUpdateBuffer() {
        return transactionUpdateBuffer;
    }

    public DepositLimitsBuffer getDepositLimitsBuffer() {
        return depositLimitsBuffer;
    }

    public GenericBuffer getBufferS() {
        return bufferS;
    }

    public Map<Character, IdentifiableBuffer> getExitsBufferMap() {
        return exitsBufferMap;
    }

    public String getMac() {
        return mac;
    }

    public TransactionReplyCommand build() {
        return new TransactionReplyCommand(responseFlag,
                luno,
                sequenceTimeVariantNumber,
                nextStateIdData,
                notesToDispense,
                coinsToDispense,
                transactionSerialNumber,
                functionId,
                screenDisplayUpdate,
                messageCoordinationNumber,
                cardFlag,
                printerDataList,
                track3DataBuffer,
                track1DataBuffer,
                track2DataBuffer,
                smartCardBuffer,
                chequeDestinationBuffer,
                multiChequeBuffer,
                transactionUpdateBuffer,
                depositLimitsBuffer,
                bufferS,
                exitsBufferMap,
                mac);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionReplyCommandBuilder.class.getSimpleName() + ": {", "}")
                .add("responseFlag: " + responseFlag)
                .add("LUNO: " + luno)
                .add("sequenceTimeVariantNumber: " + sequenceTimeVariantNumber)
                .add("nextStateIdData: '" + nextStateIdData + '\'')
                .add("notesToDispense: " + notesToDispense)
                .add("coinsToDispense: " + coinsToDispense)
                .add("transactionSerialNumber: " + transactionSerialNumber)
                .add("functionId: " + functionId)
                .add("screenDisplayUpdate: " + screenDisplayUpdate)
                .add("messageCoordinationNumber: " + messageCoordinationNumber)
                .add("cardFlag: " + cardFlag)
                .add("printerDataList: " + printerDataList)
                .add("track3DataBuffer: " + track3DataBuffer)
                .add("track1DataBuffer: " + track1DataBuffer)
                .add("track2DataBuffer: " + track2DataBuffer)
                .add("smartCardBuffer: " + smartCardBuffer)
                .add("chequeDestinationBuffer: " + chequeDestinationBuffer)
                .add("multiChequeBuffer: " + multiChequeBuffer)
                .add("transactionUpdateBuffer: " + transactionUpdateBuffer)
                .add("depositLimitsBuffer: " + depositLimitsBuffer)
                .add("exitsBufferMap: " + exitsBufferMap)
                .add("mac: '" + mac + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionReplyCommandBuilder that = (TransactionReplyCommandBuilder) o;
        return responseFlag == that.responseFlag &&
                transactionSerialNumber == that.transactionSerialNumber &&
                messageCoordinationNumber == that.messageCoordinationNumber &&
                Objects.equals(cardFlag, that.cardFlag) &&
                Objects.equals(chequeDestinationBuffer, that.chequeDestinationBuffer) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(sequenceTimeVariantNumber, that.sequenceTimeVariantNumber) &&
                Objects.equals(nextStateIdData, that.nextStateIdData) &&
                Objects.equals(notesToDispense, that.notesToDispense) &&
                Objects.equals(coinsToDispense, that.coinsToDispense) &&
                Objects.equals(functionId, that.functionId) &&
                Objects.equals(screenDisplayUpdate, that.screenDisplayUpdate) &&
                Objects.equals(printerDataList, that.printerDataList) &&
                Objects.equals(track3DataBuffer, that.track3DataBuffer) &&
                Objects.equals(track1DataBuffer, that.track1DataBuffer) &&
                Objects.equals(track2DataBuffer, that.track2DataBuffer) &&
                Objects.equals(smartCardBuffer, that.smartCardBuffer) &&
                Objects.equals(multiChequeBuffer, that.multiChequeBuffer) &&
                Objects.equals(transactionUpdateBuffer, that.transactionUpdateBuffer) &&
                Objects.equals(depositLimitsBuffer, that.depositLimitsBuffer) &&
                Objects.equals(exitsBufferMap, that.exitsBufferMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseFlag, luno, sequenceTimeVariantNumber, nextStateIdData, notesToDispense,
                coinsToDispense, transactionSerialNumber, functionId, screenDisplayUpdate,
                messageCoordinationNumber, cardFlag, printerDataList, track3DataBuffer, track1DataBuffer,
                track2DataBuffer, smartCardBuffer, chequeDestinationBuffer, multiChequeBuffer, transactionUpdateBuffer,
                depositLimitsBuffer, exitsBufferMap, mac);
    }

}
