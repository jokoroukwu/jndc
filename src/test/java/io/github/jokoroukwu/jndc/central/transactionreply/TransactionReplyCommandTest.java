package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.transactionreply.cardflag.CardFlag;
import io.github.jokoroukwu.jndc.central.transactionreply.chequedestination.ChequeDestinationBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense.CoinsToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.depositlimit.DepositLimitsBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.FunctionId;
import io.github.jokoroukwu.jndc.central.transactionreply.functionid.StandardFunction;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.Cheque;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.ChequeStamp;
import io.github.jokoroukwu.jndc.central.transactionreply.multicheque.MultiChequeBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.notestodispense.NotesToDispense;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterData;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterDataList;
import io.github.jokoroukwu.jndc.central.transactionreply.printerdata.PrinterFlag;
import io.github.jokoroukwu.jndc.central.transactionreply.screendisplayupdate.ScreenDisplayUpdate;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.SequenceTimeVariantNumber;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.TransactionReplySmartCardBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TargetBufferId;
import io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate.TransactionUpdateBuffer;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBuffer;
import io.github.jokoroukwu.jndc.screen.Screen;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track2DataBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track3DataBuffer;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator.*;

public class TransactionReplyCommandTest {
    private final char responseFlag = HEX.randomChar();
    private final Luno luno = Luno.DEFAULT;
    private final SequenceTimeVariantNumber sequenceNumber = SequenceTimeVariantNumber.sequence(1);
    private final String nextStateId = ALPHANUMERIC.fixedLength(3);
    private final NotesToDispense notesToDispense = NotesToDispense.twoDigit(1);
    private final CoinsToDispense coinsToDispense = CoinsToDispense.of(1);
    private final int transactionSerialNumber = 1;
    private final String transactionSerialNumberString = "0001";
    private final FunctionId functionId = StandardFunction.PRINT_MEDIA_SET_NEXT_STATE;
    private final String screenNumber = "010";
    private final Screen screen = new Screen("002", HEX.fixedLength(10));
    private final ScreenDisplayUpdate screenDisplayUpdate = new ScreenDisplayUpdate(screenNumber);
    private final char msgCoordinationNumber = '1';
    private final CardFlag cardFlag = CardFlag.RETAIN;
    private final PrinterData printerData = new PrinterData(PrinterFlag.RECEIPT_SIDEWAYS, HEX.fixedLength(5));
    private final Track3DataBuffer track3DataBuffer = new Track3DataBuffer(ofCharacterRange('0', '?').fixedLength(5));
    private final Track1DataBuffer track1DataBuffer =
            Track1DataBuffer.replyBuffer(ofCharacterRange(' ', '_').fixedLength(5));
    private final Track2DataBuffer track2DataBuffer = new Track2DataBuffer(ofCharacterRange('0', '?').fixedLength(5));

    private final GenericBuffer bufferM = new GenericBuffer('M', "m");
    private final GenericBuffer bufferN = new GenericBuffer('N', "n");
    private final GenericBuffer bufferO = new GenericBuffer('O', "o");
    private final GenericBuffer bufferP = new GenericBuffer('P', "p");
    private final GenericBuffer bufferQ = new GenericBuffer('Q', "q");
    private final GenericBuffer bufferR = new GenericBuffer('R', "r");
    private final Map<Character, IdentifiableBuffer> exitsBufferMap = Map.of(
            'M', bufferM,
            'N', bufferN,
            'O', bufferO,
            'P', bufferP,
            'Q', bufferQ,
            'R', bufferR
    );
    private final GenericBuffer bufferS = new GenericBuffer('S', "S");
    private final TransactionReplySmartCardBuffer smartCardBuffer =
            new TransactionReplySmartCardBuffer(List.of(), null, null);

    private final ChequeDestinationBuffer chequeDestinationBuffer = ChequeDestinationBuffer.POCKET_1;
    private final Cheque cheque = new Cheque(1, 1, ChequeStamp.STAMP, "cheque-text");
    private final TransactionUpdateBuffer transactionUpdateBuffer =
            new TransactionUpdateBuffer(TargetBufferId.NO_BUFFER, EMPTY_STRING, null, null);

    private final DepositLimitsBuffer depositLimitsBuffer = new DepositLimitsBuffer(50);
    private final String mac = HEX.fixedLength(8);
    final TransactionReplyCommand allFieldsMessage = new TransactionReplyCommandBuilder()
            .withLuno(luno)
            .withResponseFlag(responseFlag)
            .withSequenceTimeVariantNumber(sequenceNumber)
            .withNextStateIdData(nextStateId)
            .withNotesToDispense(notesToDispense)
            .withCoinsToDispense(coinsToDispense)
            .withTransactionSerialNumber(transactionSerialNumber)
            .withFunctionId(functionId)
            .withScreenDisplayUpdate(screenDisplayUpdate)
            .withMessageCoordinationNumber(msgCoordinationNumber)
            .withCardFlag(cardFlag)
            .withPrinterDataList(PrinterDataList.of(printerData))
            .withTrack3DataBuffer(track3DataBuffer)
            .withTrack1DataBuffer(track1DataBuffer)
            .withTrack2DataBuffer(track2DataBuffer)
            .withExitsBufferMap(exitsBufferMap)
            .withBufferS(bufferS)
            .withSmartCardBuffer(smartCardBuffer)
            .withChequeDestinationBuffer(chequeDestinationBuffer)
            .withMultiChequeBuffer(MultiChequeBuffer.of(cheque))
            .withTransactionUpdateBuffer(transactionUpdateBuffer)
            .withDepositLimitsBuffer(depositLimitsBuffer)
            .withMac(mac)
            .build();

    @DataProvider
    public Object[][] validDataProvider() {
        final String withSequenceNumberString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoSequenceNumber = allFieldsMessage.copy()
                .withSequenceTimeVariantNumber(null)
                .build();
        var withNoSequenceNumberString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoNextStateId = allFieldsMessage.copy()
                //  next state ID data may only be absent when Print Immediate function is used
                .withFunctionId(StandardFunction.PRINT_IMMEDIATE)
                .withNextStateIdData(EMPTY_STRING)
                .build();
        var withNoNextStateIdString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                //  should be Print Immediate
                + StandardFunction.PRINT_IMMEDIATE.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;
        var withNoCoinsToDispense = allFieldsMessage.copy()
                .withCoinsToDispense(CoinsToDispense.EMPTY)
                .build();
        var withNoCoinsToDispenseString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoScreenDisplayUpdate = allFieldsMessage.copy()
                .withScreenDisplayUpdate(null)
                .build();
        var withNoScreenDisplayUpdateString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withMultipleScreens = allFieldsMessage.copy()
                .withScreenDisplayUpdate(new ScreenDisplayUpdate(screenNumber, Collections.nCopies(3, screen)))
                .build();
        var withMultipleScreensString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenNumber
                + screen.toNdcString()
                //  GS and 4 reserved bytes should prepend
                //  each additional screen
                + NdcConstants.GROUP_SEPARATOR
                + "0000"
                + screen.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + "0000"
                + screen.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoTrack3Data = allFieldsMessage.copy()
                .withTrack3DataBuffer(null)
                .build();
        var withNoTrack3DataString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoTrack1Data = allFieldsMessage.copy()
                .withTrack1DataBuffer(null)
                .build();
        var withNoTrack1DataString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoTrack2Data = allFieldsMessage.copy()
                .withTrack2DataBuffer(null)
                .build();
        var withNoTrack2DataString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoExitsBuffers = allFieldsMessage.copy()
                .withExitsBufferMap(Map.of())
                .build();
        var withNoExitsBuffersString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoBufferS = allFieldsMessage.copy()
                .withBufferS(null)
                .build();
        var withNoBufferSString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoSmartCardBuffer = allFieldsMessage.copy()
                .withSmartCardBuffer(null)
                .build();
        var withNoSmartCardBufferString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoMultiChequeBuffer = allFieldsMessage.copy()
                .withMultiChequeBuffer(null)
                .build();
        var withNoMultiChequeBufferString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;
        var withNoChequeDestinationBuffer = allFieldsMessage.copy()
                .withChequeDestinationBuffer(null)
                .build();
        var withNoChequeDestinationBufferString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoTransactionUpdateBuffer = allFieldsMessage.copy()
                .withTransactionUpdateBuffer(null)
                .build();
        var withNoTransactionUpdateBufferString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;
        var withNoDepositLimitsBuffer = allFieldsMessage.copy()
                .withDepositLimitsBuffer(null)
                .build();
        var withNoDepositLimitsBufferString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        var withNoMac = allFieldsMessage.copy()
                .withMac(EMPTY_STRING)
                .build();
        var withNoMacString = CentralMessageClass.TRANSACTION_REPLY_COMMAND.toNdcString()
                + responseFlag
                + NdcConstants.FIELD_SEPARATOR_STRING
                + luno.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + sequenceNumber.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + nextStateId
                + NdcConstants.FIELD_SEPARATOR
                + notesToDispense.toNdcString()
                + NdcConstants.GROUP_SEPARATOR
                + coinsToDispense.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionSerialNumberString
                + functionId.toNdcString()
                + screenDisplayUpdate.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + msgCoordinationNumber
                + cardFlag.toNdcString()
                + printerData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track3DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track1DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + track2DataBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferM.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferN.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferO.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferP.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferQ.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferR.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + bufferS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + smartCardBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + chequeDestinationBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + MultiChequeBuffer.of(cheque).toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + transactionUpdateBuffer.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + depositLimitsBuffer.toNdcString();

        return new Object[][]{
                {"all fields", allFieldsMessage, withSequenceNumberString},
                {"no sequence number", withNoSequenceNumber, withNoSequenceNumberString},
                {"no next state data ID", withNoNextStateId, withNoNextStateIdString},
                {"no coins to dispense", withNoCoinsToDispense, withNoCoinsToDispenseString},
                {"no screen number", withNoScreenDisplayUpdate, withNoScreenDisplayUpdateString},
                {"multiple screens", withMultipleScreens, withMultipleScreensString},
                {"no track3 data", withNoTrack3Data, withNoTrack3DataString},
                {"no track1 data", withNoTrack1Data, withNoTrack1DataString},
                {"no track2 data", withNoTrack2Data, withNoTrack2DataString},
                {"no exits buffers", withNoExitsBuffers, withNoExitsBuffersString},
                {"no 'S' buffer", withNoBufferS, withNoBufferSString},
                {"no smart card buffer", withNoSmartCardBuffer, withNoSmartCardBufferString},
                {"no multi-cheque buffer", withNoMultiChequeBuffer, withNoMultiChequeBufferString},
                {"no cheque destination buffer", withNoChequeDestinationBuffer, withNoChequeDestinationBufferString},
                {"no transaction update buffer", withNoTransactionUpdateBuffer, withNoTransactionUpdateBufferString},
                {"no deposit limits buffer", withNoDepositLimitsBuffer, withNoDepositLimitsBufferString},
                {"no MAC", withNoMac, withNoMacString}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(String description, TransactionReplyCommand message, String expectedString) {
        System.out.println(description);
        Assertions.assertThat(message.toNdcString())
                .isEqualTo(expectedString);
    }

    @Test
    public void copy_should_be_equal_to_original() {
        Assertions.assertThat(allFieldsMessage.copy().build())
                .isEqualTo(allFieldsMessage);
    }
}
