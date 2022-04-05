package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.coincounters;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.SupplyCountersBasicAppenderTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters.GroupedCounterValues;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.mockito.internal.stubbing.answers.ReturnsElementsOf;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CoinCountersAppenderTest extends SupplyCountersBasicAppenderTest {
    private final GroupedCounterValues dummyCounterValues
            = new GroupedCounterValues(1, 2, 3, 4);
    private CoinCountersAppender appender;
    private ConfigurableNdcComponentReader<DescriptiveOptional<GroupedCounterValues>> groupedCounterValuesReaderMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        groupedCounterValuesReaderMock = mock(ConfigurableNdcComponentReader.class);
        appender = new CoinCountersAppender(groupedCounterValuesReaderMock, nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, null},
                {"1", new CoinCounters(dummyCounterValues, dummyCounterValues, dummyCounterValues)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, CoinCounters expectedCoinCounters) {
        when(groupedCounterValuesReaderMock.readComponent(any(), any()))
                .thenReturn(DescriptiveOptional.of(dummyCounterValues));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR + bufferData);

        appender.appendComponent(buffer, context, deviceConfigurationMock);
        Assertions.assertThat(context.getCountersBasicBuilder().getCoinCounters())
                .isEqualTo(expectedCoinCounters);

        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, context, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_no_preceding_group_separator() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("1");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, context, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll("group separator", "Coin counters");
    }

    @DataProvider
    public Object[][] readerAnswerProvider() {
        final String errorMessage = "error";
        final DescriptiveOptional<?> emptyOptional = DescriptiveOptional.empty(() -> errorMessage);
        final DescriptiveOptional<GroupedCounterValues> nonEmptyOptional = DescriptiveOptional.of(dummyCounterValues);

        return new Object[][]{
                {List.of(emptyOptional), new String[]{"Coins remaining", errorMessage}},

                {List.of(nonEmptyOptional, emptyOptional), new String[]{"Coins dispensed", errorMessage}},

                {List.of(nonEmptyOptional, nonEmptyOptional, emptyOptional),
                        new String[]{"Last transaction coins dispensed", errorMessage}}
        };
    }

    @Test(dataProvider = "readerAnswerProvider")
    public void should_throw_expected_exception_on_empty_reader_result(Collection<DescriptiveOptional<GroupedCounterValues>> readerAnswers,
                                                                       String[] expectedErrorMessageParts) {
        when(groupedCounterValuesReaderMock.readComponent(any(), any()))
                .thenAnswer(new ReturnsElementsOf(readerAnswers));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR + "1");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, context, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(expectedErrorMessageParts);
    }
}
