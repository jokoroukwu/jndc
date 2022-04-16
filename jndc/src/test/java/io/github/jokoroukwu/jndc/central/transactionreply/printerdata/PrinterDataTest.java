package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class PrinterDataTest {

    @DataProvider
    public static Object[][] invalidConstructorArgProvider() {
        return new Object[][]{
                {null, "123", "Printer Flag"},
                {PrinterFlag.AUDIO_DATA, null, "Data"}
        };
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String dummyData = BmpStringGenerator.HEX.fixedLength(10);
        return new Object[][]{
                {new PrinterData(PrinterFlag.AUDIO_DATA, dummyData), PrinterFlag.AUDIO_DATA.getValue() + dummyData}
        };
    }

    @Test
    public void should_return_expected_ndc_string() {
        final String dummyData = BmpStringGenerator.HEX.fixedLength(10);
        Assertions.assertThat(new PrinterData(PrinterFlag.AUDIO_DATA, dummyData).toNdcString())
                .isEqualTo(PrinterFlag.AUDIO_DATA.getValue() + dummyData);
    }

    @Test(dataProvider = "invalidConstructorArgProvider")
    public void should_throw_expected_exception_on_invalid_constructor_args(PrinterFlag printerFlag, String data, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new PrinterData(printerFlag, data))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }
}
