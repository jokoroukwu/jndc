package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class GroupedCountersReaderTest {
    private final DeviceConfiguration deviceConfigurationMock = mock(DeviceConfiguration.class);

    @Test
    public void should_return_expected_value() {
        final String group1Value = BmpStringGenerator.DECIMAL.fixedLength(5);
        final String group2Value = BmpStringGenerator.DECIMAL.fixedLength(5);
        final String group3Value = BmpStringGenerator.DECIMAL.fixedLength(5);
        final String group4Value = BmpStringGenerator.DECIMAL.fixedLength(5);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(group1Value + group2Value + group3Value + group4Value);

        final DescriptiveOptional<GroupedCounterValues> result =
                GroupedCountersReader.INSTANCE.readComponent(buffer, deviceConfigurationMock);

        Assertions.assertThat(result.isPresent())
                .as("result should not be empty")
                .isTrue();

        final GroupedCounterValues expectedResult = new GroupedCounterValues(Integer.parseInt(group1Value),
                Integer.parseInt(group2Value), Integer.parseInt(group3Value), Integer.parseInt(group4Value));
        Assertions.assertThat(result.get())
                .isEqualTo(expectedResult);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {

        return new Object[][]{
                {BmpStringGenerator.DECIMAL.fixedLength(4), "group 1"},
                {BmpStringGenerator.DECIMAL.fixedLength(9), "group 2"},
                {BmpStringGenerator.DECIMAL.fixedLength(11), "group 3"},
                {BmpStringGenerator.DECIMAL.fixedLength(19), "group 4"},
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_return_expected_error_message_on_invalid_data(String bufferData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final DescriptiveOptional<GroupedCounterValues> result =
                GroupedCountersReader.INSTANCE.readComponent(buffer, deviceConfigurationMock);
        Assertions.assertThat(result.isEmpty())
                .as("should return empty result")
                .isTrue();

        Assertions.assertThat(result.description())
                .contains(expectedMessagePart);
    }
}
