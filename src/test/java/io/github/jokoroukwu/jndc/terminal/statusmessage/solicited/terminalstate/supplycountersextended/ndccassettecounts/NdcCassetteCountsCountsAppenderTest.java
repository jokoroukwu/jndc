package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts.NdcCassetteCountsGroup.BNA_GROUP_ID;
import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NdcCassetteCountsCountsAppenderTest extends AbstractSupplyCountersExtendedTest {
    private final NdcCassetteCounts dummyCounts = new NdcCassetteCounts(1, List.of());
    private NdcComponentReader<List<NdcCassetteCounts>> cassetteCountsReaderMock;
    private NdcCassetteCountsGroupAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        cassetteCountsReaderMock = mock(NdcComponentReader.class);
        appender = new NdcCassetteCountsGroupAppender(BNA_GROUP_ID, "groupName", cassetteCountsReaderMock,
                SupplyCountersExtendedBuilder::withBnaCassetteCountsGroup, nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {"00100000", List.of(dummyCounts)},
                {"0010000000100000", Collections.nCopies(2, dummyCounts)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String data, Collection<NdcCassetteCounts> cassetteCounts) {
        when(cassetteCountsReaderMock.readComponent(any()))
                .thenReturn(List.copyOf(cassetteCounts));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR_STRING + BNA_GROUP_ID + data);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getBnaCassetteCounts())
                .isEqualTo(NdcCassetteCountsGroup.bna(cassetteCounts));
    }

}
