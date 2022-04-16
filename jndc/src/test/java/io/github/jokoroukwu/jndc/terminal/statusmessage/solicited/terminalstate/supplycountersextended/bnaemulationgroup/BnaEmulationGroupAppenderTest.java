package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositNotes;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BnaEmulationGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private final CashDepositNotes dummyCashDepositNotes = new CashDepositNotes(1, 1, 1, 1);
    private NdcComponentReader<CashDepositNotes> readerMock;
    private BnaEmulationGroupAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        readerMock = mock(NdcComponentReader.class);
        appender = new BnaEmulationGroupAppender(readerMock, nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        when(readerMock.readComponent(any()))
                .thenReturn(dummyCashDepositNotes);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR_STRING + BnaEmulationDepositDataGroup.ID);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getBnaEmulationDepositDataGroup())
                .isEqualTo(new BnaEmulationDepositDataGroup(dummyCashDepositNotes));

    }

}
