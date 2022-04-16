package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dpmgroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR_STRING;

public class DpmDataGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private DpmDataGroupAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new DpmDataGroupAppender(nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        final String data = GROUP_SEPARATOR_STRING + DpmDataGroup.ID + "1212345";
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(data);

        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        final DpmDataGroup expectedValue = new DpmDataGroup("12", "12345");
        Assertions.assertThat(builder.getDpmDataGroup())
                .isEqualTo(expectedValue);
    }
}
