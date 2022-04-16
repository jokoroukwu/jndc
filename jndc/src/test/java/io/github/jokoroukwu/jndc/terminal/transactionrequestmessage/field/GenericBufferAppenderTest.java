package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBuffer;
import io.github.jokoroukwu.jndc.genericbuffer.GenericBufferAppender;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GenericBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    final char id = '>';
    private GenericBufferAppender<TransactionRequestMessageBuilder> appender;

    @BeforeMethod
    public void setUp() {
        appender = new GenericBufferAppender<>(TransactionRequestMessageBuilder::putExitsBuffer, nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String lowerBoundary = BmpStringGenerator.HEX.fixedLength(1);
        final String upperBoundary = BmpStringGenerator.HEX.fixedLength(10);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id, new GenericBuffer(id)},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + lowerBoundary, new GenericBuffer(id, lowerBoundary)},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + upperBoundary, new GenericBuffer(id, upperBoundary)},
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, GenericBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getExitsBufferMap().get(id))
                .isEqualTo(expectedValue);
    }

    @DataProvider
    public Object[][] nextAppenderCallProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "A", 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "A" + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(10), 0}
        };
    }

    @Test(dataProvider = "nextAppenderCallProvider")
    public void should_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("A");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("buffer ID")
                .hasMessageContaining("missing field separator");
    }

    @Test
    public void should_throw_expected_exception_on_buffer_id_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("buffer ID");
    }
}
