package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.util.Currencies;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class CurrencyDataTest {
    private final String validDepositCurrency = Currencies.ISO_4217_CODES.iterator()
            .next();
    private final DepositedCheque dummyChequeInfo = new DepositedCheque(1, 0, Strings.EMPTY_STRING);


    @Test
    public void should_throw_expected_exception_on_invalid_deposit_currency() {
        Assertions.assertThatThrownBy(() -> new CurrencyData("123",
                '-',
                99,
                0,
                List.of(dummyChequeInfo))
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("is not a valid ISO 4217 currency code");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_exponent_sign() {
        Assertions.assertThatThrownBy(() -> new CurrencyData(validDepositCurrency,
                '1',
                99,
                0,
                List.of(dummyChequeInfo))
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount Exponent Sign");
    }

    @DataProvider
    public Object[][] invalidExponentValueProvider() {
        return new Object[][]{
                {-1},
                {100},
                {101}
        };
    }

    @Test(dataProvider = "invalidExponentValueProvider")
    public void should_throw_expected_exception_on_invalid_exponent_value(int invalidExponentValue) {
        Assertions.assertThatThrownBy(() -> new CurrencyData(validDepositCurrency,
                '-',
                invalidExponentValue,
                0,
                List.of(dummyChequeInfo))
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount Exponent Value");
    }

    @DataProvider
    public Object[][] validExponentValueProvider() {
        return new Object[][]{
                {0},
                {98},
                {99}
        };
    }

    @Test(dataProvider = "validExponentValueProvider")
    public void should_not_throw_exception_on_valid_exponent_value(int validExponentValue) {
        Assertions.assertThatCode(() -> new CurrencyData(validDepositCurrency,
                '-',
                validExponentValue,
                0,
                List.of(dummyChequeInfo))
        ).doesNotThrowAnyException();

    }

    @DataProvider
    public Object[][] invalidCustomerAmountProvider() {
        return new Object[][]{
                {-1L},
                {1_000_000_000_000L}
        };
    }

    @Test(dataProvider = "invalidCustomerAmountProvider")
    public void should_throw_expected_exception_on_invalid_customer_amount(long invalidCustomerAmount) {
        Assertions.assertThatThrownBy(() -> new CurrencyData(validDepositCurrency,
                '-',
                1,
                invalidCustomerAmount,
                List.of(dummyChequeInfo))
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Customer Amount");
    }

    @DataProvider
    public Object[][] validCustomerAmountProvider() {
        return new Object[][]{
                {0L},
                {1L},
                {999_999_999_999L}
        };
    }

    @Test(dataProvider = "validCustomerAmountProvider")
    public void should_not_throw_exception_on_valid_customer_amount(long validCustomerAmount) {
        Assertions.assertThatCode(() -> new CurrencyData(validDepositCurrency,
                '-',
                1,
                validCustomerAmount,
                List.of(dummyChequeInfo))
        ).doesNotThrowAnyException();

    }

    @DataProvider
    public Object[][] invalidChequesProvider() {
        return new Object[][]{
                {Collections.emptyList()},
                {null}
        };
    }

    @Test(dataProvider = "invalidChequesProvider")
    public void should_throw_expected_exception_on_invalid_cheques(List<DepositedCheque> invalidCheques) {
        Assertions.assertThatThrownBy(() -> new CurrencyData(validDepositCurrency,
                '-',
                1,
                1,
                invalidCheques)
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deposited Cheque Data");
    }

    @DataProvider
    public Object[][] expectedStringProvider() {
        return new Object[][]{
                {new CurrencyData(validDepositCurrency, '+', 1, 0,
                        List.of(dummyChequeInfo)),
                        validDepositCurrency + '+' + "01" + "000000000000" + "000000000000" + CurrencyData.RESERVED_FIELD
                                + dummyChequeInfo.toNdcString() + NdcConstants.GROUP_SEPARATOR},
                {new CurrencyData(validDepositCurrency, '-', 99, 99999999999L,
                        List.of(dummyChequeInfo)),
                        validDepositCurrency + '-' + "99" + "099999999999" + "000000000000" + CurrencyData.RESERVED_FIELD
                                + dummyChequeInfo.toNdcString() + NdcConstants.GROUP_SEPARATOR},
                {new CurrencyData(validDepositCurrency, '+', 99, 99999999999L,
                        List.of(dummyChequeInfo), 999),
                        validDepositCurrency + '+' + "99" + "099999999999" + "000000000999" + CurrencyData.RESERVED_FIELD
                                + dummyChequeInfo.toNdcString() + NdcConstants.GROUP_SEPARATOR},
                {new CurrencyData(validDepositCurrency, '+', 0, 0,
                        List.of(dummyChequeInfo, dummyChequeInfo)),
                        validDepositCurrency + '+' + "00" + "000000000000" + "000000000000" + CurrencyData.RESERVED_FIELD
                                + dummyChequeInfo.toNdcString() + NdcConstants.GROUP_SEPARATOR + dummyChequeInfo.toNdcString() + NdcConstants.GROUP_SEPARATOR},
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(CurrencyData currencyData, String expectedString) {
        Assertions.assertThat(currencyData.toNdcString())
                .isEqualTo(expectedString);
    }

}
