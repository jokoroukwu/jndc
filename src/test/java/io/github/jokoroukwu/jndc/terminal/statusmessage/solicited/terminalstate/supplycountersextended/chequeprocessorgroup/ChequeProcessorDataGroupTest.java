package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

public class ChequeProcessorDataGroupTest {
    private final Bin dummyBin = new Bin(1, 99999);

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {List.of(dummyBin), ChequeProcessorDataGroup.ID + dummyBin.toNdcString()},
                {List.of(dummyBin, dummyBin), ChequeProcessorDataGroup.ID + dummyBin.toNdcString().repeat(2)},
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(Collection<Bin> bins, String expectedString) {
        Assertions.assertThat(new ChequeProcessorDataGroup(bins).toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidBinProvider() {
        return new Object[][]{
                {null},
                {List.of()}
        };
    }

    @Test(dataProvider = "invalidBinProvider")
    public void should_throw_expected_exception_on_invalid_bins(Collection<Bin> invalidBins) {
        Assertions.assertThatThrownBy(() -> new ChequeProcessorDataGroup(invalidBins))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
