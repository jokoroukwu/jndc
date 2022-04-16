package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.notesdata;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NotesDataBufferTest {
    private Note dummyNote1;
    private Note dummyNote2;

    @BeforeClass
    public void beforeClass() {
        final Random random = ThreadLocalRandom.current();
        dummyNote1 = new Note(ThreadLocalRandom.current().nextInt(0x01, 0xFF), random.nextInt(999) + 1);
        dummyNote2 = new Note(dummyNote1.getType() + 1, random.nextInt(999) + 1);
    }

    @DataProvider
    public Object[][] notesProvider() {
        final String note1String = dummyNote1.toNdcString();
        final String note2String = dummyNote2.toNdcString();
        return new Object[][]{
                {List.of(dummyNote1, dummyNote2), "02" + note1String + note2String},
                {List.of(dummyNote1), "01" + note1String},
                {Collections.emptyList(), "00"}
        };
    }

    @Test(dataProvider = "notesProvider")
    public void should_return_expected_suspect_notes_ndc_string(List<Note> notes, String expectedString) {
        Assertions.assertThat(NotesDataBuffer.suspectNotes(notes).toNdcString())
                .isEqualTo(NotesDataBuffer.SUSPECT_NOTES_DATA_ID + expectedString);
    }

    @Test(dataProvider = "notesProvider")
    public void should_return_expected_counterfeit_notes_ndc_string(List<Note> notes, String expectedString) {
        Assertions.assertThat(NotesDataBuffer.counterfeitNotes(notes).toNdcString())
                .isEqualTo(NotesDataBuffer.COUNTERFEIT_NOTES_DATA_ID + expectedString);
    }


    @Test
    public void should_throw_expected_exception_on_invalid_note_count() {
        final int noteCount = 100;
        final List<Note> notes = new ArrayList<>(noteCount);
        for (int i = 0; i < noteCount; i++) {
            notes.add(new Note(i + 1, 1));
        }
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> NotesDataBuffer.suspectNotes(notes))
                .as("suspect notes")
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of note types");

        softly.assertThatThrownBy(() -> NotesDataBuffer.counterfeitNotes(notes))
                .as("counterfeit notes")
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Number of note types");

        softly.assertAll();
    }
}
