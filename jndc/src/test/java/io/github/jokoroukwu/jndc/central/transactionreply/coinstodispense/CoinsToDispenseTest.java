package io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CoinsToDispenseTest {

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {CoinsToDispense.of(1), "01"},
                {CoinsToDispense.of(98), "98"},
                {CoinsToDispense.of(99, 0), "9900"}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(CoinsToDispense coinsToDispense, String expectedString) {
        Assertions.assertThat(coinsToDispense.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidValueProvider() {
        return new Object[][]{
                {-1},
                {100}
        };
    }

    @Test(dataProvider = "invalidValueProvider")
    public void should_throw_expected_exception_on_invalid_value(int invalidValue) {
        Assertions.assertThatThrownBy(() -> CoinsToDispense.of(invalidValue))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of coins");
    }

}
