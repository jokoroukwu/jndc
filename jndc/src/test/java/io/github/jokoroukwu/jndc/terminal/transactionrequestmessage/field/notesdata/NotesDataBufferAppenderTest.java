package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

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

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;

public class NotesDataBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private final String fieldName = BmpStringGenerator.HEX.fixedLength(10);
    private final char id = BmpStringGenerator.HEX.randomChar();
    private BiConsumer<TransactionRequestMessageBuilder, NotesDataBuffer> dataConsumerMock;
    private NotesDataBufferAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        dataConsumerMock = mock(BiConsumer.class);
        appender = new NotesDataBufferAppender(fieldName, id, nextAppenderMock, dataConsumerMock);
    }


    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id, new NotesDataBuffer(id, null)},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "010001001", new NotesDataBuffer(id, List.of(new Note(1, 1)))},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "99" + "00FF999".repeat(99),
                        new NotesDataBuffer(id, Collections.nCopies(99, new Note(0xFF, 999)))},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String bufferData, NotesDataBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(dataConsumerMock, times(1))
                .accept(messageBuilder, expectedValue);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + NdcConstants.FIELD_SEPARATOR_STRING},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "010001001" + NdcConstants.FIELD_SEPARATOR},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "99" + "00FF999".repeat(99) + NdcConstants.FIELD_SEPARATOR}
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isOne();
    }

    @DataProvider
    public Object[][] appenderCallProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + NdcConstants.FIELD_SEPARATOR_STRING, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id, 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "010001001" + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "010001001", 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "99" + "00FF999".repeat(99) + NdcConstants.FIELD_SEPARATOR, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "99" + "00FF999".repeat(99), 0}
        };
    }

    @Test(dataProvider = "appenderCallProvider")
    public void should_optionally_call_next_appender(String bufferData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_throw_expected_exception_on_id_mismatch() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + "\u0000");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(fieldName)
                .hasMessageContainingAll("ID", Character.toString(id));
    }

    @Test
    public void should_throw_expected_exception_on_preceding_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Character.toString(id));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(fieldName)
                .hasMessageContaining("missing field separator")
                .hasMessageContainingAll(Character.toString(id), "ID");
    }

    @DataProvider
    public Object[][] invalidNoteTypeProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "010000"},
                {NdcConstants.FIELD_SEPARATOR_STRING + id + "0101FF"}
        };
    }

    @Test(dataProvider = "invalidNoteTypeProvider")
    public void should_throw_expected_exception_on_invalid_note_type(String bufferData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(fieldName)
                .hasMessageContaining("Note type identifier");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_number_of_notes() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + id + "010001000");

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining(fieldName)
                .hasMessageContaining("Number of notes");
    }
}
