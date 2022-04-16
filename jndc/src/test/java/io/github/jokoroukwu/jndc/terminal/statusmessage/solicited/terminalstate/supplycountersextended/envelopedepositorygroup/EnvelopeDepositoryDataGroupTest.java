package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EnvelopeDepositoryDataGroupTest {


    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {new EnvelopeDepositoryDataGroup(1, 1), "F0000100001"},
                {new EnvelopeDepositoryDataGroup(99999, 99999), "F9999999999"}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(EnvelopeDepositoryDataGroup dataGroup, String expectedString) {
        Assertions.assertThat(dataGroup.toNdcString())
                .isEqualTo(expectedString);
    }
}
