package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.tlv.BerTlv;
import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SmartCardBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private final String smartCardDataId = BmpStringGenerator.HEX.fixedLength(3);
    private final String camFlags = BmpStringGenerator.HEX.fixedLength(4);
    private final int intCamFlags = Integer.parseInt(camFlags, 16);
    private final BerTlv<String> dummyDataObject = new HexStringBerTlv(0xFF, BmpStringGenerator.HEX.fixedLength(2));
    private final FakerTlvReader tlvReader = new FakerTlvReader(1, dummyDataObject);
    private SmartCardBufferAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new SmartCardBufferAppender(nextAppenderMock, tlvReader);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID, SmartCardBuffer.EMPTY},
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + smartCardDataId + camFlags + "A",
                        new SmartCardBuffer(new SmartCardData(intCamFlags, smartCardDataId, List.of(dummyDataObject)))}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, SmartCardBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withSmartCardBuffer(expectedValue));
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + NdcConstants.FIELD_SEPARATOR + "A", 2},
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + smartCardDataId + camFlags + "A" + NdcConstants.FIELD_SEPARATOR, 1}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, int expectedNumberOfRemainingCharacters) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(expectedNumberOfRemainingCharacters);
    }

    @DataProvider
    public Object[][] appenderCallDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID, 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + smartCardDataId + camFlags + "A" + NdcConstants.FIELD_SEPARATOR, 1}
        };
    }

    @Test(dataProvider = "appenderCallDataProvider")
    public void should_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }


    @Test
    public void should_throw_expected_exception_on_preceding_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Character.toString(SmartCardBuffer.ID));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("missing field separator")
                .hasMessageContaining(SmartCardBufferAppender.FIELD_NAME);
    }


    @Test
    public void should_throw_expected_exception_on_buffer_id_mismatch() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + ">");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(SmartCardBufferAppender.FIELD_NAME)
                .hasMessageContaining("ID");
    }


    @DataProvider
    public Object[][] invalidSmartCardDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + "AB", "Smart card data identifier"},
                {NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + smartCardDataId + "AB", "CAM Flags"}
        };
    }

    @Test(dataProvider = "invalidSmartCardDataProvider")
    public void should_throw_expected_exception_on_invalid_smart_card_data(String bufferData, String expectedMessagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(SmartCardBufferAppender.FIELD_NAME)
                .hasMessageContaining(expectedMessagePart);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_throw_expected_exception_on_data_object_reader_empty_result() {
        final NdcComponentReader<DescriptiveOptional<BerTlv<String>>> dataObjectsReaderMock = mock(NdcComponentReader.class);
        when(dataObjectsReaderMock.readComponent(any()))
                .thenReturn(DescriptiveOptional.empty());

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + SmartCardBuffer.ID + smartCardDataId + camFlags + "A");
        final SmartCardBufferAppender appender = new SmartCardBufferAppender(nextAppenderMock, dataObjectsReaderMock);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(SmartCardBufferAppender.FIELD_NAME)
                .hasMessageContaining("EMV data objects for authorisation");
    }

    private static final class FakerTlvReader implements NdcComponentReader<DescriptiveOptional<BerTlv<String>>> {
        private final int charsToSkip;
        private final BerTlv<String> dummyTlv;

        public FakerTlvReader(int charsToSkip, BerTlv<String> dummyTlv) {
            this.charsToSkip = charsToSkip;
            this.dummyTlv = dummyTlv;
        }

        @Override
        public DescriptiveOptional<BerTlv<String>> readComponent(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(charsToSkip);
            return DescriptiveOptional.of(dummyTlv);
        }
    }
}
