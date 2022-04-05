package io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.CurrencyTableEntry;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.TransactionTableEntry;
import io.github.jokoroukwu.jndc.tlv.*;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;

public class TransactionUpdateBufferTest {
    private CurrencyTableEntry dummyCurrencyEntry;
    private TransactionTableEntry dummyTransactionEntry;
    private String dummyBufferData;
    private String dummyBufferLength;

    @BeforeClass
    public void beforeClass() {
        dummyBufferData = BmpStringGenerator.HEX.fixedLength(32);
        dummyBufferLength = Integers.toZeroPaddedHexString(dummyBufferData.length(), 2);

        final ResponseFormat2 transactionResponseFormat
                = ResponseFormat2.of(new TransactionCategoryCode("AB"), new TransactionType("BC"));
        dummyTransactionEntry = new TransactionTableEntry(1, transactionResponseFormat);

        final ResponseFormat2 currencyResponseFormat = ResponseFormat2.of(new TransactionCurrencyCode("ABCD"),
                new TransactionCurrencyExponent("AB"));
        dummyCurrencyEntry = new CurrencyTableEntry(1, currencyResponseFormat);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String currencyString = dummyCurrencyEntry.toNdcString();
        final String transactionString = dummyTransactionEntry.toNdcString();
        return new Object[][]{
                {new TransactionUpdateBuffer(TargetBufferId.NO_BUFFER, EMPTY_STRING, dummyCurrencyEntry, dummyTransactionEntry),
                        TransactionUpdateBuffer.ID + currencyString + transactionString + TargetBufferId.NO_BUFFER.getValue()},

                {new TransactionUpdateBuffer(TargetBufferId.BUFFER_C, dummyBufferData, dummyCurrencyEntry, dummyTransactionEntry),
                        TransactionUpdateBuffer.ID + currencyString + transactionString + TargetBufferId.BUFFER_C.getValue() + dummyBufferLength + dummyBufferData},

                {new TransactionUpdateBuffer(TargetBufferId.BUFFER_C, dummyBufferData, null, dummyTransactionEntry),
                        TransactionUpdateBuffer.ID + "00" + transactionString + TargetBufferId.BUFFER_C.getValue() + dummyBufferLength + dummyBufferData},

                {new TransactionUpdateBuffer(TargetBufferId.BUFFER_B, dummyBufferData, dummyCurrencyEntry, null),
                        TransactionUpdateBuffer.ID + currencyString + "00" + TargetBufferId.BUFFER_B.getValue() + dummyBufferLength + dummyBufferData},

                {new TransactionUpdateBuffer(TargetBufferId.BUFFER_B, dummyBufferData, null, null),
                        TransactionUpdateBuffer.ID + "00" + "00" + TargetBufferId.BUFFER_B.getValue() + dummyBufferLength + dummyBufferData}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(TransactionUpdateBuffer transactionUpdateBuffer, String expectedString) {
        Assertions.assertThat(transactionUpdateBuffer.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidBufferLengthProvider() {
        return new Object[][]{
                {TargetBufferId.NO_BUFFER, null},
                {TargetBufferId.NO_BUFFER, "1"},
                {TargetBufferId.NO_BUFFER, "12"},

                {TargetBufferId.BUFFER_B, null},
                {TargetBufferId.BUFFER_B, EMPTY_STRING},
                {TargetBufferId.BUFFER_B, "1"},
                {TargetBufferId.BUFFER_B, BmpStringGenerator.HEX.fixedLength(31)},

                {TargetBufferId.BUFFER_C, null},
                {TargetBufferId.BUFFER_C, EMPTY_STRING},
                {TargetBufferId.BUFFER_C, "1"},
                {TargetBufferId.BUFFER_C, BmpStringGenerator.HEX.fixedLength(31)},

                {TargetBufferId.AMOUNT_BUFFER, null},
                {TargetBufferId.AMOUNT_BUFFER, EMPTY_STRING},
                {TargetBufferId.AMOUNT_BUFFER, "1"},
                {TargetBufferId.AMOUNT_BUFFER, BmpStringGenerator.HEX.fixedLength(7)},
                {TargetBufferId.AMOUNT_BUFFER, BmpStringGenerator.HEX.fixedLength(9)},
                {TargetBufferId.AMOUNT_BUFFER, BmpStringGenerator.HEX.fixedLength(11)},
                {TargetBufferId.AMOUNT_BUFFER, BmpStringGenerator.HEX.fixedLength(13)}
        };
    }

    @Test(dataProvider = "invalidBufferLengthProvider")
    public void should_throw_expected_exception_on_invalid_buffer_data_length(TargetBufferId targetBufferId, String bufferData) {
        Assertions.assertThatThrownBy(()
                -> new TransactionUpdateBuffer(targetBufferId, bufferData, dummyCurrencyEntry, dummyTransactionEntry))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
