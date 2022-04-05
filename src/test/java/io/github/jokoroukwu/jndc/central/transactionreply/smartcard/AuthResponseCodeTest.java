package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuthResponseCodeTest {


    @Test
    public void should_return_expected_ndc_string() {
        final String value = BmpStringGenerator.HEX.fixedLength(4);

        Assertions.assertThat(new AuthResponseCode(value).toNdcString())
                .isEqualTo("8A02" + value);
    }

    @DataProvider
    public Object[][] invalidValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(1)},
                {BmpStringGenerator.HEX.fixedLength(2)},
                {BmpStringGenerator.HEX.fixedLength(6)},
                {"JIK"},
                {Strings.EMPTY_STRING},
                {null},
        };
    }

    @Test(dataProvider = "invalidValueProvider")
    public void should_throw_expected_exception_on_invalid_value(String invalidValue) {
        Assertions.assertThatThrownBy(() -> new AuthResponseCode(invalidValue))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
