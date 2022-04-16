package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup.CashHandlerDataGroup.HANDLER_0_ID;

public class CashHandlerDataGroupTest {
    private final CassetteCounters dummyCassette = CassetteCounters.builder()
            .withCassetteType(1)
            .withNotesInCassette(10)
            .withNotesDispensed(3)
            .withNotesRejected(2)
            .withLastTransactionNotesDispensed(99999)
            .withNotesDeposited(1)
            .buildCashHandlerCassetteCounters();

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {Collections.nCopies(4, dummyCassette), HANDLER_0_ID + dummyCassette.toNdcString().repeat(4)},
                {Collections.nCopies(7, dummyCassette), HANDLER_0_ID + dummyCassette.toNdcString().repeat(7)}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(Collection<CassetteCounters> cassettes, String expectedNdcString) {
        final CashHandlerDataGroup cashHandlerDataGroup = CashHandlerDataGroup.handler0(cassettes);
        Assertions.assertThat(cashHandlerDataGroup.toNdcString())
                .isEqualTo(expectedNdcString);
    }
}
