package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure;

import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.util.Integers;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PowerFailureTest {

    @DataProvider
    public Object[][] invalidConfigIdProvider() {
        return new Object[][]{
                {-1},
                {10000}
        };
    }

    @Test(dataProvider = "invalidConfigIdProvider")
    public void should_throw_expected_exception_on_invalid_config_id(int invalidConfigId) {
        Assertions.assertThatThrownBy(()-> new PowerFailure(invalidConfigId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration ID");
    }


    @DataProvider
    public Object[][] validConfigIdProvider() {
        return new Object[][]{
                {0},
                {9999}
        };
    }

    @Test(dataProvider = "validConfigIdProvider")
    public void should_return_expected_ndc_string(int configId) {
        final String actualNdcString = new PowerFailure(configId).toNdcString();
        final String expectedNdcString = Dig.COMMUNICATIONS.toNdcString() + Integers.toZeroPaddedString(configId, 4);

        Assertions.assertThat(actualNdcString)
                .isEqualTo(expectedNdcString);
    }
}
