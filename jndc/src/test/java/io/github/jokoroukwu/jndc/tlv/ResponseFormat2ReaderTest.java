package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class ResponseFormat2ReaderTest {
    private final HexStringBerTlv dummyTlvOne = new HexStringBerTlv(0x55, 2, BmpStringGenerator.HEX.fixedLength(4));
    private final HexStringBerTlv dummyTlvTwo = new HexStringBerTlv(0x05, 2, BmpStringGenerator.HEX.fixedLength(4));

    @DataProvider
    public static Object[][] invalidBufferDataProvider() {
        return new Object[][]{
                {"05", String.format("expected '%s'", ResponseFormat2.NAME)},
                {"77", "length"},
                {"7702", "value"}
        };
    }

    @DataProvider
    public Object[][] dataProvider() {
        final LinkedHashMap<Integer, BerTlv<String>> mapOne = new LinkedHashMap<>(1, 1);
        mapOne.put(dummyTlvOne.getTag(), dummyTlvOne);
        final LinkedHashMap<Integer, BerTlv<String>> mapTwo = new LinkedHashMap<>(2, 2);
        mapTwo.put(dummyTlvOne.getTag(), dummyTlvOne);
        mapTwo.put(dummyTlvTwo.getTag(), dummyTlvTwo);
        return new Object[][]{
                {List.of(new FakeReader(dummyTlvOne, 10)), "77050000000000",
                        new ResponseFormat2(5, mapOne)},

                {List.of(new FakeReader(dummyTlvOne, 4), new FakeReader(dummyTlvTwo)), "77020000",
                        new ResponseFormat2(2, mapTwo)}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void should_return_expected_response_format2(List<NdcComponentReader<DescriptiveOptional<BerTlv<String>>>> readers,
                                                        String bufferString,
                                                        ResponseFormat2 expectedValue) {
        final ResponseFormat2Reader reader = new ResponseFormat2Reader(readers);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferString);
        final DescriptiveOptional<ResponseFormat2> responseFormat2Optional = reader.readComponent(buffer);
        Assertions.assertThat(responseFormat2Optional.isPresent())
                .withFailMessage("response format 2 should be present but got: %s",
                        responseFormat2Optional.description())
                .isTrue();
        Assertions.assertThat(responseFormat2Optional.get())
                .isEqualTo(expectedValue);
    }

    @Test
    public void should_return_empty_result_when_data_remains() {
        final ResponseFormat2Reader reader = new ResponseFormat2Reader(List.of(new FakeReader(dummyTlvOne)));
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("77020000");
        final DescriptiveOptional<?> optional = reader.readComponent(buffer);
        Assertions.assertThat(optional.isEmpty())
                .withFailMessage("should have returned empty optional but was: %s", optional)
                .isTrue();
        Assertions.assertThat(optional.description())
                .contains("unexpected data");
    }

    @Test(dataProvider = "invalidBufferDataProvider")
    public void should_return_empty_result_on_invalid_response_format_buffer_data(String bufferData, String expectedMessage) {
        final ResponseFormat2Reader reader = new ResponseFormat2Reader(List.of(new FakeReader()));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        final DescriptiveOptional<?> optional = reader.readComponent(buffer);

        Assertions.assertThat(optional.isEmpty())
                .withFailMessage("should have returned empty optional but was: %s", optional)
                .isTrue();
        Assertions.assertThat(optional.description())
                .contains(expectedMessage);
    }

    @Test
    public void should_return_empty_result_on_nested_reader_failure() {
        final FakeReader fakeReader = new FakeReader();
        final ResponseFormat2Reader reader = new ResponseFormat2Reader(List.of(fakeReader));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap("7701000");
        final DescriptiveOptional<?> optional = reader.readComponent(buffer);

        Assertions.assertThat(optional.isEmpty())
                .withFailMessage("should have returned empty optional but was: %s", optional)
                .isTrue();
        Assertions.assertThat(optional.description())
                .contains(fakeReader.message);
    }


    private static final class FakeReader implements NdcComponentReader<DescriptiveOptional<BerTlv<String>>> {
        private final BerTlv<String> returnValue;
        private final int skipCharCount;
        private final String message = BmpStringGenerator.ALPHANUMERIC.fixedLength(10);

        private FakeReader(BerTlv<String> returnValue, int skipCharCount) {
            this.returnValue = returnValue;
            this.skipCharCount = skipCharCount;
        }

        private FakeReader(BerTlv<String> returnValue) {
            this(returnValue, 0);

        }

        private FakeReader() {
            this(null);
        }

        @Override
        public DescriptiveOptional<BerTlv<String>> readComponent(NdcCharBuffer ndcCharBuffer) {
            if (skipCharCount > 0) {
                ndcCharBuffer.skip(skipCharCount);
            }
            return DescriptiveOptional.ofNullable(returnValue, () -> message);
        }
    }
}

