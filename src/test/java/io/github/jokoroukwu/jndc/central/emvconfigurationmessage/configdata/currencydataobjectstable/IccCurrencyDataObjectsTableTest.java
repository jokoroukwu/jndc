package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyCode;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyExponent;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class IccCurrencyDataObjectsTableTest {
    private final CurrencyTableEntry dummyEntry = new CurrencyTableEntry(1,
            new ResponseFormat2(Arrays.asList(new TransactionCurrencyCode("ABCD"), new TransactionCurrencyExponent("AB"))));


    @Test
    public void should_return_expected_configuration_data_string() {
        final IccCurrencyDataObjectsTable table = IccCurrencyDataObjectsTable.of(dummyEntry, dummyEntry);
        final String expectedString = EmvConfigMessageSubClass.CURRENCY.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + "02"
                + dummyEntry.toNdcString().repeat(2);
        Assertions.assertThat(table.toNdcString())
                .isEqualTo(expectedString);

    }

    @DataProvider
    public Object[][] entriesProvider() {
        return new Object[][]{
                {Collections.nCopies(0x100, dummyEntry)}
        };
    }

    @Test(dataProvider = "entriesProvider")
    public void should_throw_expected_exception_on_invalid_number_of_entries(Collection<CurrencyTableEntry> entries) {
        Assertions.assertThatThrownBy(() -> new IccCurrencyDataObjectsTable(entries))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
