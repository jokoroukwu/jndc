package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;

public class TimeVariantNumberAppenderTest extends TransactionRequestMsgAppenderTest {
    private final String randomTimeVariantNumberString = BmpStringGenerator.HEX.fixedLength(8);
    private final long randomTimeVariantNumber = Long.parseLong(randomTimeVariantNumberString, 16);

    private TimeVariantNumberAppender appender;

    @DataProvider
    public static Object[][] invalidFieldSeparatorProvider() {
        return new Object[][]{
                {NdcCharBuffer.EMPTY},
                {NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + "as")},
                {NdcCharBuffer.wrap("as")}
        };
    }

    @BeforeMethod
    public void setUp() {
        appender = new TimeVariantNumberAppender(nextAppenderMock);
    }

    @Test(dataProvider = "invalidFieldSeparatorProvider")
    public void should_throw_exception_on_invalid_field_separator(NdcCharBuffer buffer) {
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("field separator", TimeVariantNumberAppender.FIELD_NAME);
    }

    @Test
    public void should_append_expected_value() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + randomTimeVariantNumberString);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withTimeVariantNumber(randomTimeVariantNumber));
    }

    @Test
    public void should_call_next_appender() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + randomTimeVariantNumberString);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
        Mockito.verify(nextAppenderMock, times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);

    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + randomTimeVariantNumberString + remainingData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("should have expected number of characters left")
                .isEqualTo(remainingData.length());
    }

    @Test
    public void should_propagate_expected_exception_on_invalid_time_variant_number() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + "AB");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TimeVariantNumberAppender.FIELD_NAME);
    }

}
