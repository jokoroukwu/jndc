package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispenserdatagroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinDispenserDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopper;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

public class CoinDispenserDataGroupTest {
    private final CoinHopper dummyCoinHopper = CoinHopper.builder()
            .withHopperTypeNumber(1)
            .withCoinsRemaining(99999)
            .withCoinsDispensed(99999)
            .withLastTransactionCoinsDispensed(99999)
            .withCoinsDeposited(99999)
            .build();

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {List.of(dummyCoinHopper), CoinDispenserDataGroup.ID + dummyCoinHopper.toNdcString()},
                {List.of(dummyCoinHopper, dummyCoinHopper), CoinDispenserDataGroup.ID + dummyCoinHopper.toNdcString().repeat(2)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(Collection<CoinHopper> coinHoppers, String expectedNdcString) {
        final CoinDispenserDataGroup dataGroup = new CoinDispenserDataGroup(coinHoppers);

        Assertions.assertThat(dataGroup.toNdcString())
                .isEqualTo(expectedNdcString);
    }
}
