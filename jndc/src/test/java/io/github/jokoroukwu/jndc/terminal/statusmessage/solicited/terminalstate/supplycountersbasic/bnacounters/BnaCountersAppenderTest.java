package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicAppenderTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.NdcConstants.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BnaCountersAppenderTest extends SupplyCountersBasicAppenderTest {
    private BnaCountersAppender appender;

    @BeforeMethod
    public void beforeClass() {
        appender = new BnaCountersAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {GROUP_SEPARATOR + "00000999990111109999",
                        new BnaCounters(0, 99999, 1111, 9999)},
                {GROUP_SEPARATOR_STRING.repeat(2), null},
                {GROUP_SEPARATOR_STRING + FIELD_SEPARATOR, null},
                {FIELD_SEPARATOR_STRING, null},
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, BnaCounters expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        appender.appendComponent(buffer, context, deviceConfigurationMock);
        Assertions.assertThat(context.getCountersBasicBuilder().getBnaCounters())
                .isEqualTo(expectedValue);

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @Test
    public void should_append_expected_value() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR + "00000999990111109999");
        appender.appendComponent(buffer, context, deviceConfigurationMock);
        Assertions.assertThat(context.getCountersBasicBuilder().getBnaCounters())
                .isEqualTo(new BnaCounters(0, 99999, 1111, 9999));

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {GROUP_SEPARATOR_STRING + "1"},
                {GROUP_SEPARATOR_STRING + "11111"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String bufferData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, context, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {GROUP_SEPARATOR + "000009999901111099991", 1},
                {GROUP_SEPARATOR_STRING.repeat(2), 1},
                {GROUP_SEPARATOR_STRING + FIELD_SEPARATOR, 1},
                {FIELD_SEPARATOR_STRING, 1},
        };
    }


    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, int expectedRemainingDataLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        appender.appendComponent(buffer, context, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isEqualTo(expectedRemainingDataLength);
    }
}
