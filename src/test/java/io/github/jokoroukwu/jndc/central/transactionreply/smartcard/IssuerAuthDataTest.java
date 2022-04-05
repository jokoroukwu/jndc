package io.github.jokoroukwu.jndc.central.transactionreply.smartcard;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class IssuerAuthDataTest {


    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(14)},
                {BmpStringGenerator.HEX.fixedLength(34)},
                {BmpStringGenerator.HEX.fixedLength(31)},
                {BmpStringGenerator.HEX.fixedLength(9)},
                {"GHI"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_tag_data(String data) {
        Assertions.assertThatThrownBy(() -> new IssuerAuthData(data))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void should_return_expected_ndc_string() {
        final String value = BmpStringGenerator.HEX.fixedLength(16);
        Assertions.assertThat(new IssuerAuthData(value).toNdcString())
                .isEqualTo("9108" + value);
    }
}
