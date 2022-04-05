package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CashDepositNotesTest {

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {-1, 0, 0, 0, "Number of Notes Refunded"},
                {100_000, 0, 0, 0, "Number of Notes Refunded"},
                {0, -1, 0, 0, "Number of Notes Rejected"},
                {0, 100_000, 0, 0, "Number of Notes Rejected"},
                {0, 0, -1, 0, "Number of Notes Encashed"},
                {0, 0, 100_000, 0, "Number of Notes Encashed"},
                {0, 0, 0, -1, "Number of Notes Escrowed"},
                {0, 0, 0, 100_000, "Number of Notes Escrowed"}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_args(int notesRefunded,
                                                                            int notesRejected,
                                                                            int notesEncashed,
                                                                            int notesEscrowed,
                                                                            String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new CashDepositNotes(notesRefunded, notesRejected, notesEncashed, notesEscrowed))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @DataProvider
    public Object[][] validArgProvider() {
        return new Object[][]{
                {99999, 0, 0, 0},
                {0, 99999, 0, 0},
                {0, 0, 99999, 0},
                {0, 0, 0, 99999}
        };
    }

    @Test(dataProvider = "validArgProvider")
    public void should_not_throw_exception_on_valid_constructor_args(int notesRefunded,
                                                                     int notesRejected,
                                                                     int notesEncashed,
                                                                     int notesEscrowed) {
        Assertions.assertThatCode(() -> new CashDepositNotes(notesRefunded, notesRejected, notesEncashed, notesEscrowed))
                .doesNotThrowAnyException();
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String actualString = new CashDepositNotes(0, 1, 99, 99999)
                .toNdcString();
        Assertions.assertThat(actualString)
                .isEqualTo("00000000010009999999");
    }
}
