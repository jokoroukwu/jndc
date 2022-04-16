package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata;

import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CspDataTest {


    @DataProvider
    public Object[][] invalidCspValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(17)},
                {BmpStringGenerator.HEX.fixedLength(18)},
                {null}
        };
    }

    @Test(dataProvider = "invalidCspValueProvider")
    public void should_throw_expected_exception_on_invalid_csp_value(String invalidCspValue) {
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatThrownBy(() -> CspData.csp(invalidCspValue))
                .as("csp")
                .isExactlyInstanceOf(IllegalArgumentException.class);

        softly.assertThatThrownBy(() -> CspData.confirmationCsp(invalidCspValue))
                .as("confirmation csp")
                .isExactlyInstanceOf(IllegalArgumentException.class);
        softly.assertAll();
    }

    @DataProvider
    public Object[][] validCspValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(16)},
                {BmpStringGenerator.HEX.fixedLength(1)},
                {Strings.EMPTY_STRING}
        };
    }

    @Test(dataProvider = "validCspValueProvider")
    public void should_not_throw_exception_on_valid_csp_value(String validCspValue) {
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThatCode(() -> CspData.csp(validCspValue))
                .as("csp")
                .doesNotThrowAnyException();

        softly.assertThatCode(() -> CspData.confirmationCsp(validCspValue))
                .as("confirmation csp")
                .doesNotThrowAnyException();
        softly.assertAll();

    }

    @Test
    public void should_return_expected_ndc_string() {
        final String randomValue = BmpStringGenerator.ALPHANUMERIC.randomLength(16);

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(CspData.csp(randomValue).toNdcString())
                .as("csp")
                .isEqualTo(CspData.CSP_DATA_ID + randomValue);

        softly.assertThat(CspData.confirmationCsp(randomValue).toNdcString())
                .as("confirmation csp")
                .isEqualTo(CspData.CONFIRMATION_CSP_DATA_ID + randomValue);
        softly.assertAll();
    }
}
