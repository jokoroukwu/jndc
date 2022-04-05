package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BunchChequeDepositDataReaderTest {
    private final CurrencyData dummyCurrencyData = CurrencyData.builder()
            .withDepositCurrency("RUR")
            .withDepositedCheques(List.of(new DepositedCheque(1, 1)))
            .build();
    private NdcComponentReader<List<CurrencyData>> currencyDataReaderMock;
    private BunchChequeDepositDataReader reader;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        currencyDataReaderMock = mock(NdcComponentReader.class);
        when(currencyDataReaderMock.readComponent(any()))
                .thenReturn(List.of(dummyCurrencyData));

        reader = new BunchChequeDepositDataReader(currencyDataReaderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {"000", new BunchChequeDepositData(0)},
                {"999" + BunchChequeDepositData.RESERVED_FIELD, new BunchChequeDepositData(999, List.of(dummyCurrencyData))}
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String validData, BunchChequeDepositData expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        final BunchChequeDepositData actualResult = reader.readComponent(buffer);

        Assertions.assertThat(actualResult)
                .isEqualTo(expectedResult);
    }


    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String validData, BunchChequeDepositData unused) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + NdcConstants.FIELD_SEPARATOR);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .isOne();
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {"00", "Total Cheques to Return"},
                {"002" + "000", "Reserved Field"},
                {"002" + "0001", "Reserved Field"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String invalidData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidData + NdcConstants.FIELD_SEPARATOR);

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContainingAll("Bunch Cheque Deposit buffer", expectedMessagePart);
    }
}
