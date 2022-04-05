package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicAppenderTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicContext;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Optional;

import static io.github.jokoroukwu.jndc.util.NdcConstants.FIELD_SEPARATOR;
import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR;
import static org.mockito.Mockito.*;

public class ReservedCounterFieldAppenderTest extends SupplyCountersBasicAppenderTest {
    private final DeviceConfiguration deviceConfigurationMock = mock(DeviceConfiguration.class);
    private FieldMetaSkipStrategy fieldMetaSkipStrategyMock;
    private FieldPresenceIndicator fieldPresenceIndicatorMock;
    private ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppenderMock;

    @DataProvider
    public static Object[][] validDataProvider() {
        return new Object[][]{
                //  max length checks
                {1, 1, "12", Map.of(20, "1")},
                {1, 2, "12", Map.of(20, "12")},
                //  min length checks
                {2, 2, "12" + GROUP_SEPARATOR, Map.of(20, "12")},
                {2, 2, "12" + FIELD_SEPARATOR, Map.of(20, "12")},
                {2, 2, "12", Map.of(20, "12")}
        };
    }

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        fieldPresenceIndicatorMock = mock(FieldPresenceIndicator.class);
        fieldMetaSkipStrategyMock = mock(FieldMetaSkipStrategy.class);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_data(int minLength, int maxLength, String bufferData, Map<Integer, String> expectedFieldMap) {
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final SupplyCountersBasicContext context = new SupplyCountersBasicContext();
        final ReservedCounterFieldAppender appender = new ReservedCounterFieldAppender(fieldMetaSkipStrategyMock,
                fieldPresenceIndicatorMock, 20, minLength, maxLength, nextAppenderMock);

        appender.appendComponent(buffer, context, deviceConfigurationMock);

        Assertions.assertThat(context.getReservedCounterFieldMap())
                .isEqualTo(expectedFieldMap);

        verify(fieldPresenceIndicatorMock, times(1))
                .isFieldPresent(buffer, deviceConfigurationMock);
        verify(fieldMetaSkipStrategyMock, times(1))
                .skipFieldMeta(buffer);
        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_insufficient_data() {
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("1");
        final SupplyCountersBasicContext context = new SupplyCountersBasicContext();
        final int minLength = 2;
        final ReservedCounterFieldAppender appender = new ReservedCounterFieldAppender(fieldMetaSkipStrategyMock,
                fieldPresenceIndicatorMock, 20, minLength, minLength, nextAppenderMock);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, context, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("should be at least %d characters long", minLength);
    }

    @Test
    public void should_not_read_data_when_field_not_present() {
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.FALSE);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("1");
        final SupplyCountersBasicContext context = new SupplyCountersBasicContext();
        final int minLength = 2;
        final ReservedCounterFieldAppender appender = new ReservedCounterFieldAppender(fieldMetaSkipStrategyMock,
                fieldPresenceIndicatorMock, 20, minLength, minLength, nextAppenderMock);
        appender.appendComponent(buffer, context, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("should not consume any data")
                .isOne();
        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {"1" + FIELD_SEPARATOR, 1},
                {"1" + GROUP_SEPARATOR, 1},
                {"1", 0}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, int expectedRemainingDataLength) {
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final SupplyCountersBasicContext context = new SupplyCountersBasicContext();
        final ReservedCounterFieldAppender appender = new ReservedCounterFieldAppender(fieldMetaSkipStrategyMock,
                fieldPresenceIndicatorMock, 20, 1, 10, nextAppenderMock);

        appender.appendComponent(buffer, context, deviceConfigurationMock);
        Assertions.assertThat(buffer.remaining())
                .isEqualTo(expectedRemainingDataLength);
    }
}
