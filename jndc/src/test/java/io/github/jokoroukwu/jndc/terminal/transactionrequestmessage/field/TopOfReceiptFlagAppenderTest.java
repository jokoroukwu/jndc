package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class TopOfReceiptFlagAppenderTest extends TransactionRequestMsgAppenderTest {
    private TopOfReceiptTransactionFlagAppender appender;

    @DataProvider
    public static Object[][] invalidFieldSeparatorProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap("1")},
                {NdcCharBuffer.wrap("a")},
                {NdcCharBuffer.EMPTY},
        };
    }

    @DataProvider
    public static Object[][] validDataProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap("\u001C1A")},
                {NdcCharBuffer.wrap("\u001C0B")}
        };
    }

    @DataProvider
    public static Object[][] expectedValueProvider() {
        return new Object[][]{
                {NdcCharBuffer.wrap("\u001C1"), true},
                {NdcCharBuffer.wrap("\u001C0"), false}
        };
    }

    @BeforeMethod
    public void setUp() {
        appender = new TopOfReceiptTransactionFlagAppender(nextAppenderMock);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_flag() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("\u001C3");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("should be '0' or '1'");

        verifyNoInteractions(nextAppenderMock, deviceConfigurationMock);
    }

    @Test(dataProvider = "invalidFieldSeparatorProvider")
    public void should_throw_expected_exception_on_invalid_field_separator(NdcCharBuffer buffer) {
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining("missing field separator", TopOfReceiptTransactionFlagAppender.FIELD_NAME);

        verifyNoInteractions(nextAppenderMock, deviceConfigurationMock);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_not_throw_exception_on_valid_data(NdcCharBuffer buffer) {
        Assertions.assertThatCode(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .doesNotThrowAnyException();
    }

    @Test(dataProvider = "validDataProvider")
    public void should_call_next_appender(NdcCharBuffer buffer) {
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test(dataProvider = "expectedValueProvider")
    public void should_append_expected_value(NdcCharBuffer buffer, boolean expectedValue) {
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withTopOfReceipt(expectedValue));
    }

    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(NdcCharBuffer buffer) {
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
        Assertions.assertThat(buffer.remaining())
                .as("buffer should have 1 char remaining")
                .isOne();
    }
}
