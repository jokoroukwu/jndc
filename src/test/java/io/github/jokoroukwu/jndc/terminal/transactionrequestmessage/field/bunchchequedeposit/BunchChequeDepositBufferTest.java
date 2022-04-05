package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BunchChequeDepositBufferTest {

    @DataProvider
    public Object[][] bufferProvider() {
        final BunchChequeDepositData dummyBunchDepositData = new BunchChequeDepositData(1);
        return new Object[][]{
                {BunchChequeDepositBuffer.EMPTY, Character.toString(BunchChequeDepositBuffer.ID)},
                {new BunchChequeDepositBuffer(dummyBunchDepositData), BunchChequeDepositBuffer.ID + dummyBunchDepositData.toNdcString()},
        };
    }

    @Test(dataProvider = "bufferProvider")
    public void should_return_expected_ndc_string(BunchChequeDepositBuffer buffer, String expectedString) {
        Assertions.assertThat(buffer.toNdcString())
                .isEqualTo(expectedString);
    }
}
