package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispenserdatagroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinDispenserDataGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinDispenserDataGroupAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.coindispensergroup.CoinHopper;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoinHopperDataGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private final CoinHopper dummyHopper = CoinHopper.builder()
            .withHopperTypeNumber(1)
            .withCoinsRemaining(99999)
            .withCoinsDispensed(99999)
            .withLastTransactionCoinsDispensed(99999)
            .build();
    private NdcComponentReader<List<CoinHopper>> coinHopperReaderMock;
    private CoinDispenserDataGroupAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        coinHopperReaderMock = mock(NdcComponentReader.class);
        appender = new CoinDispenserDataGroupAppender(coinHopperReaderMock, nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        when(coinHopperReaderMock.readComponent(any()))
                .thenReturn(List.of(dummyHopper));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.GROUP_SEPARATOR_STRING + CoinDispenserDataGroup.ID);
        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getCoinDispenserDataGroup())
                .isEqualTo(CoinDispenserDataGroup.of(dummyHopper));
    }
}
