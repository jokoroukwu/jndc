package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.bnacounters;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BnaCountersTest {

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {100_000, 1, 1, 1, "Notes Refunded"},
                {-1, 1, 1, 1, "Notes Refunded"},

                {1, 100_000, 1, 1, "Notes Rejected"},
                {1, -1, 1, 1, "Notes Rejected"},

                {1, 1, 100_000, 1, "Notes Encashed"},
                {1, 1, -1, 1, "Notes Encashed"},

                {1, 1, 1, 100_000, "Notes Escrowed"},
                {1, 1, 1, -1, "Notes Escrowed"}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(int notesRefunded, int notesRejected,
                                                                           int notesEncashed, int notesEscrowed,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> new BnaCounters(notesRefunded, notesRejected, notesEncashed, notesEscrowed))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {99999, 1, 1, 1, "99999000010000100001"},
                {0, 1, 1, 1, "00000000010000100001"},

                {1, 99999, 1, 1, "00001999990000100001"},
                {1, 0, 1, 1, "00001000000000100001"},

                {1, 1, 99999, 1, "00001000019999900001"},
                {1, 1, 0, 1, "00001000010000000001"},

                {1, 1, 1, 99999, "00001000010000199999"},
                {1, 1, 1, 0, "00001000010000100000"},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(int notesRefunded, int notesRejected,
                                                  int notesEncashed, int notesEscrowed,
                                                  String expectedNdcString) {
        Assertions.assertThat(new BnaCounters(notesRefunded, notesRejected, notesEncashed, notesEscrowed).toNdcString())
                .isEqualTo(expectedNdcString);
    }
}
