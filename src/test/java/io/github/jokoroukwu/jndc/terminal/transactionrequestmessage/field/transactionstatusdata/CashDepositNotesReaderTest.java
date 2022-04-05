package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CashDepositNotesReaderTest {
    private final CashDepositNotesReader reader = CashDepositNotesReader.INSTANCE;

    @Test
    public void should_return_expected_notes() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00000" + "90000" + "00001" + "00300");

        final CashDepositNotes expectedNotes = new CashDepositNotes(0, 90000, 1, 300);
        final CashDepositNotes actualNotes = reader.readComponent(buffer);

        Assertions.assertThat(actualNotes)
                .isEqualTo(expectedNotes);
    }

    @DataProvider
    public  Object[][] invalidNoteDataProvider() {
        return new Object[][]{
                {"0000", "Number of Notes Refunded"},
                {"00000" + "00", "Number of Notes Rejected"},
                {"00000" + "00000", "Number of Notes Encashed"},
                {"00000" + "00000" + "00000", "Number of Notes Escrowed"},
        };
    }

    @Test(dataProvider = "invalidNoteDataProvider")
    public void should_throw_expected_exception_on_invalid_note_data(String invalidData, String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> reader.readComponent(NdcCharBuffer.wrap(invalidData)))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(expectedMessagePart);
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("00000".repeat(4) + remainingData);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length());
    }
}
