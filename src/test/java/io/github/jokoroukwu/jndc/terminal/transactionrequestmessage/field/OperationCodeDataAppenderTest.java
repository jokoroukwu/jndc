package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OperationCodeDataAppenderTest extends TransactionRequestMsgAppenderTest {
    private OperationCodeAppender appender;

    @DataProvider
    public static Object[][] validDataProvider() {
        final String randomValidData = BmpStringGenerator.HEX.fixedLength(8);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING, Strings.EMPTY_STRING},
                {NdcConstants.FIELD_SEPARATOR + randomValidData, randomValidData}
        };
    }

    @DataProvider
    public static Object[][] invalidDataProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(1))},
                {NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(7))}
        };
    }

    @BeforeMethod
    public void setUp() {
        appender = new OperationCodeAppender(nextAppenderMock);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData, String expectedData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withOperationCodeData(expectedData));

    }

    @Test(dataProvider = "validDataProvider")
    public void should_call_next_appender(String validData, String expectedData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Mockito.verify(nextAppenderMock, Mockito.times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_propagate_expected_exception_on_field_separator_absence() {
        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.EMPTY, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("missing field separator", OperationCodeAppender.FIELD_NAME);
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_propagate_expected_exception_on_insufficient_buffer_data(NdcCharBuffer buffer) {
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(OperationCodeAppender.FIELD_NAME);
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final String validData = NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(8);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + remainingData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length());
    }


}
