package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IccLanguageSupportTableTest {
    private final LanguageSupportTableEntry dummyEntry = LanguageSupportTableEntry.builder()
            .withLanguageCode("ru")
            .withScreenBase(999)
            .withAudioBase(7)
            .withOpcodeBufferPositions(123)
            .withOpCodeBufferValues("AB@")
            .build();

    @DataProvider
    public Object[][] configDataExpectedStringProvider() {
        final String withSingleEntry = EmvConfigMessageSubClass.LANGUAGE_SUPPORT.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + "01"
                + dummyEntry.toNdcString();
        final String withMultiEntries = EmvConfigMessageSubClass.LANGUAGE_SUPPORT.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + "02"
                + dummyEntry.toNdcString().repeat(2);
        return new Object[][]{
                {List.of(dummyEntry), withSingleEntry},
                {List.of(dummyEntry, dummyEntry), withMultiEntries}
        };
    }

    @Test(dataProvider = "configDataExpectedStringProvider")
    public void should_return_expected_configuration_data_string(Collection<LanguageSupportTableEntry> entries, String expectedString) {
        final IccLanguageSupportTable table = new IccLanguageSupportTable(entries);


        Assertions.assertThat(table.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidNumberOfEntriesProvider() {
        return new Object[][]{
                {Collections.nCopies(IccLanguageSupportTable.MAX_SIZE + 1, dummyEntry)}
        };
    }

    @Test(dataProvider = "invalidNumberOfEntriesProvider")
    public void should_throw_expected_exception_on_invalid_number_of_entries(Collection<LanguageSupportTableEntry> entries) {
        Assertions.assertThatThrownBy(() -> new IccLanguageSupportTable(entries))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
