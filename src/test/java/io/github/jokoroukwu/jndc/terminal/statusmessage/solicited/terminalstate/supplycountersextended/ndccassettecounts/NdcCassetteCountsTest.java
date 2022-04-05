package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NdcCassetteCountsTest {
    private final List<NdcNote> notes = Collections.nCopies(2, new NdcNote(1, 1));

    @Test
    public void should_return_expected_ndc_string() {
        final String notesString = notes.stream().map(NdcNote::toNdcString).collect(Collectors.joining());
        final NdcCassetteCounts ndcCassetteCounts = new NdcCassetteCounts(1, notes);
        Assertions.assertThat(ndcCassetteCounts.toNdcString())
                .isEqualTo("00100002002"+ notesString);
    }

    @Test
    public void should_return_expected_total_notes_in_cassette_value() {
        final NdcCassetteCounts cassette = new NdcCassetteCounts(999, notes);
        Assertions.assertThat(cassette.getTotalNotesInCassette())
                .isEqualTo(2);
    }

    @Test
    public void should_return_expected_note_types_reported_value() {
        final NdcCassetteCounts cassette = new NdcCassetteCounts(999, notes);
        Assertions.assertThat(cassette.getNumberOfNoteTypesReported())
                .isEqualTo(2);
    }

    @DataProvider
    public Object[][] invalidCassetteTypeProvider() {
        return new Object[][]{{-1}, {0}, {1000}};
    }

    @Test(dataProvider = "invalidCassetteTypeProvider")
    public void should_throw_expected_exception_on_invalid_cassette_type(int invalidCassetteType) {
        Assertions.assertThatThrownBy(() -> new NdcCassetteCounts(invalidCassetteType, notes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NDC Cassette Type");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_total_notes_in_cassette() {
        final List<NdcNote> notes = Collections.nCopies(2, new NdcNote(1, 50000));
        Assertions.assertThatThrownBy(() -> new NdcCassetteCounts(1, notes))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Notes In Cassette");
    }

}
