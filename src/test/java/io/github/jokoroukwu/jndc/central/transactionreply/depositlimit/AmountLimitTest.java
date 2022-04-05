package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AmountLimitTest {

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {new AmountLimit("RUR", 0), "001RUR0.00"},
                {new AmountLimit("RUR", 1), "001RUR1.00"},
                {new AmountLimit("RUR", 10_000), "001RUR10000.00"},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(AmountLimit amountLimit, String expectedString) {
        Assertions.assertThat(amountLimit.toNdcString())
                .isEqualTo(expectedString);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_currency_code() {
        Assertions.assertThatThrownBy(() -> new AmountLimit("РУБ", 0))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not a valid ISO 4217 currency code");
    }
}
