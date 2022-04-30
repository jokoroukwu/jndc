package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
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
import io.github.jokoroukwu.jndc.trackdata.TrackDataBuffer;
import io.github.jokoroukwu.jndc.util.Chars;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class TransactionRequestMessage implements TerminalOriginatedMessage {
    public static final String COMMAND_NAME = TerminalMessageClass.UNSOLICITED
            + ": " + TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE;
    protected final Luno luno;
    protected final long timeVariantNumber;
    protected final boolean isTopOfReceipt;
    protected final char messageCoordinationNumber;
    protected final String track2Data;
    protected final String track3Data;
    protected final String operationCodeData;
    protected final AmountEntry amountEntry;
    protected final String pinBufferA;
    protected final BufferB bufferB;
    protected final String bufferC;
    protected final Track1DataBuffer track1Data;
    protected final LastTransactionStatusData lastTransactionStatusData;
    protected final CspData cspData;
    protected final CspData confirmationCspData;
    protected final SmartCardBuffer smartCardBuffer;
    protected final CashAcceptorBuffer cashAcceptorBuffer;
    protected final DocumentBuffer documentBuffer;
    protected final NotesDataBuffer suspectNotesDataBuffer;
    protected final NotesDataBuffer counterfeitNotesDataBuffer;
    protected final BarCodeBuffer barCodeBuffer;
    protected final CoinHoppersBuffer coinHoppersBuffer;
    protected final BunchChequeDepositBuffer bunchChequeDepositBuffer;
    protected final VoiceGuidanceBuffer voiceGuidanceBuffer;
    protected final Map<Character, IdentifiableBuffer> exitsBufferMap;
    protected final LinkedHashMap<Character, IdentifiableBuffer> optionalDataFieldsMap;
    protected final String mac;

    public TransactionRequestMessage(Luno luno,
                                     long timeVariantNumber,
                                     boolean isTopOfReceipt,
                                     char messageCoordinationNumber,
                                     String track2Data,
                                     String track3Data,
                                     String operationCodeData,
                                     AmountEntry amountEntry,
                                     String pinBufferA,
                                     BufferB bufferB,
                                     String bufferC,
                                     Track1DataBuffer track1Data,
                                     LastTransactionStatusData lastTransactionStatusData,
                                     CspData cspData,
                                     CspData confirmationCspData,
                                     SmartCardBuffer smartCardBuffer,
                                     CashAcceptorBuffer cashAcceptorBuffer,
                                     DocumentBuffer documentBuffer,
                                     NotesDataBuffer suspectNotesDataBuffer,
                                     NotesDataBuffer counterfeitNotesDataBuffer,
                                     BarCodeBuffer barCodeBuffer,
                                     CoinHoppersBuffer coinHoppersBuffer,
                                     BunchChequeDepositBuffer bunchChequeDepositBuffer,
                                     VoiceGuidanceBuffer voiceGuidanceBuffer,
                                     Map<Character, IdentifiableBuffer> exitsBufferMap,
                                     LinkedHashMap<Character, IdentifiableBuffer> optionalDataFieldsMap, String mac) {
        this.luno = ObjectUtils.validateNotNull(luno, "'LUNO'");
        this.timeVariantNumber = timeVariantNumber;
        this.isTopOfReceipt = isTopOfReceipt;
        this.messageCoordinationNumber = Chars.validateRange(messageCoordinationNumber, '1', '~',
                "'Message Coordination Number'");
        this.track2Data = TrackDataBuffer.validateTrackData(track2Data, 39, "Track 2 Data");
        this.track3Data = TrackDataBuffer.validateTrackData(track3Data, 106, "Track 3 Data");
        this.operationCodeData = validateOperationCodeData(operationCodeData);
        this.pinBufferA = validateBuffer(pinBufferA, "'Pin Buffer A'");
        this.bufferC = validateBuffer(bufferC, "'General Purpose Buffer C'");
        this.exitsBufferMap = Map.copyOf(exitsBufferMap);
        this.optionalDataFieldsMap = new LinkedHashMap<>(optionalDataFieldsMap);
        this.mac = MacReaderBase.validateMac(mac);
        this.amountEntry = amountEntry;
        this.bufferB = bufferB;
        this.track1Data = track1Data;
        this.lastTransactionStatusData = lastTransactionStatusData;
        this.cspData = cspData;
        this.confirmationCspData = confirmationCspData;
        this.smartCardBuffer = smartCardBuffer;
        this.cashAcceptorBuffer = cashAcceptorBuffer;
        this.documentBuffer = documentBuffer;
        this.suspectNotesDataBuffer = suspectNotesDataBuffer;
        this.counterfeitNotesDataBuffer = counterfeitNotesDataBuffer;
        this.barCodeBuffer = barCodeBuffer;
        this.coinHoppersBuffer = coinHoppersBuffer;
        this.bunchChequeDepositBuffer = bunchChequeDepositBuffer;
        this.voiceGuidanceBuffer = voiceGuidanceBuffer;
    }

    TransactionRequestMessage(Luno luno,
                              long timeVariantNumber,
                              boolean isTopOfReceipt,
                              char messageCoordinationNumber,
                              String track2Data,
                              String track3Data,
                              String operationCodeData,
                              AmountEntry amountEntry,
                              String pinBufferA,
                              BufferB bufferB,
                              String bufferC,
                              Track1DataBuffer track1Data,
                              LastTransactionStatusData lastTransactionStatusData,
                              CspData cspData,
                              CspData confirmationCspData,
                              SmartCardBuffer smartCardBuffer,
                              CashAcceptorBuffer cashAcceptorBuffer,
                              DocumentBuffer documentBuffer,
                              NotesDataBuffer suspectNotesDataBuffer,
                              NotesDataBuffer counterfeitNotesDataBuffer,
                              BarCodeBuffer barCodeBuffer,
                              CoinHoppersBuffer coinHoppersBuffer,
                              BunchChequeDepositBuffer bunchChequeDepositBuffer,
                              VoiceGuidanceBuffer voiceGuidanceBuffer,
                              Map<Character, IdentifiableBuffer> exitsBufferMap,
                              LinkedHashMap<Character, IdentifiableBuffer> optionalDataFieldsMap,
                              String mac,
                              Void unused) {
        this.luno = luno;
        this.timeVariantNumber = timeVariantNumber;
        this.isTopOfReceipt = isTopOfReceipt;
        this.messageCoordinationNumber = messageCoordinationNumber;
        this.track2Data = track2Data;
        this.track3Data = track3Data;
        this.operationCodeData = operationCodeData;
        this.amountEntry = amountEntry;
        this.pinBufferA = pinBufferA;
        this.bufferC = bufferC;
        this.bufferB = bufferB;
        this.track1Data = track1Data;
        this.lastTransactionStatusData = lastTransactionStatusData;
        this.cspData = cspData;
        this.confirmationCspData = confirmationCspData;
        this.smartCardBuffer = smartCardBuffer;
        this.cashAcceptorBuffer = cashAcceptorBuffer;
        this.documentBuffer = documentBuffer;
        this.suspectNotesDataBuffer = suspectNotesDataBuffer;
        this.counterfeitNotesDataBuffer = counterfeitNotesDataBuffer;
        this.barCodeBuffer = barCodeBuffer;
        this.coinHoppersBuffer = coinHoppersBuffer;
        this.bunchChequeDepositBuffer = bunchChequeDepositBuffer;
        this.voiceGuidanceBuffer = voiceGuidanceBuffer;
        this.exitsBufferMap = Map.copyOf(exitsBufferMap);
        this.optionalDataFieldsMap = new LinkedHashMap<>(optionalDataFieldsMap);
        this.mac = mac;
    }

    public TransactionRequestMessageBuilder copy() {
        return new TransactionRequestMessageBuilder()
                .withLuno(luno)
                .withTimeVariantNumber(timeVariantNumber)
                .withTopOfReceipt(isTopOfReceipt)
                .withMessageCoordinationNumber(messageCoordinationNumber)
                .withTrack2Data(track2Data)
                .withTrack3Data(track3Data)
                .withOperationCodeData(operationCodeData)
                .withAmountEntry(amountEntry)
                .withPinBufferA(pinBufferA)
                .withBufferB(bufferB)
                .withBufferC(bufferC)
                .withTrack1Data(track1Data)
                .withLastTransactionStatusData(lastTransactionStatusData)
                .withCspData(cspData)
                .withConfirmationCspData(confirmationCspData)
                .withSmartCardBuffer(smartCardBuffer)
                .withCashAcceptorBuffer(cashAcceptorBuffer)
                .withDocumentBuffer(documentBuffer)
                .withSuspectNotesBuffer(suspectNotesDataBuffer)
                .withCounterfeitNotesBuffer(counterfeitNotesDataBuffer)
                .withBarCodeBuffer(barCodeBuffer)
                .withCoinHoppersBuffer(coinHoppersBuffer)
                .withBunchChequeDepositBuffer(bunchChequeDepositBuffer)
                .withVoiceGuidanceBuffer(voiceGuidanceBuffer)
                .withExitsBufferMap(exitsBufferMap)
                .withOptionalDataFieldsMap(optionalDataFieldsMap)
                .withMac(mac);
    }

    @Override
    public TerminalMessageClass getMessageClass() {
        return TerminalMessageClass.UNSOLICITED;
    }

    @Override
    public TerminalMessageSubClass getMessageSubclass() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE;
    }

    public Luno getLuno() {
        return luno;
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

    public Optional<AmountEntry> getAmountEntry() {
        return Optional.ofNullable(amountEntry);
    }

    public String getPinBufferA() {
        return pinBufferA;
    }

    public Optional<BufferB> getBufferB() {
        return Optional.ofNullable(bufferB);
    }

    public String getBufferC() {
        return bufferC;
    }

    public Optional<Track1DataBuffer> getTrackOneData() {
        return Optional.ofNullable(track1Data);
    }

    public Optional<LastTransactionStatusData> getLastTransactionStatusData() {
        return Optional.ofNullable(lastTransactionStatusData);
    }

    public Optional<CspData> getCspData() {
        return Optional.ofNullable(cspData);
    }

    public Optional<CspData> getConfirmationCspData() {
        return Optional.ofNullable(confirmationCspData);
    }

    public Optional<SmartCardBuffer> getSmartCardData() {
        return Optional.ofNullable(smartCardBuffer);
    }

    public Optional<CashAcceptorBuffer> getCashAcceptorData() {
        return Optional.ofNullable(cashAcceptorBuffer);
    }

    public Optional<DocumentBuffer> getDocumentBuffer() {
        return Optional.ofNullable(documentBuffer);
    }

    public Optional<NotesDataBuffer> getSuspectNotesData() {
        return Optional.ofNullable(suspectNotesDataBuffer);
    }

    public Optional<NotesDataBuffer> getCounterfeitNotesData() {
        return Optional.ofNullable(counterfeitNotesDataBuffer);
    }

    public Optional<BarCodeBuffer> getBarCodeData() {
        return Optional.ofNullable(barCodeBuffer);
    }

    public Optional<CoinHoppersBuffer> getExtendedCoinHopperData() {
        return Optional.ofNullable(coinHoppersBuffer);
    }

    public Optional<BunchChequeDepositBuffer> getBunchChequeDepositData() {
        return Optional.ofNullable(bunchChequeDepositBuffer);
    }

    public Optional<VoiceGuidanceBuffer> getVoiceGuidanceData() {
        return Optional.ofNullable(voiceGuidanceBuffer);
    }

    public OptionalLong getTimeVariantNumber() {
        return timeVariantNumber >= 0 ? OptionalLong.of(timeVariantNumber) : OptionalLong.empty();
    }

    public Optional<IdentifiableBuffer> getIdentifiableBufferById(char id) {
        return Optional.ofNullable(exitsBufferMap.get(id));
    }

    public Map<Character, IdentifiableBuffer> getExitsBufferMap() {
        return exitsBufferMap;
    }

    public Map<Character, IdentifiableBuffer> getOptionalDataFieldsMap() {
        return Collections.unmodifiableMap(optionalDataFieldsMap);
    }

    public String getMac() {
        return mac;
    }


    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(256)
                .appendComponent(TerminalMessageClass.UNSOLICITED)
                .appendComponent(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendFs()
                .appendZeroPaddedHex(timeVariantNumber, 8)
                .appendFs()
                .append(isTopOfReceipt ? '1' : '0')
                .append(messageCoordinationNumber)
                .appendFs()
                .append(track2Data)
                .appendFs()
                .append(track3Data)
                .appendFs()
                .append(operationCodeData)
                .appendFs()
                .appendComponent(amountEntry)
                .appendFs()
                .append(pinBufferA)
                .appendFs()
                .appendComponent(bufferB)
                .appendFs()
                .append(bufferC);

        return joinWithFieldGroups(builder);
    }

    protected CharSequence fieldGroupsToNdcString() {
        return new NdcStringBuilder(128)
                .appendFieldGroup(cspData)
                .appendFieldGroup(confirmationCspData)
                .appendFieldGroup(exitsBufferMap.get('W'))
                .appendFieldGroup(exitsBufferMap.get('X'))
                .appendFieldGroup(exitsBufferMap.get('Y'))
                .appendFieldGroup(exitsBufferMap.get('Z'))
                .appendFieldGroup(exitsBufferMap.get('['))
                .appendFieldGroup(exitsBufferMap.get('\\'))
                .appendFieldGroup(smartCardBuffer)
                .appendFieldGroup(cashAcceptorBuffer)
                .appendFieldGroup(documentBuffer)
                .appendFieldGroup(suspectNotesDataBuffer)
                .appendFieldGroup(counterfeitNotesDataBuffer)
                .appendFieldGroup(barCodeBuffer)
                .appendFieldGroup(coinHoppersBuffer)
                .appendFieldGroup(bunchChequeDepositBuffer)
                .appendFieldGroup(voiceGuidanceBuffer)
                .appendFieldGroup(optionalDataFieldsMap.values())
                .appendFieldGroup(mac);
    }

    protected String joinWithFieldGroups(NdcStringBuilder standardFields) {
        final CharSequence fieldGroups = fieldGroupsToNdcString();
        if (fieldGroups.length() != 0) {
            return standardFields.ensureCapacity(standardFields.length() + fieldGroups.length() + 56)
                    .appendFs()
                    .appendComponent(track1Data)
                    .appendFs()
                    .appendComponent(lastTransactionStatusData)
                    .append(fieldGroups)
                    .toString();
        }
        if (lastTransactionStatusData != null) {
            return standardFields.ensureCapacity(standardFields.length() + 56)
                    .appendFs()
                    .appendComponent(track1Data)
                    .appendFs()
                    .appendComponent(lastTransactionStatusData)
                    .toString();
        }
        if (track1Data != null) {
            standardFields.appendFs().appendComponent(track1Data);
        }
        return standardFields.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionRequestMessage.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + TerminalMessageClass.UNSOLICITED)
                .add("messageSubclass: " + TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE)
                .add("luno: " + luno)
                .add("timeVariantNumber: " + timeVariantNumber)
                .add("isTopOfReceipt: " + isTopOfReceipt)
                .add("messageCoordinationNumber: " + messageCoordinationNumber)
                .add("track2Data: '" + track2Data + '\'')
                .add("track3Data: '" + track3Data + '\'')
                .add("operationCodeData: '" + operationCodeData + '\'')
                .add("amountEntry: " + amountEntry)
                .add("pinBufferA: '" + pinBufferA + '\'')
                .add("bufferB: " + bufferB)
                .add("bufferC: '" + bufferC + '\'')
                .add("track1Data: " + track1Data)
                .add("lastTransactionStatusData: " + lastTransactionStatusData)
                .add("cspData: " + cspData)
                .add("confirmationCspData: " + confirmationCspData)
                .add("smartCardBuffer: " + smartCardBuffer)
                .add("cashAcceptorBuffer: " + cashAcceptorBuffer)
                .add("documentBuffer: " + documentBuffer)
                .add("suspectNotesDataBuffer: " + suspectNotesDataBuffer)
                .add("counterfeitNotesDataBuffer: " + counterfeitNotesDataBuffer)
                .add("barCodeBuffer: " + barCodeBuffer)
                .add("coinHoppersBuffer: " + coinHoppersBuffer)
                .add("bunchChequeDepositBuffer: " + bunchChequeDepositBuffer)
                .add("voiceGuidanceBuffer: " + voiceGuidanceBuffer)
                .add("exitsBufferMap: " + exitsBufferMap)
                .add("optionalDataFieldsMap: " + optionalDataFieldsMap)
                .add("MAC: '" + mac + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(o.getClass())) return false;
        TransactionRequestMessage that = (TransactionRequestMessage) o;
        return isTopOfReceipt == that.isTopOfReceipt &&
                messageCoordinationNumber == that.messageCoordinationNumber &&
                ((timeVariantNumber < 0 && that.timeVariantNumber < 0) || timeVariantNumber == that.timeVariantNumber) &&
                mac.equals(that.mac) &&
                track2Data.equals(that.track2Data) &&
                track3Data.equals(that.track3Data) &&
                operationCodeData.equals(that.operationCodeData) &&
                pinBufferA.equals(that.pinBufferA) &&
                bufferC.equals(that.bufferC) &&
                Objects.equals(amountEntry, that.amountEntry) &&
                exitsBufferMap.equals(that.exitsBufferMap) &&
                optionalDataFieldsMap.equals(that.optionalDataFieldsMap) &&
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
                Objects.equals(voiceGuidanceBuffer, that.voiceGuidanceBuffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Math.max(timeVariantNumber, -1),
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
                exitsBufferMap,
                smartCardBuffer,
                cashAcceptorBuffer,
                documentBuffer,
                suspectNotesDataBuffer,
                counterfeitNotesDataBuffer,
                barCodeBuffer,
                coinHoppersBuffer,
                bunchChequeDepositBuffer,
                voiceGuidanceBuffer,
                optionalDataFieldsMap,
                mac);
    }

    private String validateOperationCodeData(String operationCodeData) {
        ObjectUtils.validateNotNull(operationCodeData, "'Operation Code Data'");
        final int length = operationCodeData.length();
        if (length > 0 && length != 8) {
            throw new IllegalArgumentException("'Operation Code Data' must be 8 characters long");
        }
        return operationCodeData;
    }

    private String validateBuffer(String value, String name) {
        ObjectUtils.validateNotNull(value, name);
        Integers.validateRange(value.length(), 0, 32, name + " length");
        return value;
    }
}
