package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class CoinsDispensedDataReaderTest {
    private final CoinsDispensedDataReader reader = new CoinsDispensedDataReader();

    @Test
    public void should_return_expected_coins_dispensed() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00000" + "00001" + "90000" + "09000" + "00000");
        final List<Integer> expectedCoinsDispensed = List.of(1, 90000, 9000, 0);
        final List<Integer> actualCoinsDispensed = reader.readComponent(buffer);

        Assertions.assertThat(actualCoinsDispensed)
                .isEqualTo(expectedCoinsDispensed);
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00000" + "00001" + "90000" + "09000" + "00000" + remainingData);
        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length());
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {"00001"},
                {"0000"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_coinage_amount_dispensed(String invalidCoinageDispensed) {
        Assertions.assertThatThrownBy(() -> reader.readComponent(NdcCharBuffer.wrap(invalidCoinageDispensed)))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Last Transaction Coinage Amount Dispensed");
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_coins_dispensed(String invalidCoinageDispensed) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00000" + invalidCoinageDispensed);
        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Last Transaction Coins Dispensed");
    }
}
