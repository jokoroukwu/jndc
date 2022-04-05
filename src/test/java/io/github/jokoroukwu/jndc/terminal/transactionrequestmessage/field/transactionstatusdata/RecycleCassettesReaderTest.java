package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class RecycleCassettesReaderTest {
    private final RecycledCassettesReader reader = new RecycledCassettesReader();

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {"00", Collections.emptyList()},
                {"01007001", List.of(new RecycleCassette(7, 1, null))},
                {"02001001002999", List.of(new RecycleCassette(1, 1, null),
                        new RecycleCassette(2, 999, null))}
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_recycle_cassettes_data(String validData, List<RecycleCassette> expectedResult) {
        final List<RecycleCassette> actualResult = reader.readComponent(NdcCharBuffer.wrap(validData));
        Assertions.assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_number_of_cassettes() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("1");

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Last Transaction Status Data")
                .hasMessageContaining("Number of recycle cassettes reported");
    }

    @DataProvider
    public Object[][] invalidCassetteTypeProvider() {
        return new Object[][]{
                {"000"},
                {"008"},
                {"009"}
        };
    }

    @Test(dataProvider = "invalidCassetteTypeProvider")
    public void should_throw_expected_exception_on_invalid_cassette_type(String invalidCassetteType) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01" + invalidCassetteType);

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Last Transaction Status Data")
                .hasMessageContaining("NDC Cassette Type")
                .hasMessageContaining("should be within valid range (1-7 dec)");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_number_of_notes() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01" + "006" + "000");

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Last Transaction Status Data")
                .hasMessageContaining("Number of Notes")
                .hasMessageContaining("should be within valid range (1-999 dec)");
    }

    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String validData, List<RecycleCassette> expectedResult) {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + remainingData);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("should have expected number of characters remaining")
                .isEqualTo(remainingData.length());
    }

}
