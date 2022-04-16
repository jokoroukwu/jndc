package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BarcodeDataTest {


    @DataProvider
    public Object[][] expectedStringProvider() {
        final String scannedData = BmpStringGenerator.HEX.randomLength(100);
        return new Object[][]{
                {0, scannedData, "0000" + BarcodeData.RESERVED_FIELD + scannedData},
                {0xAB, scannedData, "00AB" + BarcodeData.RESERVED_FIELD + scannedData},
                {0xFFFF, scannedData, "FFFF" + BarcodeData.RESERVED_FIELD + scannedData},
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(int barcodeId, String scannedBarcodeData, String expectedString) {
        Assertions.assertThat(new BarcodeData(barcodeId, scannedBarcodeData).toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidBarcodeIdProvider() {
        return new Object[][]{
                {-1},
                {0x10000},
                {0x10001}
        };
    }

    @Test(dataProvider = "invalidBarcodeIdProvider")
    public void should_throw_expected_exception_on_invalid_barcode_id(int invalidBarcodeId) {
        Assertions.assertThatThrownBy(() -> new BarcodeData(invalidBarcodeId, BmpStringGenerator.HEX.fixedLength(5)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Barcode Format identifier");
    }

    @DataProvider
    public Object[][] validBarcodeIdProvider() {
        return new Object[][]{
                {0},
                {0xFFFF},
                {0xFFFE}
        };
    }

    @Test(dataProvider = "validBarcodeIdProvider")
    public void should_not_throw_exception_on_valid_barcode_id(int validBarcodeId) {
        Assertions.assertThatCode(() -> new BarcodeData(validBarcodeId, BmpStringGenerator.HEX.fixedLength(5)))
                .doesNotThrowAnyException();
    }

}
