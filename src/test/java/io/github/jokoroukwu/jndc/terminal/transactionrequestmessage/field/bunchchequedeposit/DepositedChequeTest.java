package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DepositedChequeTest {
    private final String dummyCodeLineData = BmpStringGenerator.HEX.fixedLength(5);

    @DataProvider
    public Object[][] invalidChequeIdProvider() {
        return new Object[][]{
                {-1},
                {0},
                {1000}
        };
    }

    @Test(dataProvider = "invalidChequeIdProvider")
    public void should_throw_expected_exception_on_invalid_cheque_id(int invalidChequeId) {
        Assertions.assertThatThrownBy(() -> new DepositedCheque(invalidChequeId, 0L, dummyCodeLineData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cheque Identifier");
    }


    @DataProvider
    public Object[][] invalidCustomerChequeAmountProvider() {
        return new Object[][]{
                {-1L},
                {1_000_000_000_000L}
        };
    }

    @Test(dataProvider = "invalidCustomerChequeAmountProvider")
    public void should_throw_expected_exception_on_invalid_customer_cheque_amount(long invalidCustomerChequeAmount) {
        Assertions.assertThatThrownBy(()
                -> new DepositedCheque(1, invalidCustomerChequeAmount, dummyCodeLineData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer Cheque Amount");
    }

    @DataProvider
    public Object[][] invalidCodeLineDataProvider() {
        return new Object[][]{
                {null, "Codeline Data"},
                {BmpStringGenerator.HEX.fixedLength(1000), "Codeline Length"}
        };
    }

    @Test(dataProvider = "invalidCodeLineDataProvider")
    public void should_throw_expected_exception_on_invalid_codeline_data(String invalidCodeLineData, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new DepositedCheque(1, 1L, invalidCodeLineData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @DataProvider
    public Object[][] expectedStringProvider() {
        final String codeLineDataLength = Integers.toZeroPaddedString(dummyCodeLineData.length(), 3);
        final String derivedChequeAmount = "000000000000";
        return new Object[][]{
                {new DepositedCheque(1, 1L, dummyCodeLineData),
                        "001" + "000000000001" + derivedChequeAmount + codeLineDataLength + dummyCodeLineData},
                {new DepositedCheque(99, 99_999_999_999L, dummyCodeLineData),
                        "099" + "099999999999" + derivedChequeAmount + codeLineDataLength + dummyCodeLineData},
                {new DepositedCheque(999, 999_999_999_999L, dummyCodeLineData),
                        "999" + "999999999999" + derivedChequeAmount + codeLineDataLength + dummyCodeLineData},
                {new DepositedCheque(999, 999_999_999_999L, Strings.EMPTY_STRING),
                        "999" + "999999999999" + derivedChequeAmount + "000" + Strings.EMPTY_STRING},
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(DepositedCheque depositedCheque, String expectedString) {
        Assertions.assertThat(depositedCheque.toNdcString())
                .isEqualTo(expectedString);
    }
}
