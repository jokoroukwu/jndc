package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NoteTest {

    @DataProvider
    public Object[][] expectedStringProvider() {
        return new Object[][]{
                {1, 1, "0001001"},
                {0x0F, 99, "000F099"},
                {0xAF, 999, "00AF999"}
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(int noteType, int numberOfNotes, String expectedString) {
        Assertions.assertThat(new Note(noteType, numberOfNotes).toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidNoteTypeProvider() {
        return new Object[][]{
                {-1},
                {0},
                {0x100}
        };
    }

    @Test(dataProvider = "invalidNoteTypeProvider")
    public void should_throw_expected_exception_on_invalid_note_type(int invalidNoteType) {
        Assertions.assertThatThrownBy(() -> new Note(invalidNoteType, 1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Note type identifier");
    }

    @DataProvider
    public Object[][] invalidNumberOfNotesProvider() {
        return new Object[][]{
                {-1},
                {0},
                {1000},
                {1001}
        };
    }

    @Test(dataProvider = "invalidNumberOfNotesProvider")
    public void should_throw_expected_exception_on_invalid_number_of_notes(int invalidNumberOfNotes) {
        Assertions.assertThatThrownBy(() -> new Note(1, invalidNumberOfNotes))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of notes");
    }
}
