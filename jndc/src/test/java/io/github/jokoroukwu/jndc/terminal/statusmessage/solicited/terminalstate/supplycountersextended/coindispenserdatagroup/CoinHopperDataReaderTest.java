package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispenserdatagroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopper;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopperDataReader;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CoinHopperDataReaderTest {
    private final CoinHopper dummyCoinHopper = CoinHopper.builder()
            .withHopperTypeNumber(1)
            .withCoinsRemaining(99999)
            .withCoinsDispensed(99999)
            .withLastTransactionCoinsDispensed(99999)
            .withCoinsDeposited(99999)
            .build();

    @DataProvider
    public Object[][] validDataProvider() {
        final CoinHopper withNoDepositedCoins = dummyCoinHopper.copy()
                .withCoinsDeposited(-1)
                .build();
        return new Object[][]{
                {dummyCoinHopper.toNdcString(), List.of(dummyCoinHopper)},
                {dummyCoinHopper.toNdcString().repeat(CoinHopperDataReader.MAX_NUMBER_OF_HOPPERS),
                        Collections.nCopies(CoinHopperDataReader.MAX_NUMBER_OF_HOPPERS, dummyCoinHopper)},

                {withNoDepositedCoins.toNdcString(), List.of(withNoDepositedCoins)},
                {withNoDepositedCoins.toNdcString().repeat(CoinHopperDataReader.MAX_NUMBER_OF_HOPPERS),
                        Collections.nCopies(CoinHopperDataReader.MAX_NUMBER_OF_HOPPERS, withNoDepositedCoins)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_result(String bufferData, List<CoinHopper> expectedHoppers) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final List<CoinHopper> actualHoppers = CoinHopperDataReader.INSTANCE.readComponent(buffer);

        Assertions.assertThat(actualHoppers)
                .isEqualTo(expectedHoppers);

        Assertions.assertThat(buffer.remaining())
                .as("no characters should remain in the buffer")
                .isZero();
    }

    @Test
    public void should_throw_expected_exception_on_invalid_number_of_hoppers() {
        final String invalidNumberOfHoppersString = Collections.nCopies(CoinHopperDataReader.MAX_NUMBER_OF_HOPPERS + 1,
                dummyCoinHopper)
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining());
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidNumberOfHoppersString);

        Assertions.assertThatThrownBy(() -> CoinHopperDataReader.INSTANCE.readComponent(buffer))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("length exceeds max field data length");
    }
}
