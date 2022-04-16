package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CassetteCountersTest {

    @DataProvider
    public Object[][] validDataProvider() {
        final CassetteCounters withDepositedNotes = CassetteCounters.builder()
                .withCassetteType(0)
                .withNotesInCassette(10)
                .withNotesDispensed(3)
                .withNotesRejected(2)
                .withLastTransactionNotesDispensed(99999)
                .withNotesDeposited(1)
                .buildCashHandlerCassetteCounters();
        final CassetteCounters withNoDepositedNotes = withDepositedNotes.copy()
                .withNotesDeposited(-1)
                .buildCashHandlerCassetteCounters();
        return new Object[][]{
                {withDepositedNotes, "0000001000002000039999900001"},
                {withNoDepositedNotes, "00000010000020000399999"}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(CassetteCounters cassette, String expectedNdcString) {
        Assertions.assertThat(cassette.toNdcString())
                .isEqualTo(expectedNdcString);
    }

    @DataProvider
    public Object[][] invalidBuilderProvider() {
        final CassetteCounters validCassette = CassetteCounters.builder()
                .withCassetteType(1)
                .withNotesInCassette(10)
                .withNotesDispensed(3)
                .withNotesRejected(2)
                .withLastTransactionNotesDispensed(99999)
                .withNotesDeposited(1)
                .buildCashHandlerCassetteCounters();

        return new Object[][]{
                {validCassette.copy().withCassetteType(-1), "Cassette Type"},
                {validCassette.copy().withCassetteType(8), "Cassette Type"},

                {validCassette.copy().withNotesInCassette(-1), "Notes In Cassette"},
                {validCassette.copy().withNotesInCassette(100_000), "Notes In Cassette"},

                {validCassette.copy().withNotesDispensed(-1), "Notes Dispensed"},
                {validCassette.copy().withNotesDispensed(100_000), "Notes Dispensed"},

                {validCassette.copy().withLastTransactionNotesDispensed(-1), "Last Transaction Notes Dispensed"},
                {validCassette.copy().withLastTransactionNotesDispensed(100_000), "Last Transaction Notes Dispensed"},

                {validCassette.copy().withNotesDeposited(100_000), "Notes Deposited"},
        };
    }

    @Test(dataProvider = "invalidBuilderProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(CassetteCountersBuilder builder,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(builder::buildCashHandlerCassetteCounters)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
