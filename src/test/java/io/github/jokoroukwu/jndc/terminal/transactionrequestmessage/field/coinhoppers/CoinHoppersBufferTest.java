package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CoinHoppersBufferTest {

    @DataProvider
    public Object[][] ndcStringProvider() {
        return new Object[][]{
                {Collections.emptyList(), String.valueOf(CoinHoppersBuffer.ID)},
                {List.of(0, 0, 0, 0, 0), CoinHoppersBuffer.ID + "0000000000"}
        };
    }

    @Test(dataProvider = "ndcStringProvider")
    public void should_return_expected_ndc_string(List<Integer> data, String expectedString) {
        Assertions.assertThat(new CoinHoppersBuffer(data).toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {List.of(0), "Number of hopper types"},
                {List.of(0, 0, 0, 0), "Number of hopper types"},
                {List.of(0, 0, 0, 0, 0, 0, 0, 0, 0), "Number of hopper types"},
                {null, "must not be null"},
                {Arrays.asList(0, null, 0, 0, 0), "Coins Dispensed"},
                {List.of(0, 0, -1, 0, 0), "Coins Dispensed"},
                {List.of(0, 0, 0, 100, 0), "Coins Dispensed"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_coin_data(Collection<Integer> coinHopperData, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new CoinHoppersBuffer(coinHopperData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);

    }
}
