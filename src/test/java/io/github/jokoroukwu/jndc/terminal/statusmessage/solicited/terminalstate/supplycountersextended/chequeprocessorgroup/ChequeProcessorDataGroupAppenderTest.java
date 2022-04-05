package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR_STRING;

public class ChequeProcessorDataGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private final Bin dummyBin = new Bin(1, 99999);
    private ChequeProcessorDataGroupAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new ChequeProcessorDataGroupAppender(nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        final String bufferData = GROUP_SEPARATOR_STRING + ChequeProcessorDataGroup.ID + dummyBin.toNdcString().repeat(2);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        Assertions.assertThat(builder.getChequeProcessorDataGroup())
                .isEqualTo(new ChequeProcessorDataGroup(Collections.nCopies(2, dummyBin)));
    }
}
