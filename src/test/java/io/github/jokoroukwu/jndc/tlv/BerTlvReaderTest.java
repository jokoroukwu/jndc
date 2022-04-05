package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptionalInt;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BerTlvReaderTest {


    @DataProvider
    public Object[][] tagDataProvider() {
        return new Object[][]{
                {"5F8FFF57FF", 0x5F8FFF57},
                {"FF7FFF", 0xFF7F},
                {"B5FF", 0xB5},
                {"A1FF", 0xA1},
        };
    }

    @Test(dataProvider = "tagDataProvider")
    public void should_return_expected_tag(String tagString, int expectedTag) {
        final DescriptiveOptionalInt optionalTag = BerTlvReaderBase.INSTANCE.tryReadTag(NdcCharBuffer.wrap(tagString));
        Assertions.assertThat(optionalTag.isPresent())
                .as("tag should be present")
                .isTrue();

        Assertions.assertThat(optionalTag.get())
                .isEqualTo(expectedTag);
    }

    @Test(dataProvider = "tagDataProvider")
    public void should_not_read_data_following_tag(String tagString, int expectedValue) {
        final NdcCharBuffer charBuffer = NdcCharBuffer.wrap(tagString);
        BerTlvReaderBase.INSTANCE.tryReadTag(charBuffer);
        Assertions.assertThat(charBuffer.remaining())
                .as("should have 2 characters remaining")
                .isEqualTo(2);
    }

    @DataProvider
    public Object[][] invalidTagProvider() {
        return new Object[][]{
                {"5F8FFFFF"},
        };
    }

    @Test(dataProvider = "invalidTagProvider")
    public void should_return_error_on_tag_overflow(String invalidTag) {
        final NdcCharBuffer charBuffer = NdcCharBuffer.wrap(invalidTag);
        final DescriptiveOptionalInt descriptiveOptionalInt = BerTlvReaderBase.INSTANCE.tryReadTag(charBuffer);
        Assertions.assertThat(descriptiveOptionalInt.isEmpty())
                .as("result should be empty")
                .isTrue();
        Assertions.assertThat(descriptiveOptionalInt.description())
                .contains("overflow");
    }

    @DataProvider
    public Object[][] lengthDataProvider() {
        return new Object[][]{
                {"3FFF", 0x3F},
                {"7FFF", 0x7F},
                {"82FFFFFF", 0xFFFF},
                {"847FFFFFFFFF", Integer.MAX_VALUE}
        };
    }

    @Test(dataProvider = "lengthDataProvider")
    public void should_return_expected_length(String lengthString, int expectedLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(lengthString);
        final DescriptiveOptionalInt descriptiveOptionalInt = BerTlvReaderBase.INSTANCE.tryReadLength(buffer);
        Assertions.assertThat(descriptiveOptionalInt.isPresent())
                .as("length should be present")
                .isTrue();
        Assertions.assertThat(descriptiveOptionalInt.get())
                .isEqualTo(expectedLength);
    }

    @DataProvider
    public Object[][] overflowedLengthProvider() {
        return new Object[][]{
                {"84FFFFFFFF"},
                {"850BFFFFFFFF"}
        };
    }

    @Test(dataProvider = "overflowedLengthProvider")
    public void should_return_error_on_length_overflow(String invalidLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidLength);
        final DescriptiveOptionalInt descriptiveOptionalInt = BerTlvReaderBase.INSTANCE.tryReadLength(buffer);
        Assertions.assertThat(descriptiveOptionalInt.isEmpty())
                .as("result should be empty")
                .isTrue();
        Assertions.assertThat(descriptiveOptionalInt.description())
                .contains("overflow");
    }

    @Test(dataProvider = "lengthDataProvider")
    public void should_not_read_data_following_length(String lengthString, int expectedLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(lengthString);
        BerTlvReaderBase.INSTANCE.tryReadLength(buffer);
        Assertions.assertThat(buffer.remaining())
                .isEqualTo(2);

    }
}
