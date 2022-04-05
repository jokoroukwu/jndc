package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyCode;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyExponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.random.ThreadLocalSecureRandom;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class CurrencyTableEntryTest {
    private final ResponseFormat2 validResponseFormat2 = new ResponseFormat2(Arrays.asList(new TransactionCurrencyExponent("AB"),
            new TransactionCurrencyCode("ABCD")));
    private final ResponseFormat2 responseFormat2Mock = mock(ResponseFormat2.class);

    @Test
    private void should_return_expected_ndc_string() {
        final int entryType = ThreadLocalSecureRandom.get().nextInt(0x100);
        Assertions.assertThat(new CurrencyTableEntry(entryType, validResponseFormat2).toNdcString())
                .isEqualTo(Integers.toEvenLengthHexString(entryType) + validResponseFormat2.toNdcString());
    }

    @DataProvider
    public Object[][] ResponseFormat2Provider() {
        return new Object[][]{
                {new ResponseFormat2(List.of(new TransactionCurrencyCode("ABCD"))), "5F36"},
                {new ResponseFormat2(List.of(new TransactionCurrencyExponent("AB"))), "5F2A"}

        };
    }

    @Test(dataProvider = "ResponseFormat2Provider")
    private void should_throw_expected_exception_on_no_mandatory_tag(ResponseFormat2 responseFormat2, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new CurrencyTableEntry(0xFF, responseFormat2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @DataProvider
    public Object[][] invalidCurrencyTypeProvider() {
        return new Object[][]{
                {-1},
                {0},
                {0x100}
        };
    }

    @Test(dataProvider = "invalidCurrencyTypeProvider")
    private void should_throw_expected_exception_on_invalid_currency_type(int invalidCurrencyType) {
        Assertions.assertThatThrownBy(() -> new CurrencyTableEntry(invalidCurrencyType, validResponseFormat2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Entry Type");
    }
}
