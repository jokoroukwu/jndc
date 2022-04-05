package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MessageCoordinationNumberAppenderTest extends TransactionRequestMsgAppenderTest {
    private MessageCoordinationNumberAppender<TransactionRequestMessageBuilder> appender;

    @BeforeMethod
    public void setUp() {
        appender = new MessageCoordinationNumberAppender<>("'command'", false, nextAppenderMock);
    }

    @Test
    public void should_append_expected_value() {
        final char validNumber = BmpStringGenerator.ofCharacterRange(0x31, 0x7E).randomChar();
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(new char[]{validNumber});
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withMessageCoordinationNumber(validNumber));
        Mockito.verify(nextAppenderMock, Mockito.times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_number() {
        final char invalidNumber = BmpStringGenerator.ofCharacterRanges(0, 0x30, 0x7F, 0x7F).randomChar();
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(new char[]{invalidNumber});
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(MessageCoordinationNumberAppender.FIELD_NAME, "should be within range 0x31-0x7E");
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final char validNumber = BmpStringGenerator.ofCharacterRange(0x31, 0x7E).randomChar();
        final String randomRemainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validNumber + randomRemainingData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("should have expected number of characters left")
                .isEqualTo(randomRemainingData.length());
    }

    @Test
    public void should_propagate_expected_exception_on_empty_buffer() {
        Assertions.assertThatThrownBy(() -> appender.appendComponent(NdcCharBuffer.EMPTY, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class);
    }
}
