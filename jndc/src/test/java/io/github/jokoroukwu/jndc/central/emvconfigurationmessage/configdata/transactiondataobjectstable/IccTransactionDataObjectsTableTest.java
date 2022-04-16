package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.tlv.TransactionType;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IccTransactionDataObjectsTableTest {
    private final TransactionTableEntry dummyEntry = new TransactionTableEntry(1,
            new ResponseFormat2(List.of(new TransactionType("AB"), new TransactionCategoryCode("AB"))));

    @DataProvider
    public Object[][] entriesProvider() {
        return new Object[][]{
                {1}, {2}, {IccTransactionDataObjectsTable.MAX_SIZE}
        };
    }


    @Test(dataProvider = "entriesProvider")
    public void should_return_expected_configuration_data_string(int numberOfEntries) {
        final Collection<TransactionTableEntry> entries = Collections.nCopies(numberOfEntries, dummyEntry);
        final IccTransactionDataObjectsTable table = new IccTransactionDataObjectsTable(entries);
        final String expectedString = EmvConfigMessageSubClass.TRANSACTION.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + Integers.toEvenLengthHexString(numberOfEntries)
                + dummyEntry.toNdcString().repeat(numberOfEntries);

        Assertions.assertThat(table.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidEntriesProvider() {
        return new Object[][]{
                {Collections.nCopies(IccTransactionDataObjectsTable.MAX_SIZE + 1, dummyEntry)},
                {null}
        };
    }

    @Test(dataProvider = "invalidEntriesProvider")
    public void should_throw_expected_exception_on_invalid_entries(Collection<TransactionTableEntry> entries) {
        Assertions.assertThatThrownBy(() -> new IccTransactionDataObjectsTable(entries))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
