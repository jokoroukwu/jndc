package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.track1data;

import io.github.jokoroukwu.jndc.trackdata.Track1DataBuffer;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Track1DataBufferTest {
    private final IStringGenerator validRange = BmpStringGenerator.ofCharacterRange(0x20, 0x5F);


    @DataProvider
    public Object[][] invalidValueProvider() {
        final IStringGenerator invalidRange = BmpStringGenerator.ofCharacterRanges(0, 0x1F, 0x60, 0x7F);
        return new Object[][]{
                {invalidRange.randomLength(1, 78)},
                {validRange.fixedLength(79)},
                {validRange.fixedLength(80)},
                {Strings.EMPTY_STRING},
                {null}
        };
    }

    @Test(dataProvider = "invalidValueProvider")
    public void should_throw_expected_exception_on_invalid_track1_value(String invalidValue) {
        Assertions.assertThatThrownBy(() -> Track1DataBuffer.requestBuffer(invalidValue))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DataProvider
    public Object[][] validValueProvider() {
        return new Object[][]{
                {validRange.fixedLength(1)},
                {validRange.fixedLength(78)},
        };
    }

    @Test(dataProvider = "validValueProvider")
    public void should_not_throw_exception_on_valid_track1_value(String validValue) {
        Assertions.assertThatCode(() -> Track1DataBuffer.requestBuffer(validValue))
                .doesNotThrowAnyException();

    }

    @Test
    public void should_return_expected_ndc_string() {
        final String randomValue = validRange.randomLength(1, 78);
        Assertions.assertThat(Track1DataBuffer.requestBuffer(randomValue).toNdcString())
                .isEqualTo(Track1DataBuffer.TRANSACTION_REQUEST_ID + randomValue);
    }
}



