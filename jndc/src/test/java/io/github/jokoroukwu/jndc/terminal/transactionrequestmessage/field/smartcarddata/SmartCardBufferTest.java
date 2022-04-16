package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.smartcarddata;

import io.github.jokoroukwu.jndc.tlv.HexStringBerTlv;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class SmartCardBufferTest {

    @DataProvider
    public Object[][] dataProvider() {
        final SmartCardData dummySmartCardData =
                new SmartCardData(0xFFFF, List.of(new HexStringBerTlv(0xFF, BmpStringGenerator.HEX.fixedLength(4))));
        return new Object[][]{
                {dummySmartCardData, SmartCardBuffer.ID + dummySmartCardData.toNdcString()},
                {null, String.valueOf(SmartCardBuffer.ID)}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void should_return_expected_ndc_string(SmartCardData smartCardData, String expectedString) {
        Assertions.assertThat(new SmartCardBuffer(smartCardData).toNdcString())
                .isEqualTo(expectedString);
    }
}
