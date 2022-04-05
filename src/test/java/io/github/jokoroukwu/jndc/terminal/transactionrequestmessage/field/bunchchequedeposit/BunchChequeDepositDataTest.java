package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BunchChequeDepositDataTest {
    public final CurrencyData dummyCurrencyData = CurrencyData.builder()
            .withDepositCurrency("RUR")
            .withPositiveExponentSign()
            .withAmountExponentValue(1)
            .withDepositedCheques(List.of(new DepositedCheque(1, 0, Strings.EMPTY_STRING)))
            .build();


    @DataProvider
    public Object[][] invalidChequesToReturnProvider() {
        return new Object[][]{
                {-1},
                {1000}
        };
    }

    @Test(dataProvider = "invalidChequesToReturnProvider")
    public void should_throw_expected_exception_on_invalid_cheques_to_return_value(int invalidChequesToReturn) {
        final Collection<CurrencyData> emptyData = Collections.emptyList();
        Assertions.assertThatThrownBy(() -> new BunchChequeDepositData(invalidChequesToReturn, emptyData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Cheques to Return");
    }

    @DataProvider
    public Object[][] validChequesToReturnProvider() {
        return new Object[][]{
                {0},
                {999}
        };
    }

    @Test(dataProvider = "validChequesToReturnProvider")
    public void should_not_throw_exception_on_valid_cheques_to_return_value(int invalidChequesToReturn) {
        Assertions.assertThatCode(() -> new BunchChequeDepositData(invalidChequesToReturn, Collections.emptyList()))
                .doesNotThrowAnyException();
    }

    @DataProvider
    public Object[][] expectedStringProvider() {
        final String depositDataString = dummyCurrencyData.toNdcString();
        return new Object[][]{
                {new BunchChequeDepositData(1, Collections.emptyList()), "001"},
                {new BunchChequeDepositData(99, Collections.emptyList()), "099"},
                {new BunchChequeDepositData(999, Collections.emptyList()), "999"},
                {new BunchChequeDepositData(1, List.of(dummyCurrencyData, dummyCurrencyData)),
                        "001" + "0000" + depositDataString + NdcConstants.GROUP_SEPARATOR + depositDataString},
                {new BunchChequeDepositData(1, List.of(dummyCurrencyData)),
                        "001" + "0000" + depositDataString}
        };
    }

    @Test(dataProvider = "expectedStringProvider")
    public void should_return_expected_ndc_string(BunchChequeDepositData bunchChequeDepositData, String expectedString) {
        Assertions.assertThat(bunchChequeDepositData.toNdcString())
                .isEqualTo(expectedString);
    }
}
