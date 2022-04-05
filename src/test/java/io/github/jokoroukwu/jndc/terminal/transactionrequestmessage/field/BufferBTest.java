package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BufferBTest {

    @DataProvider
    public Object[][] invalidValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(1)},
                {BmpStringGenerator.HEX.fixedLength(2)},
                {BmpStringGenerator.HEX.fixedLength(33)},
                {Strings.EMPTY_STRING},
                {null}
        };
    }

    @Test(dataProvider = "invalidValueProvider")
    public void should_throw_expected_exception_on_invalid_buffer_value(String invalidBufferValue) {
        Assertions.assertThatThrownBy(() -> new BufferB(invalidBufferValue))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DataProvider
    public Object[][] validValueProvider() {
        return new Object[][]{
                {BmpStringGenerator.HEX.fixedLength(3)},
                {BmpStringGenerator.HEX.fixedLength(32)}
        };
    }

    @Test(dataProvider = "validValueProvider")
    public void should_not_throw_exception_on_valid_buffer_value(String validBufferValue) {
        Assertions.assertThatCode(() -> new BufferB(validBufferValue))
                .doesNotThrowAnyException();
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String expectedString = BmpStringGenerator.HEX.randomLength(3, 32);
        Assertions.assertThat(new BufferB(expectedString).toNdcString())
                .isEqualTo(expectedString);
    }

}
