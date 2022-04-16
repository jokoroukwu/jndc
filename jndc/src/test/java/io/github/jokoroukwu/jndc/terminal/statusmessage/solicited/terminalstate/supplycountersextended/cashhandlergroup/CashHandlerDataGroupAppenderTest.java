package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CashHandlerDataGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private final CassetteCounters dummyCassette = CassetteCounters.builder()
            .withCassetteType(0)
            .withNotesInCassette(10)
            .withNotesDispensed(3)
            .withNotesRejected(2)
            .withLastTransactionNotesDispensed(99999)
            .withNotesDeposited(1)
            .buildCashHandlerCassetteCounters();
    private NdcComponentReader<List<CassetteCounters>> cassetteReaderMock;
    private CashHandlerDataGroupAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        cassetteReaderMock = mock(NdcComponentReader.class);
        appender = CashHandlerDataGroupAppender.builder()
                .withNextAppender(nextAppenderMock)
                .withDataAcceptor(SupplyCountersExtendedBuilder::withCashHandler0DataGroup)
                .withHandler0Id()
                .withHandler0GroupName()
                .withCassettesReader(cassetteReaderMock)
                .build();
    }

    @Test
    public void should_append_expected_value() {
        when(cassetteReaderMock.readComponent(any()))
                .thenReturn(Collections.nCopies(4, dummyCassette));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR_STRING + CashHandlerDataGroup.HANDLER_0_ID);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getCashHandler0DataGroup())
                .isEqualTo(CashHandlerDataGroup.handler0(Collections.nCopies(4, dummyCassette)));
    }
}
