package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NdcNoteTest {

    @Test
    public void should_return_expected_ndc_string() {
        final NdcNote ndcNote = new NdcNote(0xFFFF, 99999);
        Assertions.assertThat(ndcNote.toNdcString())
                .isEqualTo("FFFF99999");
    }

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {-1, 0, "Note Type Identifier"},
                {0, 0, "Note Type Identifier"},
                {0x10000, 0, "Note Type Identifier"},
                {1, -1, "Number of notes"},
                {1, 100000, "Number of notes"}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(int noteTypeId,
                                                                           int numberOfNotes,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> new NdcNote(noteTypeId, numberOfNotes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
