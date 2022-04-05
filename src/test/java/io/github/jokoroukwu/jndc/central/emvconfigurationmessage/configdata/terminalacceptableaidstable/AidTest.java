package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;

public class AidTest {


    @DataProvider
    public static Object[][] invalidAidValueProvider() {
        return new Object[][]{
                {null},
                {EMPTY_STRING},
                {BmpStringGenerator.ofCharacterRange('А', 'Я').randomLength(1, 16)},
                {BmpStringGenerator.HEX.fixedLength(1)},
                {BmpStringGenerator.HEX.fixedLength(31)},
                {BmpStringGenerator.HEX.fixedLength(34)},

        };
    }

    @DataProvider
    public static Object[][] validAidValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(2)},
                {BmpStringGenerator.HEX.fixedLength(30)},
                {BmpStringGenerator.HEX.fixedLength(32)}
        };
    }

    @DataProvider
    public static Object[][] valueAndLengthProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(2), 1},
                {BmpStringGenerator.HEX.fixedLength(4), 2}
        };
    }

    @DataProvider
    public static Object[][] aidAndNdcStringProvider() {
        final String minCharCount = BmpStringGenerator.HEX.fixedLength(2);
        final String maxCharCount = BmpStringGenerator.HEX.fixedLength(32);
        return new Object[][]{
                {new Aid(minCharCount), "01" + minCharCount},
                {new Aid(maxCharCount), "10" + maxCharCount}
        };
    }

    @Test(dataProvider = "invalidAidValueProvider")
    public void should_throw_exception_on_invalid_aid_value(String aidValue) {
        Assertions.assertThatThrownBy(() -> new Aid(aidValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test(dataProvider = "validAidValueProvider")
    public void should_not_throw_exception_when_aid_value_is_valid(String aidValue) {
        Assertions.assertThatCode(() -> new Aid(aidValue))
                .doesNotThrowAnyException();
    }

    @Test(dataProvider = "valueAndLengthProvider")
    public void should_return_expected_length(String value, int expectedLength) {
        Assertions.assertThat(new Aid(value).getAidValueLength())
                .isEqualTo(expectedLength);
    }

    @Test(dataProvider = "aidAndNdcStringProvider")
    public void should_return_expected_ndc_string(Aid aid, String expectedNdcString) {
        Assertions.assertThat(aid.toNdcString())
                .isEqualTo(expectedNdcString);
    }
}
