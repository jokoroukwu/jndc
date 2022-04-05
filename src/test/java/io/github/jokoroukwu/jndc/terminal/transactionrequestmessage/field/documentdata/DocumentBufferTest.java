package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DocumentBufferTest {


    @DataProvider
    public Object[][] dataProvider() {
        final SingleChequeDepositData dummyChequeDepositData =
                new SingleChequeDepositData(BmpStringGenerator.HEX.randomLength(256));

        return new Object[][]{
                {dummyChequeDepositData, DocumentBuffer.ID + dummyChequeDepositData.toNdcString()},
                {null, String.valueOf(DocumentBuffer.ID)}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void should_return_expected_ndc_string(SingleChequeDepositData singleChequeDepositData, String expectedString) {
        Assertions.assertThat(new DocumentBuffer(singleChequeDepositData).toNdcString())
                .isEqualTo(expectedString);
    }
}
