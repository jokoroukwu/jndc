package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SingleChequeDepositDataTest {

    @DataProvider
    public Object[][] validCodeLineValueProvider() {
        final String codeLineValue = BmpStringGenerator.HEX.randomLength(256);
        return new Object[][]{
                {codeLineValue, '1' + codeLineValue},
                {Strings.EMPTY_STRING, String.valueOf('0')}
        };
    }

    @Test(dataProvider = "validCodeLineValueProvider")
    public void should_return_expected_ndc_string(String codeLineValue, String expectedString) {
        Assertions.assertThat(new SingleChequeDepositData(codeLineValue).toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidCodeLineValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(257)},
                {null}
        };
    }

    @Test(dataProvider = "invalidCodeLineValueProvider")
    public void should_throw_expected_exception_on_invalid_codeline_value(String invalidCodeLineData) {
        Assertions.assertThatThrownBy(() -> new SingleChequeDepositData(invalidCodeLineData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Codeline value");
    }

}
