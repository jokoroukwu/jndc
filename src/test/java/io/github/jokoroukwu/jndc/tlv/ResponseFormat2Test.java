package io.github.jokoroukwu.jndc.tlv;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

public class ResponseFormat2Test {
    private final IStringGenerator hex = BmpStringGenerator.HEX;


    @DataProvider
    public Object[][] tlvLengthProvider() {
        final HexStringBerTlv tlvOne = new HexStringBerTlv(0xFF, 1, hex.fixedLength(2));
        final HexStringBerTlv tlvTwo = new HexStringBerTlv(0xFFAD, 2, hex.fixedLength(4));
        final HexStringBerTlv tlvThree = new HexStringBerTlv(0x09CDEFAA, 127, hex.fixedLength(127 * 2));
        final HexStringBerTlv tlvTFour = new HexStringBerTlv(0x13CDEFAA, 1024, hex.fixedLength(1024 * 2));

        return new Object[][]{
                {List.of(tlvOne), 3},
                {List.of(tlvTwo), 5},
                {List.of(tlvThree), 4 + 1 + 127},
                {List.of(tlvTFour), 4 + 3 + 1024},
                {List.of(tlvTFour, tlvThree), 4 + 3 + 1024 + 4 + 1 + 127},
        };
    }

    @Test(dataProvider = "tlvLengthProvider")
    public void should_return_expected_length(Collection<BerTlv<String>> tlvs, int expectedLength) {
        final ResponseFormat2 responseFormat2 = new ResponseFormat2(tlvs);
        Assertions.assertThat(responseFormat2.getLength())
                .isEqualTo(expectedLength);
    }

    @DataProvider
    public Object[][] tlvNdcStringProvider() {
        final HexStringBerTlv tlvOne = new HexStringBerTlv(0xFFAD, 2, hex.fixedLength(4));
        final HexStringBerTlv tlvTwo = new HexStringBerTlv(0x09CDEFAA, 127, hex.fixedLength(127 * 2));
        return new Object[][]{
                {List.of(tlvOne), "7705FFAD02" + tlvOne.getValue()},
                {List.of(tlvOne, tlvTwo), "778189FFAD02" + tlvOne.getValue() + "09CDEFAA7F" + tlvTwo.getValue()}
        };
    }

    @Test(dataProvider = "tlvNdcStringProvider")
    public void should_return_expected_ndc_string(List<BerTlv<String>> tlvs, String expectedString) {
        Assertions.assertThat(new ResponseFormat2(tlvs).toNdcString())
                .isEqualTo(expectedString);
    }
}
