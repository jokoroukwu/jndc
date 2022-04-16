package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dpmgroup;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DpmGroupTest {

    @Test
    public void should_return_expected_ndc_string() {
        final DpmDataGroup dpmDataGroup = new DpmDataGroup("12", "12345");
        final String expectedString = DpmDataGroup.ID + "1212345";

        Assertions.assertThat(dpmDataGroup.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidConstructorArgProvider() {
        return new Object[][]{
                {"1", "12345", "Deposit Bin Identifier"},
                {"12", "1234", "Documents Deposited in Bin"}
        };
    }

    @Test(dataProvider = "invalidConstructorArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(String depositBinId,
                                                                           String documentsDeposited,
                                                                           String expectedMessagePart) {

        Assertions.assertThatThrownBy(() -> new DpmDataGroup(depositBinId, documentsDeposited))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
