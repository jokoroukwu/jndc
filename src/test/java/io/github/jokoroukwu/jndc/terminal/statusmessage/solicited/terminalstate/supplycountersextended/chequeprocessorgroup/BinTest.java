package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BinTest {

    @Test
    public void should_return_expected_ndc_string() {
        final Bin bin = new Bin(1, 99999);
        Assertions.assertThat(bin.toNdcString())
                .isEqualTo("199999");
    }

    @DataProvider
    public Object[][] invalidArgProvider() {
        return new Object[][]{
                {0, 0, "Bin Number"},
                {10, 99999, "Bin Number"},

                {9, -1, "Cheques deposited in bin"},
                {9, 100_000, "Cheques deposited in bin"}
        };
    }

    @Test(dataProvider = "invalidArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(int binNumber,
                                                                           int chequesDeposited,
                                                                           String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> new Bin(binNumber, chequesDeposited))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
