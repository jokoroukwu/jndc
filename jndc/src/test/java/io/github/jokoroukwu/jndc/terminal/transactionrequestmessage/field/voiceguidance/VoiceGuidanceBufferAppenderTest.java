package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class VoiceGuidanceBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private VoiceGuidanceBufferAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new VoiceGuidanceBufferAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, VoiceGuidanceBuffer.EMPTY},
                {"ru", new VoiceGuidanceBuffer("ru")}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_voice_guidance_buffer(String validData, VoiceGuidanceBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + VoiceGuidanceBuffer.ID + validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getVoiceGuidanceBuffer())
                .isEqualTo(expectedValue);
    }

    @DataProvider
    public Object[][] nextAppenderCallTestProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, 0},
                {NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {"ru", 0},
                {"ru" + NdcConstants.FIELD_SEPARATOR, 1}
        };
    }

    @Test(dataProvider = "nextAppenderCallTestProvider")
    public void should_optionally_call_next_appender(String validData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + VoiceGuidanceBuffer.ID + validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }


    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String validData, VoiceGuidanceBuffer unused) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + VoiceGuidanceBuffer.ID + validData + NdcConstants.FIELD_SEPARATOR);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isOne();
    }

    @Test
    public void should_skip_buffer_meta() {
        final FakeVoiceGuidanceBufferAppender fakeAppenderMock = mock(FakeVoiceGuidanceBufferAppender.class, CALLS_REAL_METHODS);

        fakeAppenderMock.appendComponent(NdcCharBuffer.EMPTY, messageBuilder, deviceConfigurationMock);

        verify(fakeAppenderMock, times(1))
                .skipBufferMeta(NdcCharBuffer.EMPTY);
    }


    @Test
    public void should_throw_expected_exception_on_invalid_data() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + VoiceGuidanceBuffer.ID + "r");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("Language Identifier");
    }

    private static class FakeVoiceGuidanceBufferAppender extends VoiceGuidanceBufferAppender {
        public FakeVoiceGuidanceBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
            super(nextAppender);
        }

        @Override
        protected void skipBufferMeta(NdcCharBuffer ndcCharBuffer) {

        }
    }
}
