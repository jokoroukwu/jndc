package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBuffer;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.BufferB;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry.AmountEntry;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata.BarCodeBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata.BarcodeData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.BunchChequeDepositBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.BunchChequeDepositData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.CurrencyData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit.DepositedCheque;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.CashAcceptorBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.CashAcceptorNote;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers.CoinHoppersBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata.CspData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata.DocumentBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata.SingleChequeDepositData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata.Note;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata.NotesDataBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata.SmartCardBuffer;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata.SmartCardData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositData;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.DepositTransactionDirection;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.LastStatusIssued;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.LastTransactionStatusDataBase;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance.VoiceGuidanceBuffer;
import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;

public class TransactionRequestMessageTest {

    @Test
    public void should_throw_expected_exception_on_invalid_coordination_number() {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber((char) 0);
        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message Coordination Number");
    }

    @DataProvider
    public Object[][] invalidTrack2DataProvider() {
        final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x30, 0x3F);
        final IStringGenerator invalidRange = BmpStringGenerator.ofCharacterRanges(0, 0x2F, 0x40, 0x7F);
        return new Object[][]{
                {validRange.fixedLength(40)},
                {validRange.fixedLength(41)},
                {invalidRange.randomLength(39)},
                {null},
        };
    }

    @Test(dataProvider = "invalidTrack2DataProvider")
    public void should_throw_expected_exception_on_invalid_track2_data(String invalidTrack2Data) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withTrack2Data(invalidTrack2Data);
        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Track 2 Data");
    }

    @DataProvider
    public Object[][] validTrack2DataProvider() {
        final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x30, 0x3F);
        return new Object[][]{
                {validRange.fixedLength(39)},
                {validRange.fixedLength(38)},
                {EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validTrack2DataProvider")
    public void should_not_throw_exception_on_valid_track2_data(String validTrack2Data) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withTrack2Data(validTrack2Data);

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();
    }


    @DataProvider
    public Object[][] invalidTrack3DataProvider() {
        final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x30, 0x3F);
        final IStringGenerator invalidRange = BmpStringGenerator.ofCharacterRanges(0, 0x2F, 0x40, 0x7F);
        return new Object[][]{
                {validRange.fixedLength(107)},
                {validRange.fixedLength(108)},
                {invalidRange.randomLength(106)},
                {null},
        };
    }

    @Test(dataProvider = "invalidTrack3DataProvider")
    public void should_throw_expected_exception_on_invalid_track3_data(String invalidTrack3Data) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withTrack3Data(invalidTrack3Data);
        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Track 3 Data");
    }


    @DataProvider
    public Object[][] validTrack3DataProvider() {
        final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x30, 0x3F);
        return new Object[][]{
                {validRange.fixedLength(106)},
                {validRange.fixedLength(105)},
                {EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validTrack3DataProvider")
    public void should_not_throw_exception_on_valid_track3_data(String validTrack3Data) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withTrack3Data(validTrack3Data);

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidOperationCodeDataProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(9)},
                {BmpStringGenerator.HEX.fixedLength(7)},
                {null}
        };
    }

    @Test(dataProvider = "invalidOperationCodeDataProvider")
    public void should_throw_expected_exception_on_invalid_operation_code_data(String invalidOperationCodeData) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withOperationCodeData(invalidOperationCodeData);

        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Operation Code Data");
    }

    @DataProvider
    public Object[][] validOperationCodeDataProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(8)},
                {EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validOperationCodeDataProvider")
    public void should_not_throw_exception_on_valid_operation_code_data(String validOperationCodeData) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withOperationCodeData(validOperationCodeData);

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidAmountEntryProvider() {
        return new Object[][]{
                {1_000_000_000_000L},
                {1_000_000_000_001L}
        };
    }


    @DataProvider
    public Object[][] validAmountEntryProvider() {
        return new Object[][]{
                {-1},
                {0},
                {1},
                {999_999_999_999L}
        };
    }


    @DataProvider
    public Object[][] invalidBufferAProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(33)},
                {BmpStringGenerator.HEX.fixedLength(34)},
                {null}
        };
    }

    @Test(dataProvider = "invalidBufferAProvider")
    public void should_throw_expected_exception_on_invalid_pin_buffer_a(String invalidBufferA) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withPinBufferA(invalidBufferA);

        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pin Buffer A");
    }


    @DataProvider
    public Object[][] validBufferAProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(32)},
                {BmpStringGenerator.HEX.fixedLength(31)},
                {EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validBufferAProvider")
    public void should_not_throw_exception_on_valid_pin_buffer_a(String validBufferA) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withPinBufferA(validBufferA);

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] invalidBufferCProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(33)},
                {BmpStringGenerator.HEX.fixedLength(34)},
                {null}
        };
    }

    @Test(dataProvider = "invalidBufferCProvider")
    public void should_throw_expected_exception_on_invalid_buffer_c(String invalidBufferC) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withBufferC(invalidBufferC);

        Assertions.assertThatThrownBy(builder::build)
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("General Purpose Buffer C");
    }

    @DataProvider
    public Object[][] validBufferCProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(32)},
                {BmpStringGenerator.HEX.fixedLength(31)},
                {EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validBufferCProvider")
    public void should_not_throw_exception_on_valid_buffer_c(String validBufferC) {
        final TransactionRequestMessageBuilder builder = new TransactionRequestMessageBuilder()
                .withMessageCoordinationNumber('1')
                .withBufferC(validBufferC);

        Assertions.assertThatCode(builder::build)
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] expectedStringProvider() {
        final long timeVariantNumber = 0xFF;
        final String track2Data = BmpStringGenerator.ofCharacterRange(0x30, 0x3F).randomLength(39);
        final String track3Data = BmpStringGenerator.ofCharacterRange(0x30, 0x3F).randomLength(106);
        final String operationCodeData = BmpStringGenerator.HEX.fixedLength(8);
        final AmountEntry amountEntryField = AmountEntry.extended(ThreadLocalRandom.current().nextLong(1_000_000_000_000L));
        final String pinBufferA = BmpStringGenerator.HEX.randomLength(4, 32);
        final BufferB bufferB = new BufferB(BmpStringGenerator.HEX.randomLength(3, 32));
        final String bufferC = BmpStringGenerator.HEX.randomLength(32);
        final Track1DataBuffer track1Data =
                Track1DataBuffer.requestBuffer(BmpStringGenerator.ofCharacterRange(0x20, 0x5F).randomLength(78));

        final CspData cspData = CspData.csp(BmpStringGenerator.HEX.fixedLength(16));
        final CspData confirmationCspData = CspData.confirmationCsp(BmpStringGenerator.HEX.fixedLength(16));
        final Map<Character, IdentifiableBuffer> bufferMap = Map.of(
                'W', new GenericBuffer('W', BmpStringGenerator.HEX.fixedLength(10)),
                'X', new GenericBuffer('X', BmpStringGenerator.HEX.fixedLength(10)),
                'Y', new GenericBuffer('Y', BmpStringGenerator.HEX.fixedLength(10)),
                'Z', new GenericBuffer('Z', BmpStringGenerator.HEX.fixedLength(10)),
                '[', new GenericBuffer('[', BmpStringGenerator.HEX.fixedLength(10)),
                '\\', new GenericBuffer('\\', BmpStringGenerator.HEX.fixedLength(10))
        );
        final SmartCardBuffer smartCardBuffer = new SmartCardBuffer(new SmartCardData(0xFF, Collections.emptyList()));
        final CashAcceptorBuffer cashAcceptorBuffer = new CashAcceptorBuffer(Set.of(CashAcceptorNote.threeDigit(0x30, 999)));
        final DocumentBuffer documentBuffer = new DocumentBuffer(new SingleChequeDepositData(BmpStringGenerator.HEX.randomLength(256)));
        final NotesDataBuffer suspectNotes = NotesDataBuffer.suspectNotes(Set.of(new Note(0xAB, 999)));
        final NotesDataBuffer counterfeitNotes = NotesDataBuffer.counterfeitNotes(Set.of(new Note(0xAB, 999)));
        final BarCodeBuffer barCodeBuffer = new BarCodeBuffer(new BarcodeData(BmpStringGenerator.HEX.randomLength(30)));
        final CoinHoppersBuffer coinHoppersBuffer = new CoinHoppersBuffer(List.of(1, 2, 3, 4, 5));
        final BunchChequeDepositBuffer bunchChequeDepositBuffer = new BunchChequeDepositBuffer(
                new BunchChequeDepositData(1, List.of(CurrencyData.builder()
                        .withPositiveExponentSign()
                        .withDepositCurrency("RUR")
                        .withAmountExponentValue(2)
                        .withDepositedCheques(List.of(new DepositedCheque(1, 0, BmpStringGenerator.HEX.fixedLength(10))))
                        .build()))
        );
        final VoiceGuidanceBuffer voiceGuidanceBuffer = new VoiceGuidanceBuffer("ru");

        final LastTransactionStatusDataBase lastTransactionStatusData = new LastTransactionStatusDataBase(
                1, LastStatusIssued.ERROR, List.of(1, 2, 3, 4), List.of(1, 2, 3, 4),
                new CashDepositData(DepositTransactionDirection.REFUND_DIRECTION), null);
        final String mac = BmpStringGenerator.HEX.fixedLength(8);

        final String meta = TerminalMessageClass.UNSOLICITED.toNdcString()
                + TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + Luno.DEFAULT.toNdcString()
                + NdcConstants.FIELD_SEPARATOR;

        return new Object[][]{
                {new TransactionRequestMessageBuilder().withMessageCoordinationNumber('1'),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + '0' + '1' + NdcConstants.FIELD_SEPARATOR_STRING.repeat(7)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withTimeVariantNumber(timeVariantNumber),
                        meta + NdcConstants.FIELD_SEPARATOR
                                + Longs.toZeroPaddedHexString(timeVariantNumber, 8)
                                + NdcConstants.FIELD_SEPARATOR_STRING
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR_STRING.repeat(7)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withTrack2Data(track2Data),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + '0' + '1' + NdcConstants.FIELD_SEPARATOR + track2Data + NdcConstants.FIELD_SEPARATOR_STRING.repeat(6)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withTrack3Data(track3Data),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + '0' + '1' + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + track3Data
                                + NdcConstants.FIELD_SEPARATOR_STRING.repeat(5)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withOperationCodeData(operationCodeData),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + '0' + '1' + NdcConstants.FIELD_SEPARATOR_STRING.repeat(3)
                                + operationCodeData + NdcConstants.FIELD_SEPARATOR_STRING.repeat(4)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withAmountEntry(amountEntryField),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2)
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR_STRING.repeat(4)
                                + amountEntryField.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR_STRING.repeat(3)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withPinBufferA(pinBufferA),
                        meta + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + '0' + '1' + NdcConstants.FIELD_SEPARATOR_STRING.repeat(5) + pinBufferA
                                + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2)
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withBufferB(bufferB),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferB.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withBufferC(bufferC),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferC
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withTrack1Data(track1Data),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + track1Data.toNdcString()
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .withLastTransactionStatusData(lastTransactionStatusData),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + lastTransactionStatusData.toNdcString()
                },
                {new TransactionRequestMessageBuilder()
                        .withMessageCoordinationNumber('1')
                        .putExitsBuffer(bufferMap.get('X')),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + '0'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('X').toNdcString()
                },
                {new TransactionRequestMessageBuilder()
                        .withTimeVariantNumber(timeVariantNumber)
                        .withTopOfReceipt(true)
                        .withMessageCoordinationNumber('1')
                        .withTrack2Data(track2Data)
                        .withTrack3Data(track3Data)
                        .withOperationCodeData(operationCodeData)
                        .withAmountEntry(amountEntryField)
                        .withPinBufferA(pinBufferA)
                        .withBufferB(bufferB)
                        .withBufferC(bufferC)
                        .withTrack1Data(track1Data)
                        .withLastTransactionStatusData(lastTransactionStatusData)
                        .withCspData(cspData)
                        .withConfirmationCspData(confirmationCspData)
                        .withExitsBufferMap(bufferMap)
                        .withSmartCardBuffer(smartCardBuffer)
                        .withCashAcceptorBuffer(cashAcceptorBuffer)
                        .withDocumentBuffer(documentBuffer)
                        .withSuspectNotesBuffer(suspectNotes)
                        .withCounterfeitNotesBuffer(counterfeitNotes)
                        .withBarCodeBuffer(barCodeBuffer)
                        .withCoinHoppersBuffer(coinHoppersBuffer)
                        .withBunchChequeDepositBuffer(bunchChequeDepositBuffer)
                        .withVoiceGuidanceBuffer(voiceGuidanceBuffer)
                        .withMac(mac),
                        meta
                                + NdcConstants.FIELD_SEPARATOR
                                + Longs.toZeroPaddedHexString(timeVariantNumber, 8)
                                + NdcConstants.FIELD_SEPARATOR
                                + '1'
                                + '1'
                                + NdcConstants.FIELD_SEPARATOR
                                + track2Data
                                + NdcConstants.FIELD_SEPARATOR
                                + track3Data
                                + NdcConstants.FIELD_SEPARATOR
                                + operationCodeData
                                + NdcConstants.FIELD_SEPARATOR
                                + amountEntryField.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + pinBufferA
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferB.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferC
                                + NdcConstants.FIELD_SEPARATOR
                                + track1Data.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + lastTransactionStatusData.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + cspData.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + confirmationCspData.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('W').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('X').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('Y').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('Z').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('[').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bufferMap.get('\\').toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + smartCardBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + cashAcceptorBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + documentBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + suspectNotes.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + counterfeitNotes.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + barCodeBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + coinHoppersBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + bunchChequeDepositBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + voiceGuidanceBuffer.toNdcString()
                                + NdcConstants.FIELD_SEPARATOR
                                + mac
                }
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(TransactionRequestMessageBuilder builder, String expectedString) {
        Assertions.assertThat(builder.build().toNdcString())
                .isEqualTo(expectedString);
    }
}
