package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BarcodeBufferTest {


    @DataProvider
    public Object[][] barcodeProvider() {
        final BarcodeData dummyBarcodeData = new BarcodeData(1, "AB");
        return new Object[][]{
                {null, String.valueOf(BarCodeBuffer.ID)},
                {dummyBarcodeData, BarCodeBuffer.ID + dummyBarcodeData.toNdcString()}
        };
    }

    @Test(dataProvider = "barcodeProvider")
    public void should_return_expected_ndc_string(BarcodeData barcodeData, String expectedString) {
        Assertions.assertThat(new BarCodeBuffer(barcodeData).toNdcString())
                .isEqualTo(expectedString);
    }
}
