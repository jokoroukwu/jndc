package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.BiConsumer;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.mockito.Mockito.*;

public class CspBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private final String fieldName = BmpStringGenerator.ALPHANUMERIC.fixedLength(10);
    private final char id = CspData.CSP_DATA_ID;
    private BiConsumer<TransactionRequestMessageBuilder, CspData> dataConsumerMock;
    private CspBufferAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        dataConsumerMock = mock(BiConsumer.class);
        appender = new CspBufferAppender(fieldName, CspData.CSP_DATA_ID, nextAppenderMock, dataConsumerMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String minLengthData = BmpStringGenerator.HEX.fixedLength(1);
        final String maxLengthData = BmpStringGenerator.HEX.fixedLength(16);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id, new CspData(id, EMPTY_STRING)},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + minLengthData, new CspData(id, minLengthData)},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + maxLengthData, new CspData(id, maxLengthData)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validBufferData, CspData expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validBufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(dataConsumerMock, times(1))
                .accept(messageBuilder, expectedValue);
        verifyNoMoreInteractions(dataConsumerMock);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(1) + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(17), 1}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, int remainingDataLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingDataLength);
    }

    @Test
    public void should_throw_expected_exception_on_preceding_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("A");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(fieldName)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("missing field separator");
    }

    @DataProvider
    public Object[][] nextAppenderCallDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(1) + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(16), 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + BmpStringGenerator.HEX.fixedLength(17), 1}
        };
    }

    @Test(dataProvider = "nextAppenderCallDataProvider")
    public void should_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }
}
