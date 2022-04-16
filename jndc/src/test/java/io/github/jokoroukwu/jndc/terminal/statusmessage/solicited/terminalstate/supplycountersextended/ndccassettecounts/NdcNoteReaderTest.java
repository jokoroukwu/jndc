package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class NdcNoteReaderTest {
    private final NdcNote dummyNote = new NdcNote(1, 500);

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {"000", List.of()},
                {"001" + dummyNote.toNdcString(), List.of(dummyNote)},
                {"002" + dummyNote.toNdcString().repeat(2), List.of(dummyNote, dummyNote)},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String bufferData, List<NdcNote> expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final List<NdcNote> result = NdcNoteReader.INSTANCE.readComponent(buffer);

        Assertions.assertThat(result)
                .isEqualTo(expectedResult);

    }
}
