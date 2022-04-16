package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicAppenderTest;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroupedCounterValuesAppenderTest extends SupplyCountersBasicAppenderTest {
    private final String fieldName = "field-name";
    private ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCountersReaderMock;
    private GroupedCounterValuesAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        groupedCountersReaderMock = mock(ConfigurableNdcComponentReader.class);
        appender = GroupedCounterValuesAppender.builder()
                .withNextAppender(nextAppenderMock)
                .withFieldName(fieldName)
                .withDataConsumer((data, context) -> context.getCountersBasicBuilder().withNotesDispensed(data))
                .withFieldMetaSkipStrategy(fieldMetaSkipStrategyMock)
                .withFieldPresenceIndicator(fieldPresenceIndicatorMock)
                .withGroupedCountersReader(groupedCountersReaderMock)
                .build();
    }


    @Test
    public void should_append_expected_value() {
        final GroupedCounterValues dummyGroupedCounters
                = new GroupedCounterValues(11111, 0, 99999, 9999);
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());
        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
        when(groupedCountersReaderMock.readComponent(any(), any()))
                .thenReturn(DescriptiveOptional.of(dummyGroupedCounters));


        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        appender.appendComponent(buffer, context, deviceConfigurationMock);

        Assertions.assertThat(context.getCountersBasicBuilder().getNotesDispensed())
                .isEqualTo(dummyGroupedCounters);

        verify(fieldMetaSkipStrategyMock, times(1))
                .skipFieldMeta(buffer);

        verify(fieldPresenceIndicatorMock, times(1))
                .isFieldPresent(buffer, deviceConfigurationMock);

        verify(groupedCountersReaderMock, times(1))
                .readComponent(buffer, deviceConfigurationMock);

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_field_meta_parse_error() {
        final String errorMessage = "error";
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.of(errorMessage));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.EMPTY, context, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(errorMessage);
    }


    @Test
    public void should_throw_expected_exception_on_empty_reader_result() {
        when(fieldMetaSkipStrategyMock.skipFieldMeta(any()))
                .thenReturn(Optional.empty());

        when(fieldPresenceIndicatorMock.isFieldPresent(any(), any()))
                .thenReturn(Boolean.TRUE);
        final String errorMessage = "error";
        when(groupedCountersReaderMock.readComponent(any(), any()))
                .thenReturn(DescriptiveOptional.empty(() -> errorMessage));

        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, context, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(fieldName, errorMessage);
    }
}
