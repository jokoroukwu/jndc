package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.tlv.TransactionType;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class TransactionTableEntryTest {
    private final ResponseFormat2 dummyResponseFormat2 = new ResponseFormat2(List.of(new TransactionType("AB"),
            new TransactionCategoryCode("AB")));

    @DataProvider
    public static Object[][] validEntryTypeProvider() {
        return new Object[][]{
                {1},
                {2},
                {IccTransactionDataObjectsTable.MAX_SIZE}
        };
    }

    @DataProvider
    public static Object[][] invalidDataObjectsProvider() {
        return new Object[][]{
                {new ResponseFormat2(List.of(new TransactionCategoryCode("AB"))), "9C"},
                {new ResponseFormat2(List.of(new TransactionType("AB"))), "9F53"},
                {null, "null"}
        };
    }

    @DataProvider
    public Object[][] invalidEntryTypeProvider() {
        return new Object[][]{
                {-1},
                {0},
                {IccTransactionDataObjectsTable.MAX_SIZE + 1}
        };
    }

    @Test(dataProvider = "invalidEntryTypeProvider")
    public void should_throw_expected_exception_on_invalid_transaction_type(int invalidEntryType) {
        Assertions.assertThatThrownBy(() -> new TransactionTableEntry(invalidEntryType, dummyResponseFormat2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Entry Type");
    }

    @Test(dataProvider = "validEntryTypeProvider")
    public void should_not_throw_expected_exception_on_valid_transaction_type(int validEntryType) {
        Assertions.assertThatCode(() -> new TransactionTableEntry(validEntryType, dummyResponseFormat2))
                .doesNotThrowAnyException();
    }

    @Test(dataProvider = "invalidDataObjectsProvider")
    public void should_not_throw_expected_exception_on_invalid_data_objects(ResponseFormat2 invalidDataObjects, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new TransactionTableEntry(1, invalidDataObjects))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void should_return_expected_ndc_string() {
        Assertions.assertThat(new TransactionTableEntry(1, dummyResponseFormat2).toNdcString())
                .isEqualTo("01" + dummyResponseFormat2.toNdcString());
    }

}
