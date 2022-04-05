package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CashAcceptorBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private final CashAcceptorNote dummyNote = new TwoDigitNote(1, 1);
    private NdcComponentReaderFactory<ConfigurationOptions, CashAcceptorNote> noteReaderFactoryMock;
    private NdcComponentReader<CashAcceptorNote> fakeNoteReader;
    private CashAcceptorBufferAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        fakeNoteReader = new FakeNoteReader(dummyNote);
        noteReaderFactoryMock = mock(NdcComponentReaderFactory.class);

        when(deviceConfigurationMock.getConfigurationOptions())
                .thenReturn(ConfigurationOptions.EMPTY);

        when(noteReaderFactoryMock.getNoteReader(any()))
                .thenReturn(fakeNoteReader);

        appender = new CashAcceptorBufferAppender(nextAppenderMock, noteReaderFactoryMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID, CashAcceptorBuffer.EMPTY},
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID + "1", new CashAcceptorBuffer(List.of(dummyNote))},
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID + "12", new CashAcceptorBuffer(Collections.nCopies(2, dummyNote))},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, CashAcceptorBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder)
                .isEqualTo(new TransactionRequestMessageBuilder().withCashAcceptorBuffer(expectedValue));
    }

    @Test(dataProvider = "validDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, CashAcceptorBuffer expectedValue) {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData + NdcConstants.FIELD_SEPARATOR_STRING + remainingData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isEqualTo(remainingData.length() + 1);
    }

    @Test
    public void should_throw_expected_exception_on_buffer_id_mismatch() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + "\u0000");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(CashAcceptorBufferAppender.FIELD_NAME)
                .hasMessageContaining("ID");
    }

    @DataProvider
    public Object[][] nextAppenderCallProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID, 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + CashAcceptorBuffer.ID + "1" + NdcConstants.FIELD_SEPARATOR, 1}
        };
    }

    @Test(dataProvider = "nextAppenderCallProvider")
    public void should_optionally_call_next_appender(String bufferData, int expectedNumberOfCalls) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfCalls))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_preceding_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Character.toString(CashAcceptorBuffer.ID));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(CashAcceptorBufferAppender.FIELD_NAME)
                .hasMessageContaining("missing field separator");
    }

    private static final class FakeNoteReader implements NdcComponentReader<CashAcceptorNote> {
        private final CashAcceptorNote dummyNote;

        private FakeNoteReader(CashAcceptorNote dummyNote) {
            this.dummyNote = dummyNote;
        }

        @Override
        public CashAcceptorNote readComponent(NdcCharBuffer ndcCharBuffer) {
            ndcCharBuffer.skip(1);
            return dummyNote;
        }
    }
}
