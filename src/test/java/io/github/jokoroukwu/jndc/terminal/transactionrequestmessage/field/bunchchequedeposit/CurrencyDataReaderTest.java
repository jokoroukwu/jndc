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

public class CurrencyDataReaderTest {
    private final DepositedCheque dummyCheque = new DepositedCheque(1, 0);
    private NdcComponentReader<List<DepositedCheque>> chequeReaderMock;
    private CurrencyDataReader reader;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        chequeReaderMock = mock(NdcComponentReader.class);
        when(chequeReaderMock.readComponent(any()))
                .thenReturn(List.of(dummyCheque));

        reader = new CurrencyDataReader(chequeReaderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String rawData1 = "RUR" + '-' + "99" + "000000000000".repeat(2) + CurrencyData.RESERVED_FIELD;
        final CurrencyData currencyData1 = CurrencyData.builder()
                .withDepositCurrency("RUR")
                .withNegativeExponentSign()
                .withAmountExponentValue(99)
                .addDepositedCheque(dummyCheque)
                .build();

        final String rawData2 = "RUR" + '+' + "00" + "999999999999".repeat(2) + CurrencyData.RESERVED_FIELD;
        final CurrencyData currencyData2 = new CurrencyData("RUR", '+', 0,
                999999999999L, List.of(dummyCheque), 999999999999L);

        return new Object[][]{
                {rawData1, List.of(currencyData1)},
                {rawData2, List.of(currencyData2)},
                {rawData1 + NdcConstants.GROUP_SEPARATOR + rawData2, List.of(currencyData1, currencyData2)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String validData, List<CurrencyData> expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        final List<CurrencyData> actualResult = reader.readComponent(buffer);

        Assertions.assertThat(actualResult)
                .isEqualTo(expectedResult);

    }

    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String validData, List<CurrencyData> unused) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + NdcConstants.FIELD_SEPARATOR);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isOne();
    }

    @DataProvider
    public Object[][] invalidDataProvider() {

        return new Object[][]{
                {"1AB", "Deposit Currency"},
                {"RUR" + "=", "Amount Exponent Sign"},
                {"RUR" + "-" + "99" + "0".repeat(24) + "000", "Reserved Field"},
                {"RUR" + "-" + "99" + "0".repeat(24) + "0009", "Reserved Field"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String invalidData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidData + NdcConstants.FIELD_SEPARATOR);

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContainingAll("Bunch Cheque Deposit Buffer", expectedMessagePart);

    }

}
