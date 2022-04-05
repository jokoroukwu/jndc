package io.github.jokoroukwu.jndc.trackdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TrackDataReaderTest {
    private final int maxLength = 10;
    private final int minRange = '0';
    private final int maxRange = '9';

    @DataProvider
    public Object[][] validDataProvider() {
        final IStringGenerator digits = BmpStringGenerator.ofCharacterRange(minRange, maxRange);
        final String maxData = digits.fixedLength(maxLength);
        final String subMaxData = digits.fixedLength(9);
        return new Object[][]{
                {maxData + BmpStringGenerator.HEX.fixedLength(3), maxData},
                {subMaxData + NdcConstants.FIELD_SEPARATOR, subMaxData},
                {subMaxData + NdcConstants.GROUP_SEPARATOR, subMaxData}
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String validData, String expectedData) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        final DescriptiveOptional<String> result = new TrackDataReader(maxLength, minRange, maxRange).readComponent(buffer);
        Assertions.assertThat(result.isPresent())
                .as("returned data should be present")
                .isTrue();

        Assertions.assertThat(result.get())
                .isEqualTo(expectedData);
    }

    @Test
    public void should_throw_expected_message_on_invalid_data() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("1239g");
        final DescriptiveOptional<String> result = new TrackDataReader(maxLength, minRange, maxRange).readComponent(buffer);
        Assertions.assertThat(result.isEmpty())
                .as("should be empty on invalid data")
                .isTrue();
        Assertions.assertThat(result.description())
                .contains("is not within valid character range");
    }
}
