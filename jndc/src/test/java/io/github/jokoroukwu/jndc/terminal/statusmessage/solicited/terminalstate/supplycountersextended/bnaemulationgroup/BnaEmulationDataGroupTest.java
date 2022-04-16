package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositNotes;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class BnaEmulationDataGroupTest {
    final CashDepositNotes dummyCashDepositNotes = new CashDepositNotes(1, 1, 1, 1);

    @Test
    public void should_return_expected_ndc_string() {
        final BnaEmulationDepositDataGroup dataGroup = new BnaEmulationDepositDataGroup(dummyCashDepositNotes);

        Assertions.assertThat(dataGroup.toNdcString())
                .isEqualTo(BnaEmulationDepositDataGroup.ID + dummyCashDepositNotes.toNdcString());
    }
}
