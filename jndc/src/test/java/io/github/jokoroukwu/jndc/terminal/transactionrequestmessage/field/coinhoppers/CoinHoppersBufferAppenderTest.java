package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class CoinHoppersBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private CoinHopperBufferAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new CoinHopperBufferAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, CoinHoppersBuffer.EMPTY},
                {"00" + "01" + "99" + "09" + "00", new CoinHoppersBuffer(List.of(0, 1, 99, 9, 0))},
                {"00" + "01" + "99" + "09" + "00" + "00", new CoinHoppersBuffer(List.of(0, 1, 99, 9, 0, 0))},
                {"00" + "01" + "99" + "09" + "00" + "00" + "00", new CoinHoppersBuffer(List.of(0, 1, 99, 9, 0, 0, 0))},
                {"00" + "01" + "99" + "09" + "00" + "00" + "00" + "00", new CoinHoppersBuffer(List.of(0, 1, 99, 9, 0, 0, 0, 0))}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData, CoinHoppersBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + CoinHoppersBuffer.ID + validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getCoinHoppersBuffer())
                .isEqualTo(expectedValue);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_optionally_call_next_appender(String validData, CoinHoppersBuffer unused) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + CoinHoppersBuffer.ID + validData);

        final FakeCoinHopperBufferAppender appenderMock = mock(FakeCoinHopperBufferAppender.class, CALLS_REAL_METHODS);
        appenderMock.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(appenderMock, times(1))
                .callNextAppenderIfDataRemains(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_skip_buffer_meta() {
        final FakeCoinHopperBufferAppender appenderMock = mock(FakeCoinHopperBufferAppender.class, CALLS_REAL_METHODS);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("12");
        appenderMock.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(appenderMock, times(1))
                .skipBufferMeta(buffer);
    }

    @DataProvider
    public Object[][] invalidHopperData() {
        return new Object[][]{
                {"0", "hopper type 1"},
                {"00".repeat(4), "at least 5 hopper types must be present"},
                {"00".repeat(9), "exceeds max number of hopper types"}
        };
    }

    @Test(dataProvider = "invalidHopperData")
    public void should_throw_expected_exception_on_invalid_hopper_data(String invalidHopperData, String messagePart) {
        final FakeCoinHopperBufferAppender appenderMock = mock(FakeCoinHopperBufferAppender.class, CALLS_REAL_METHODS);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + CoinHoppersBuffer.ID + invalidHopperData);
        Assertions.assertThatThrownBy(() -> appenderMock.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(CoinHopperBufferAppender.FIELD_NAME)
                .hasMessageContaining(messagePart);

    }

    private static class FakeCoinHopperBufferAppender extends CoinHopperBufferAppender {

        protected FakeCoinHopperBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
            super(nextAppender);
        }

        @Override
        protected void skipBufferMeta(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(2);
        }

        @Override
        protected void callNextAppenderIfDataRemains(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder collector, DeviceConfiguration deviceConfiguration) {

        }
    }
}
