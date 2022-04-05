package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.AbstractSupplyCountersExtendedTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.NdcConstants.GROUP_SEPARATOR;

public class EnvelopeDepositoryDataGroupAppenderTest extends AbstractSupplyCountersExtendedTest {
    private EnvelopeDepositoryDataGroupAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new EnvelopeDepositoryDataGroupAppender(nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR + "F0000099999");

        appender.appendComponent(buffer, builder, deviceConfigurationMock);

        final EnvelopeDepositoryDataGroup expectedValue
                = new EnvelopeDepositoryDataGroup(0, 99999);

        Assertions.assertThat(builder.getEnvelopeDepositoryDataGroup())
                .isEqualTo(expectedValue);

    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {"F1", "Envelopes Deposited"},
                {"F111110", "Last Envelope Serial Number"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String bufferData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(GROUP_SEPARATOR + bufferData);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, builder, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
