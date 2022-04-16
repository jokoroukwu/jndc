package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.ResponseFlag;
import io.github.jokoroukwu.jndc.central.CentralMessage;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.cardflag.CardFlag;
import io.github.jokoroukwu.jndc.central.transactionreply.chequedestination.ChequeDestinationBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense.CoinsToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.depositlimit.DepositLimitsBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.FunctionId;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.StandardFunction;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.MultiChequeBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.notestodispense.NotesToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterData;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterDataList;
import io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate.ScreenDisplayUpdate;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.SequenceTimeVariantNumber;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.TransactionReplySmartCardBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TransactionUpdateBuffer;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBuffer;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track2DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track3DataBuffer;
import io.github.jokoroukwu.jndc.util.*;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class TransactionReplyCommand implements CentralMessage {
    protected final char responseFlag;
    protected final Luno luno;
    protected final SequenceTimeVariantNumber sequenceTimeVariantNumber;
    protected final String nextStateIdData;
    protected final NotesToDispense notesToDispense;
    protected final CoinsToDispense coinsToDispense;
    protected final int transactionSerialNumber;
    protected final FunctionId functionId;
    protected final ScreenDisplayUpdate screenDisplayUpdate;
    protected final char messageCoordinationNumber;
    protected final CardFlag cardFlag;
    protected final PrinterDataList printerDataList;
    protected final Track3DataBuffer track3DataBuffer;
    protected final Track1DataBuffer track1DataBuffer;
    protected final Track2DataBuffer track2DataBuffer;
    protected final TransactionReplySmartCardBuffer smartCardBuffer;
    protected final ChequeDestinationBuffer chequeDestinationBuffer;
    protected final MultiChequeBuffer multiChequeBuffer;
    protected final TransactionUpdateBuffer transactionUpdateBuffer;
    protected final DepositLimitsBuffer depositLimitsBuffer;
    protected final Map<Character, IdentifiableBuffer> exitsBufferMap;
    protected final String mac;
    private final GenericBuffer bufferS;

    public TransactionReplyCommand(char responseFlag,
                                   Luno luno,
                                   SequenceTimeVariantNumber sequenceTimeVariantNumber,
                                   String nextStateIdData,
                                   NotesToDispense notesToDispense,
                                   CoinsToDispense coinsToDispense,
                                   int transactionSerialNumber,
                                   FunctionId functionId,
                                   ScreenDisplayUpdate screenDisplayUpdate,
                                   char messageCoordinationNumber,
                                   CardFlag cardFlag,
                                   PrinterDataList printerDataList,
                                   Track3DataBuffer track3DataBuffer,
                                   Track1DataBuffer track1DataBuffer,
                                   Track2DataBuffer track2DataBuffer,
                                   TransactionReplySmartCardBuffer smartCardBuffer,
                                   ChequeDestinationBuffer chequeDestinationBuffer,
                                   MultiChequeBuffer multiChequeBuffer,
                                   TransactionUpdateBuffer transactionUpdateBuffer,
                                   DepositLimitsBuffer depositLimitsBuffer,
                                   GenericBuffer bufferS,
                                   Map<Character, IdentifiableBuffer> exitsBufferMap,
                                   String mac) {
        this.responseFlag = ResponseFlag.validateResponseFlag(responseFlag);
        this.luno = ObjectUtils.validateNotNull(luno, "LUNO");
        this.functionId = ObjectUtils.validateNotNull(functionId, "Function ID");
        this.nextStateIdData = validateNextStateId(nextStateIdData, functionId);
        this.notesToDispense = ObjectUtils.validateNotNull(notesToDispense, "Notes to Dispense");
        this.coinsToDispense = ObjectUtils.validateNotNull(coinsToDispense, "Coins to Dispense");
        this.transactionSerialNumber = Integers.validateRange(transactionSerialNumber, 0, 9999, "Transaction Serial Number");
        this.cardFlag = ObjectUtils.validateNotNull(cardFlag, "Card Flag");
        this.printerDataList = CollectionUtils.requireNonNullNonEmpty(printerDataList, "Printer Data List");
        this.chequeDestinationBuffer = chequeDestinationBuffer;
        this.multiChequeBuffer = multiChequeBuffer;
        this.transactionUpdateBuffer = transactionUpdateBuffer;
        this.depositLimitsBuffer = depositLimitsBuffer;
        this.mac = MacReaderBase.validateMac(mac);
        this.exitsBufferMap = Map.copyOf(exitsBufferMap);
        this.messageCoordinationNumber = Chars.validateRange(messageCoordinationNumber, '1', '~', "Message Coordination Number");
        this.screenDisplayUpdate = screenDisplayUpdate;
        this.track3DataBuffer = track3DataBuffer;
        this.track1DataBuffer = track1DataBuffer;
        this.track2DataBuffer = track2DataBuffer;
        this.smartCardBuffer = smartCardBuffer;
        this.sequenceTimeVariantNumber = sequenceTimeVariantNumber;
        this.bufferS = bufferS;
    }

    public TransactionReplyCommandBuilder copy() {
        return new TransactionReplyCommandBuilder()
                .withLuno(luno)
                .withResponseFlag(responseFlag)
                .withMessageCoordinationNumber(messageCoordinationNumber)
                .withNextStateIdData(nextStateIdData)
                .withNotesToDispense(notesToDispense)
                .withCoinsToDispense(coinsToDispense)
                .withSequenceTimeVariantNumber(sequenceTimeVariantNumber)
                .withTransactionSerialNumber(transactionSerialNumber)
                .withScreenDisplayUpdate(screenDisplayUpdate)
                .withFunctionId(functionId)
                .withCardFlag(cardFlag)
                .withPrinterDataList(printerDataList)
                .withChequeDestinationBuffer(chequeDestinationBuffer)
                .withMultiChequeBuffer(multiChequeBuffer)
                .withTransactionUpdateBuffer(transactionUpdateBuffer)
                .withDepositLimitsBuffer(depositLimitsBuffer)
                .withExitsBufferMap(exitsBufferMap)
                .withBufferS(bufferS)
                .withTrack3DataBuffer(track3DataBuffer)
                .withTrack1DataBuffer(track1DataBuffer)
                .withTrack2DataBuffer(track2DataBuffer)
                .withSmartCardBuffer(smartCardBuffer)
                .withMac(mac);
    }

    public char getResponseFlag() {
        return responseFlag;
    }

    public Luno getLuno() {
        return luno;
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

    public Optional<ScreenDisplayUpdate> getScreenDisplayUpdate() {
        return Optional.ofNullable(screenDisplayUpdate);
    }

    public char getMessageCoordinationNumber() {
        return messageCoordinationNumber;
    }

    public CardFlag getCardFlag() {
        return cardFlag;
    }

    public Optional<Track1DataBuffer> getTrack1DataBuffer() {
        return Optional.ofNullable(track1DataBuffer);
    }

    public Optional<Track3DataBuffer> getTrack3DataBuffer() {
        return Optional.ofNullable(track3DataBuffer);
    }

    public Optional<Track2DataBuffer> getTrack2DataBuffer() {
        return Optional.ofNullable(track2DataBuffer);
    }

    public Optional<TransactionReplySmartCardBuffer> getSmartCardBuffer() {
        return Optional.ofNullable(smartCardBuffer);
    }

    public Optional<ChequeDestinationBuffer> getChequeDestinationBuffer() {
        return Optional.ofNullable(chequeDestinationBuffer);
    }

    public Optional<MultiChequeBuffer> getMultiChequeBuffer() {
        return Optional.ofNullable(multiChequeBuffer);
    }

    public Optional<TransactionUpdateBuffer> getTransactionUpdateBuffer() {
        return Optional.ofNullable(transactionUpdateBuffer);
    }

    public Optional<DepositLimitsBuffer> getDepositLimitsBuffer() {
        return Optional.ofNullable(depositLimitsBuffer);
    }

    public Optional<GenericBuffer> getBufferS() {
        return Optional.ofNullable(bufferS);
    }

    public String getMac() {
        return mac;
    }

    public List<PrinterData> getPrinterDataList() {
        return Collections.unmodifiableList(printerDataList);
    }

    public Map<Character, IdentifiableBuffer> getExitsBufferMap() {
        return exitsBufferMap;
    }

    public Optional<IdentifiableBuffer> exitsBufferById(char id) {
        return Optional.ofNullable(exitsBufferMap.get(id));
    }

    public Optional<SequenceTimeVariantNumber> getSequenceTimeVariantNumber() {
        return Optional.ofNullable(sequenceTimeVariantNumber);
    }

    @Override
    public CentralMessageClass getMessageClass() {
        return CentralMessageClass.TRANSACTION_REPLY_COMMAND;
    }

    @Override
    public String toNdcString() {
        //  some fields contain huge chunks of data
        //  so enough space should be allocated
        return new NdcStringBuilder(512)
                .appendComponent(CentralMessageClass.TRANSACTION_REPLY_COMMAND)
                .append(responseFlag)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendComponent(sequenceTimeVariantNumber)
                .appendFs()
                .append(nextStateIdData)
                .appendFs()
                .appendComponent(notesToDispense)
                .appendComponent(NdcConstants.GROUP_SEPARATOR_STRING, coinsToDispense)
                .appendFs()
                .appendZeroPadded(transactionSerialNumber, 4)
                .appendComponent(functionId)
                .appendComponent(screenDisplayUpdate)
                .appendFs()
                .append(messageCoordinationNumber)
                .appendComponent(cardFlag)
                .appendComponent(printerDataList)
                .appendFieldGroup(track3DataBuffer)
                .appendFieldGroup(track1DataBuffer)
                .appendFieldGroup(track2DataBuffer)
                .appendFieldGroup(exitsBufferMap.get('M'))
                .appendFieldGroup(exitsBufferMap.get('N'))
                .appendFieldGroup(exitsBufferMap.get('O'))
                .appendFieldGroup(exitsBufferMap.get('P'))
                .appendFieldGroup(exitsBufferMap.get('Q'))
                .appendFieldGroup(exitsBufferMap.get('R'))
                .appendFieldGroup(bufferS)
                .appendFieldGroup(smartCardBuffer)
                .appendFieldGroup(chequeDestinationBuffer)
                .appendFieldGroup(multiChequeBuffer)
                .appendFieldGroup(transactionUpdateBuffer)
                .appendFieldGroup(depositLimitsBuffer)
                .appendFieldGroup(mac)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionReplyCommand.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + CentralMessageClass.TRANSACTION_REPLY_COMMAND)
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
                .add("exitsBuffers: " + exitsBufferMap.values())
                .add("MAC: '" + mac + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionReplyCommand that = (TransactionReplyCommand) o;
        return responseFlag == that.responseFlag &&
                transactionSerialNumber == that.transactionSerialNumber &&
                messageCoordinationNumber == that.messageCoordinationNumber &&
                cardFlag == that.cardFlag &&
                chequeDestinationBuffer == that.chequeDestinationBuffer &&
                functionId.equals(that.functionId) &&
                mac.equals(that.mac) &&
                nextStateIdData.equals(that.nextStateIdData) &&
                exitsBufferMap.equals(that.exitsBufferMap) &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(screenDisplayUpdate, that.screenDisplayUpdate) &&
                Objects.equals(sequenceTimeVariantNumber, that.sequenceTimeVariantNumber) &&
                Objects.equals(notesToDispense, that.notesToDispense) &&
                Objects.equals(coinsToDispense, that.coinsToDispense) &&
                Objects.equals(printerDataList, that.printerDataList) &&
                Objects.equals(track3DataBuffer, that.track3DataBuffer) &&
                Objects.equals(track1DataBuffer, that.track1DataBuffer) &&
                Objects.equals(track2DataBuffer, that.track2DataBuffer) &&
                Objects.equals(smartCardBuffer, that.smartCardBuffer) &&
                Objects.equals(multiChequeBuffer, that.multiChequeBuffer) &&
                Objects.equals(transactionUpdateBuffer, that.transactionUpdateBuffer) &&
                Objects.equals(depositLimitsBuffer, that.depositLimitsBuffer);

    }

    @Override
    public int hashCode() {
        return Objects.hash(CentralMessageClass.TRANSACTION_REPLY_COMMAND,
                responseFlag, luno, sequenceTimeVariantNumber, nextStateIdData, notesToDispense,
                coinsToDispense, transactionSerialNumber, functionId, screenDisplayUpdate,
                messageCoordinationNumber, cardFlag, printerDataList, track3DataBuffer, track1DataBuffer, track2DataBuffer,
                smartCardBuffer, chequeDestinationBuffer, multiChequeBuffer, transactionUpdateBuffer, depositLimitsBuffer,
                exitsBufferMap, mac);
    }


    private String validateNextStateId(String nextStateId, FunctionId functionId) {
        ObjectUtils.validateNotNull(nextStateId, "Next State ID Data");
        if (functionId.getId() == StandardFunction.PRINT_IMMEDIATE.getId()) {
            if (nextStateId.isEmpty()) {
                return nextStateId;
            }
            throw new IllegalArgumentException("'Next State ID Data' must be absent when 'Function ID' is: " + functionId);
        }
        Integers.validateIsExactValue(nextStateId.length(), 3, "Next State ID Data length");
        if ("255".equals(nextStateId) || !Strings.isAlphanumeric(nextStateId)) {
            throw new IllegalArgumentException("'Next State ID Data' must be in range 000-254 or 256-ZZZ but was:" + nextStateId);
        }
        return nextStateId;
    }
}
