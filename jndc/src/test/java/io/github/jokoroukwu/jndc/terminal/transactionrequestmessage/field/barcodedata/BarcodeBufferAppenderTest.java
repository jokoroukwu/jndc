package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.Result;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class BarcodeBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private BarcodeBufferAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new BarcodeBufferAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, BarCodeBuffer.EMPTY},
                {"0000" + "00", new BarCodeBuffer(new BarcodeData(0))},
                {"FFFF" + "00" + "A", new BarCodeBuffer(new BarcodeData(0xFFFF, "A"))},
                {"0001" + "00" + "AB", new BarCodeBuffer(new BarcodeData(1, "AB"))},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, BarCodeBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + BarCodeBuffer.ID + bufferData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withBarCodeBuffer(expectedValue));
    }

    @DataProvider
    public Object[][] appenderCallProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING, 0},
                {Strings.EMPTY_STRING + NdcConstants.FIELD_SEPARATOR, 1},
                {"0000" + "00", 0},
                {"0000" + "00" + NdcConstants.FIELD_SEPARATOR, 1},
                {"FFFF" + "00" + "A", 0},
                {"FFFF" + "00" + "A" + NdcConstants.FIELD_SEPARATOR, 1}
        };
    }

    @Test(dataProvider = "appenderCallProvider")
    public void should_optionally_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + BarCodeBuffer.ID + bufferData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @DataProvider
    public Object[][] remainingTestDataProvider() {
        return new Object[][]{
                {Strings.EMPTY_STRING},
                {"0000" + "00"},
                {"FFFF" + "00" + "A"}
        };
    }

    @Test(dataProvider = "remainingTestDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData) {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + BarCodeBuffer.ID + bufferData + NdcConstants.FIELD_SEPARATOR + remainingData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length() + 1);
    }

    @Test
    public void should_call_expected_method() {
        final FakerBarcodeBufferAppender bufferAppenderMock = mock(FakerBarcodeBufferAppender.class, CALLS_REAL_METHODS);

        Result.ofVoid(() -> bufferAppenderMock.appendComponent(NdcCharBuffer.EMPTY, messageBuilder, deviceConfigurationMock));

        verify(bufferAppenderMock, times(1))
                .skipBufferMeta(NdcCharBuffer.EMPTY);
    }

    @DataProvider
    public Object[][] invalidBarcodeDataProvider() {
        return new Object[][]{
                {"000", "Barcode Format Identifier"},
                {"0000" + "0", "Reserved Field"}
        };
    }

    @Test(dataProvider = "invalidBarcodeDataProvider")
    public void should_throw_expected_exception_on_invalid_barcode_data(String invalidData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + BarCodeBuffer.ID + invalidData);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(BarcodeBufferAppender.FIELD_NAME)
                .hasMessageContaining(expectedMessagePart);
    }


    private static class FakerBarcodeBufferAppender extends BarcodeBufferAppender {
        @Override
        protected void skipBufferMeta(NdcCharBuffer ndcCharBuffer) {

        }
    }
}
