package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.MessageCoordinationNumberAcceptor;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.BufferB;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry.AmountEntry;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata.BarCodeBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.BunchChequeDepositBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.CashAcceptorBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers.CoinHoppersBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata.CspData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata.DocumentBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata.NotesDataBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata.SmartCardBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.LastTransactionStatusData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance.VoiceGuidanceBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.util.ArrayUtils;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class TransactionRequestMessageBuilder implements MessageCoordinationNumberAcceptor<TransactionRequestMessageBuilder>,
        MacAcceptor<TransactionRequestMessageBuilder> {
    protected Luno luno = Luno.DEFAULT;
    protected long timeVariantNumber = -1L;
    protected AmountEntry amountEntry;
    protected boolean isTopOfReceipt;
    protected char messageCoordinationNumber;
    protected String track2Data = Strings.EMPTY_STRING;
    protected String track3Data = Strings.EMPTY_STRING;
    protected String operationCodeData = Strings.EMPTY_STRING;
    protected String pinBufferA = Strings.EMPTY_STRING;
    protected String bufferC = Strings.EMPTY_STRING;
    protected String mac = Strings.EMPTY_STRING;
    protected BufferB bufferB;
    protected Track1DataBuffer track1Data;
    protected LastTransactionStatusData lastTransactionStatusData;
    protected CspData cspData;
    protected CspData confirmationCspData;
    protected SmartCardBuffer smartCardBuffer;
    protected CashAcceptorBuffer cashAcceptorBuffer;
    protected DocumentBuffer documentBuffer;
    protected NotesDataBuffer suspectNotesDataBuffer;
    protected NotesDataBuffer counterfeitNotesDataBuffer;
    protected BarCodeBuffer barCodeBuffer;
    protected CoinHoppersBuffer coinHoppersBuffer;
    protected BunchChequeDepositBuffer bunchChequeDepositBuffer;
    protected VoiceGuidanceBuffer voiceGuidanceBuffer;
    protected Map<Character, IdentifiableBuffer> exitsBufferMap = Collections.emptyMap();
    protected LinkedHashMap<Character, IdentifiableBuffer> optionalDataFieldsMap;

    @Override
    public TransactionRequestMessageBuilder withMac(String mac) {
        this.mac = mac;
        return this;
    }

    @Override
    public TransactionRequestMessageBuilder withMessageCoordinationNumber(char messageCoordinationNumber) {
        this.messageCoordinationNumber = messageCoordinationNumber;
        return this;
    }

    public TransactionRequestMessageBuilder withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public TransactionRequestMessageBuilder withTimeVariantNumber(long timeVariantNumber) {
        this.timeVariantNumber = timeVariantNumber;
        return this;
    }

    public TransactionRequestMessageBuilder withTopOfReceipt(boolean topOfReceipt) {
        isTopOfReceipt = topOfReceipt;
        return this;
    }

    public TransactionRequestMessageBuilder withTrack2Data(String track2Data) {
        this.track2Data = track2Data;
        return this;
    }

    public TransactionRequestMessageBuilder withTrack3Data(String track3Data) {
        this.track3Data = track3Data;
        return this;
    }

    public TransactionRequestMessageBuilder withOperationCodeData(String operationCodeData) {
        this.operationCodeData = operationCodeData;
        return this;
    }

    public TransactionRequestMessageBuilder withAmountEntry(AmountEntry amountEntry) {
        this.amountEntry = amountEntry;
        return this;
    }

    public TransactionRequestMessageBuilder withPinBufferA(String pinBufferA) {
        this.pinBufferA = pinBufferA;
        return this;
    }

    public TransactionRequestMessageBuilder withBufferB(BufferB bufferB) {
        this.bufferB = bufferB;
        return this;
    }

    public TransactionRequestMessageBuilder withBufferC(String bufferC) {
        this.bufferC = bufferC;
        return this;
    }

    public TransactionRequestMessageBuilder withTrack1Data(Track1DataBuffer track1Data) {
        this.track1Data = track1Data;
        return this;
    }

    public TransactionRequestMessageBuilder withLastTransactionStatusData(LastTransactionStatusData lastTransactionStatusData) {
        this.lastTransactionStatusData = lastTransactionStatusData;
        return this;
    }

    public TransactionRequestMessageBuilder withCspData(CspData cspData) {
        this.cspData = cspData;
        return this;
    }

    public TransactionRequestMessageBuilder withConfirmationCspData(CspData confirmationCspData) {
        this.confirmationCspData = confirmationCspData;
        return this;
    }

    public TransactionRequestMessageBuilder withSmartCardBuffer(SmartCardBuffer smartCardBuffer) {
        this.smartCardBuffer = smartCardBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withCashAcceptorBuffer(CashAcceptorBuffer cashAcceptorBuffer) {
        this.cashAcceptorBuffer = cashAcceptorBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withDocumentBuffer(DocumentBuffer documentBuffer) {
        this.documentBuffer = documentBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withSuspectNotesBuffer(NotesDataBuffer suspectNotesDataBuffer) {
        this.suspectNotesDataBuffer = suspectNotesDataBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withCounterfeitNotesBuffer(NotesDataBuffer counterfeitNotesDataBuffer) {
        this.counterfeitNotesDataBuffer = counterfeitNotesDataBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withBarCodeBuffer(BarCodeBuffer barCodeBuffer) {
        this.barCodeBuffer = barCodeBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withCoinHoppersBuffer(CoinHoppersBuffer coinHoppersBuffer) {
        this.coinHoppersBuffer = coinHoppersBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withBunchChequeDepositBuffer(BunchChequeDepositBuffer bunchChequeDepositBuffer) {
        this.bunchChequeDepositBuffer = bunchChequeDepositBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withVoiceGuidanceBuffer(VoiceGuidanceBuffer voiceGuidanceBuffer) {
        this.voiceGuidanceBuffer = voiceGuidanceBuffer;
        return this;
    }

    public TransactionRequestMessageBuilder withExitsBufferMap(Map<Character, IdentifiableBuffer> exitsBufferMap) {
        this.exitsBufferMap = exitsBufferMap;
        return this;
    }

    public TransactionRequestMessageBuilder withOptionalDataFieldsMap(LinkedHashMap<Character, IdentifiableBuffer> optionalDataFieldsMap) {
        this.optionalDataFieldsMap = optionalDataFieldsMap;
        return this;
    }

    public TransactionRequestMessageBuilder putOptionalDataField(IdentifiableBuffer identifiableBuffer) {
        ObjectUtils.validateNotNull(identifiableBuffer, "identifiableBuffer");
        if (optionalDataFieldsMap == null) {
            optionalDataFieldsMap = new LinkedHashMap<>();
        }
        optionalDataFieldsMap.put(identifiableBuffer.getId(), identifiableBuffer);
        return this;
    }

    public TransactionRequestMessageBuilder putExitsBuffer(IdentifiableBuffer identifiableBuffer, IdentifiableBuffer... other) {
        ObjectUtils.validateNotNull(identifiableBuffer, "identifiableBuffer");
        if (exitsBufferMap == Collections.EMPTY_MAP) {
            exitsBufferMap = new HashMap<>();
        }
        if (ArrayUtils.isNullOrEmpty(other)) {
            exitsBufferMap.put(identifiableBuffer.getId(), identifiableBuffer);
            return this;
        }
        exitsBufferMap.put(identifiableBuffer.getId(), identifiableBuffer);
        for (IdentifiableBuffer buffer : other) {
            exitsBufferMap.put(buffer.getId(), buffer);
        }
        return this;
    }


    public Luno getLuno() {
        return luno;
    }

    public long getTimeVariantNumber() {
        return timeVariantNumber;
    }

    public AmountEntry getAmountEntry() {
        return amountEntry;
    }

    public boolean isTopOfReceipt() {
        return isTopOfReceipt;
    }

    public char getMessageCoordinationNumber() {
        return messageCoordinationNumber;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public String getTrack3Data() {
        return track3Data;
    }

    public String getOperationCodeData() {
        return operationCodeData;
    }

    public String getPinBufferA() {
        return pinBufferA;
    }

    public String getBufferC() {
        return bufferC;
    }

    public String getMac() {
        return mac;
    }

    public BufferB getBufferB() {
        return bufferB;
    }

    public Track1DataBuffer getTrack1Data() {
        return track1Data;
    }

    public LastTransactionStatusData getLastTransactionStatusData() {
        return lastTransactionStatusData;
    }

    public CspData getCspData() {
        return cspData;
    }

    public CspData getConfirmationCspData() {
        return confirmationCspData;
    }

    public SmartCardBuffer getSmartCardBuffer() {
        return smartCardBuffer;
    }

    public CashAcceptorBuffer getCashAcceptorBuffer() {
        return cashAcceptorBuffer;
    }

    public DocumentBuffer getDocumentBuffer() {
        return documentBuffer;
    }

    public NotesDataBuffer getSuspectNotesDataBuffer() {
        return suspectNotesDataBuffer;
    }

    public NotesDataBuffer getCounterfeitNotesDataBuffer() {
        return counterfeitNotesDataBuffer;
    }

    public BarCodeBuffer getBarCodeBuffer() {
        return barCodeBuffer;
    }

    public CoinHoppersBuffer getCoinHoppersBuffer() {
        return coinHoppersBuffer;
    }

    public BunchChequeDepositBuffer getBunchChequeDepositBuffer() {
        return bunchChequeDepositBuffer;
    }

    public VoiceGuidanceBuffer getVoiceGuidanceBuffer() {
        return voiceGuidanceBuffer;
    }

    public Map<Character, IdentifiableBuffer> getExitsBufferMap() {
        return exitsBufferMap;
    }

    public LinkedHashMap<Character, IdentifiableBuffer> getOptionalDataFieldsMap() {
        return optionalDataFieldsMap;
    }

    public TransactionRequestMessage build() {
        return new TransactionRequestMessage(luno,
                timeVariantNumber,
                isTopOfReceipt,
                messageCoordinationNumber,
                track2Data,
                track3Data,
                operationCodeData,
                amountEntry,
                pinBufferA,
                bufferB,
                bufferC,
                track1Data,
                lastTransactionStatusData,
                cspData,
                confirmationCspData,
                smartCardBuffer,
                cashAcceptorBuffer,
                documentBuffer,
                suspectNotesDataBuffer,
                counterfeitNotesDataBuffer,
                barCodeBuffer,
                coinHoppersBuffer,
                bunchChequeDepositBuffer,
                voiceGuidanceBuffer,
                exitsBufferMap,
                optionalDataFieldsMap != null ? optionalDataFieldsMap : new LinkedHashMap<>(0),
                mac);
    }

    TransactionRequestMessage buildWithNoValidation() {
        return new TransactionRequestMessage(luno,
                timeVariantNumber,
                isTopOfReceipt,
                messageCoordinationNumber,
                track2Data,
                track3Data,
                operationCodeData,
                amountEntry,
                pinBufferA,
                bufferB,
                bufferC,
                track1Data,
                lastTransactionStatusData,
                cspData,
                confirmationCspData,
                smartCardBuffer,
                cashAcceptorBuffer,
                documentBuffer,
                suspectNotesDataBuffer,
                counterfeitNotesDataBuffer,
                barCodeBuffer,
                coinHoppersBuffer,
                bunchChequeDepositBuffer,
                voiceGuidanceBuffer,
                exitsBufferMap,
                optionalDataFieldsMap != null ? optionalDataFieldsMap : new LinkedHashMap<>(0),
                mac, null);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + ": {", "}")
                .add("LUNO: " + luno + "'")
                .add("timeVariantNumber: " + timeVariantNumber)
                .add("amountEntry: " + amountEntry)
                .add("isTopOfReceipt: " + isTopOfReceipt)
                .add("messageCoordinationNumber: " + messageCoordinationNumber)
                .add("track2Data: '" + track2Data + "'")
                .add("track3Data: '" + track3Data + "'")
                .add("operationCodeData: '" + operationCodeData + "'")
                .add("pinBufferA: '" + pinBufferA + "'")
                .add("bufferC: '" + bufferC + "'")
                .add("mac: '" + mac + "'")
                .add("bufferB: " + bufferB)
                .add("track1Data: " + track1Data)
                .add("lastTransactionStatusData: " + lastTransactionStatusData)
                .add("cspData: " + cspData)
                .add("confirmationCspData: " + confirmationCspData)
                .add("smartCardBuffer: " + smartCardBuffer)
                .add("cashAcceptorBuffer: " + cashAcceptorBuffer)
                .add("documentData: " + documentBuffer)
                .add("suspectNotesDataBuffer: " + suspectNotesDataBuffer)
                .add("counterfeitNotesDataBuffer: " + counterfeitNotesDataBuffer)
                .add("barCodeBuffer: " + barCodeBuffer)
                .add("coinHoppersBuffer: " + coinHoppersBuffer)
                .add("bunchChequeDepositBuffer: " + bunchChequeDepositBuffer)
                .add("voiceGuidanceBuffer: " + voiceGuidanceBuffer)
                .add("exitsBufferMap: " + exitsBufferMap)
                .add("optionalDataFieldsMap: " + optionalDataFieldsMap)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRequestMessageBuilder that = (TransactionRequestMessageBuilder) o;
        return timeVariantNumber == that.timeVariantNumber &&
                isTopOfReceipt == that.isTopOfReceipt &&
                messageCoordinationNumber == that.messageCoordinationNumber &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(amountEntry, that.amountEntry) &&
                Objects.equals(track2Data, that.track2Data) &&
                Objects.equals(track3Data, that.track3Data) &&
                Objects.equals(operationCodeData, that.operationCodeData) &&
                Objects.equals(pinBufferA, that.pinBufferA) &&
                Objects.equals(bufferC, that.bufferC) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(bufferB, that.bufferB) &&
                Objects.equals(track1Data, that.track1Data) &&
                Objects.equals(lastTransactionStatusData, that.lastTransactionStatusData) &&
                Objects.equals(cspData, that.cspData) &&
                Objects.equals(confirmationCspData, that.confirmationCspData) &&
                Objects.equals(smartCardBuffer, that.smartCardBuffer) &&
                Objects.equals(cashAcceptorBuffer, that.cashAcceptorBuffer) &&
                Objects.equals(documentBuffer, that.documentBuffer) &&
                Objects.equals(suspectNotesDataBuffer, that.suspectNotesDataBuffer) &&
                Objects.equals(counterfeitNotesDataBuffer, that.counterfeitNotesDataBuffer) &&
                Objects.equals(barCodeBuffer, that.barCodeBuffer) &&
                Objects.equals(coinHoppersBuffer, that.coinHoppersBuffer) &&
                Objects.equals(bunchChequeDepositBuffer, that.bunchChequeDepositBuffer) &&
                Objects.equals(voiceGuidanceBuffer, that.voiceGuidanceBuffer) &&
                Objects.equals(exitsBufferMap, that.exitsBufferMap) &&
                Objects.equals(optionalDataFieldsMap, that.optionalDataFieldsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(luno,
                timeVariantNumber,
                amountEntry,
                isTopOfReceipt,
                messageCoordinationNumber,
                track2Data,
                track3Data,
                operationCodeData,
                pinBufferA,
                bufferC,
                mac,
                bufferB,
                track1Data,
                lastTransactionStatusData,
                cspData,
                confirmationCspData,
                smartCardBuffer,
                cashAcceptorBuffer,
                documentBuffer,
                suspectNotesDataBuffer,
                counterfeitNotesDataBuffer,
                barCodeBuffer,
                coinHoppersBuffer,
                bunchChequeDepositBuffer,
                voiceGuidanceBuffer,
                exitsBufferMap,
                optionalDataFieldsMap);
    }
}
