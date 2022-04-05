package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CashHandlerCassetteCountersReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CashHandlerCassetteCountersReaderTest {
    private List<CassetteCounters> minNumberOfCassettes;
    private List<CassetteCounters> maxNumberOfCassettes;

    private List<CassetteCounters> minNumberOfCassettesWithDepositedNotes;
    private List<CassetteCounters> maxNumberOfCassettesWithDepositedNotes;

    @BeforeClass
    public void setUp() {
        final CassetteCounters cassetteWithNoDepositedNotes = CassetteCounters.builder()
                .withCassetteType(1)
                .withNotesInCassette(10)
                .withNotesDispensed(3)
                .withNotesRejected(2)
                .withLastTransactionNotesDispensed(99999)
                .buildCashHandlerCassetteCounters();
        minNumberOfCassettes = Collections.nCopies(CashHandlerCassetteCountersReader.MIN_CASSETTE_NUMBER, cassetteWithNoDepositedNotes);
        maxNumberOfCassettes = Collections.nCopies(CashHandlerCassetteCountersReader.MAX_CASSETTE_NUMBER, cassetteWithNoDepositedNotes);

        final CassetteCounters cassetteWithDepositedNotes = cassetteWithNoDepositedNotes.copy()
                .withNotesDeposited(1)
                .buildCashHandlerCassetteCounters();
        minNumberOfCassettesWithDepositedNotes = Collections.nCopies(CashHandlerCassetteCountersReader.MIN_CASSETTE_NUMBER, cassetteWithDepositedNotes);
        maxNumberOfCassettesWithDepositedNotes = Collections.nCopies(CashHandlerCassetteCountersReader.MAX_CASSETTE_NUMBER, cassetteWithDepositedNotes);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String fourCassettesNdcString = minNumberOfCassettes
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining());

        final String sevenCassettesNdcString = maxNumberOfCassettes
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining());

        final String fourCassettesWithDepositedNotesNdcString = minNumberOfCassettesWithDepositedNotes
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining());

        final String sevenCassettesWithDepositedNotesNdcString = maxNumberOfCassettesWithDepositedNotes
                .stream()
                .map(NdcComponent::toNdcString)
                .collect(Collectors.joining());

        return new Object[][]{
                {fourCassettesNdcString, minNumberOfCassettes},
                {sevenCassettesNdcString, maxNumberOfCassettes},
                {fourCassettesWithDepositedNotesNdcString, minNumberOfCassettesWithDepositedNotes},
                {sevenCassettesWithDepositedNotesNdcString, maxNumberOfCassettesWithDepositedNotes},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String bufferData, List<CassetteCounters> expectedCassettes) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final List<CassetteCounters> actualCassettes = CashHandlerCassetteCountersReader.INSTANCE.readComponent(buffer);

        Assertions.assertThat(actualCassettes)
                .isEqualTo(expectedCassettes);

        Assertions.assertThat(buffer.remaining())
                .as("no characters should remain in buffer")
                .isZero();
    }

}
