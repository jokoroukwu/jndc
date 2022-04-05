package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.CashHandlers;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotesDispensedDataReaderTest {
    private DeviceConfiguration deviceConfigurationMock;
    private NotesDispensedDataReader reader;

    @BeforeMethod
    public void setUp() {
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        reader = new NotesDispensedDataReader();
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {ConfigurationOptions.of(CashHandlers.BIT_TWO), "00000012340001200001", List.of(0, 1234, 12, 1)},
                {ConfigurationOptions.of(CashHandlers.DEFAULT), "00000012340001200001", List.of(0, 1234, 12, 1)},
                {ConfigurationOptions.of(CashHandlers.BIT_ZERO), "00000012340001200001999999999999999",
                        List.of(0, 1234, 12, 1, 99999, 99999, 99999)},
                {ConfigurationOptions.of(CashHandlers.COMBINED), "00000012340001200001999999999999999",
                        List.of(0, 1234, 12, 1, 99999, 99999, 99999)},
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_notes_data(ConfigurationOptions options, String bufferData, List<Integer> expectedNotesData) {
        when(deviceConfigurationMock.getConfigurationOptions())
                .thenReturn(options);

        final List<Integer> actualResult = reader.readComponent(NdcCharBuffer.wrap(bufferData), deviceConfigurationMock);
        Assertions.assertThat(actualResult)
                .as("should return expected notes data")
                .isEqualTo(expectedNotesData);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {ConfigurationOptions.of(CashHandlers.BIT_ZERO), "00000"},
                {ConfigurationOptions.of(CashHandlers.BIT_ZERO), "000000123400012000019999999999"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_propagate_expected_exception_on_invalid_data(ConfigurationOptions options, String bufferData) {
        when(deviceConfigurationMock.getConfigurationOptions())
                .thenReturn(options);

        Assertions.assertThatThrownBy(() -> reader.readComponent(NdcCharBuffer.wrap(bufferData), deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("Last Transaction Notes Dispensed cassette");
    }


    @DataProvider
    public Object[][] remainingDataProvider() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        return new Object[][]{
                {ConfigurationOptions.of(CashHandlers.DEFAULT), "00000012340001200001" + remainingData,
                        remainingData.length()},
                {ConfigurationOptions.of(CashHandlers.BIT_ZERO), "00000012340001200001999999999999999" + remainingData,
                        remainingData.length()}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(ConfigurationOptions options, String bufferData, int remainingDataLength) {
        when(deviceConfigurationMock.getConfigurationOptions())
                .thenReturn(options);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        reader.readComponent(buffer, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingDataLength);
    }
}
