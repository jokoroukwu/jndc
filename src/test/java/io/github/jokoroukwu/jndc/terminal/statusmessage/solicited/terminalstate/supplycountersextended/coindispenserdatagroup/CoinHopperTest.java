package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispenserdatagroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopper;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopperBuilder;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CoinHopperTest {

    @DataProvider
    public Object[][] validDataProvider() {
        final CoinHopper coinHopper = CoinHopper.builder()
                .withHopperTypeNumber(1)
                .withCoinsRemaining(99999)
                .withCoinsDispensed(99999)
                .withLastTransactionCoinsDispensed(99999)
                .build();
        final CoinHopper withDepositedCoins = coinHopper.copy()
                .withCoinsDeposited(99999)
                .build();

        return new Object[][]{
                {coinHopper, "01999999999999999"},
                {withDepositedCoins, "0199999999999999999999"}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(CoinHopper coinHopper, String expectedNdcString) {
        Assertions.assertThat(coinHopper.toNdcString())
                .isEqualTo(expectedNdcString);
    }

    @DataProvider
    public Object[][] builderProvider() {
        final CoinHopper coinHopper = CoinHopper.builder()
                .withHopperTypeNumber(1)
                .withCoinsRemaining(99999)
                .withCoinsDispensed(99999)
                .withLastTransactionCoinsDispensed(99999)
                .withCoinsDeposited(99999)
                .build();

        return new Object[][]{
                {coinHopper.copy().withHopperTypeNumber(0), "Hopper Type Number"},
                {coinHopper.copy().withHopperTypeNumber(9), "Hopper Type Number"},

                {coinHopper.copy().withCoinsRemaining(-1), "Coins Remaining"},
                {coinHopper.copy().withCoinsRemaining(100_000), "Coins Remaining"},

                {coinHopper.copy().withCoinsDispensed(-1), "Coins Dispensed"},
                {coinHopper.copy().withCoinsDispensed(100_000), "Coins Dispensed"},

                {coinHopper.copy().withLastTransactionCoinsDispensed(-1), "Last Transaction Coins Dispensed"},
                {coinHopper.copy().withLastTransactionCoinsDispensed(100_000), "Last Transaction Coins Dispensed"},

                {coinHopper.copy().withCoinsDeposited(100_000), "Coins Deposited"},
        };
    }

    @Test(dataProvider = "builderProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(CoinHopperBuilder coinHopperBuilder,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(coinHopperBuilder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
