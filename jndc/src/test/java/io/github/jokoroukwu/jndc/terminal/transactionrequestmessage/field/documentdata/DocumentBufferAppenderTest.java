package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DocumentBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private DocumentBufferAppender appender;

    @DataProvider
    public static Object[][] validDataProvider() {
        final String minCodeLineData = BmpStringGenerator.HEX.fixedLength(1);
        final String maxCodeLineData = BmpStringGenerator.HEX.fixedLength(256);

        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID, DocumentBuffer.EMPTY},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "0", new DocumentBuffer(SingleChequeDepositData.EMPTY)},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + minCodeLineData, new DocumentBuffer(new SingleChequeDepositData(minCodeLineData))},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + maxCodeLineData, new DocumentBuffer(new SingleChequeDepositData(maxCodeLineData))}
        };
    }

    @DataProvider
    public static Object[][] remainingDataProvider() {
        final String minCodeLineData = BmpStringGenerator.HEX.fixedLength(1);
        final String maxCodeLineData = BmpStringGenerator.HEX.fixedLength(257);

        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + NdcConstants.FIELD_SEPARATOR_STRING},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "0" + NdcConstants.FIELD_SEPARATOR},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + minCodeLineData + NdcConstants.FIELD_SEPARATOR},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + maxCodeLineData}
        };
    }

    @DataProvider
    public static Object[][] nextAppenderCallProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID, 0},

                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "0" + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "0", 0},

                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + "A" + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + "1" + "A", 0}
        };
    }

    @BeforeMethod
    public void setUp() {
        appender = new DocumentBufferAppender(nextAppenderMock);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, DocumentBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withDocumentBuffer(expectedValue));
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isOne();
    }

    @Test(dataProvider = "nextAppenderCallProvider")
    public void should_optionally_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }


    @Test
    public void should_throw_expected_exception_on_buffer_id_mismatch() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + "\u0000");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(DocumentBufferAppender.FIELD_NAME)
                .hasMessageContaining("ID");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_codeline_detected() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + DocumentBuffer.ID + '\u0000');

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(DocumentBufferAppender.FIELD_NAME)
                .hasMessageContaining("Codeline detected")
                .hasMessageContaining("should be '1' or '0'");
    }
}
