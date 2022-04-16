package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PrinterDataListTest {
    private final PrinterData dummyPrinterData = new PrinterData(PrinterFlag.AUDIO_DATA, "AB");

    @DataProvider
    public Object[][] validDataProvider() {
        final String printerDataString = dummyPrinterData.toNdcString();
        return new Object[][]{
                {PrinterDataList.of(dummyPrinterData), printerDataString},
                {PrinterDataList.of(dummyPrinterData, dummyPrinterData), printerDataString + NdcConstants.GROUP_SEPARATOR + printerDataString}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(PrinterDataList printerDataList, String expectedString) {
        Assertions.assertThat(printerDataList.toNdcString())
                .isEqualTo(expectedString);
    }


    @Test
    public void should_throw_expected_exception_when_max_size_exceeded() {
        final PrinterDataList printerDataList = new PrinterDataList();
        for (int i = 0; i < PrinterDataList.MAX_SIZE; i++) {
            printerDataList.add(dummyPrinterData);
        }

        Assertions.assertThatThrownBy(() -> printerDataList.add(dummyPrinterData))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
