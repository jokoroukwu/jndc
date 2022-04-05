package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.TerminalCountryCode;
import io.github.jokoroukwu.jndc.tlv.TerminalType;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class IccTerminalDataObjectsTableTest {

    @DataProvider
    public Object[][] invalidDataObjectProvider() {
        return new Object[][]{
                {new ResponseFormat2(List.of(new TerminalType("AB"))), "9F1A"},
                {new ResponseFormat2(List.of(new TerminalCountryCode("ABCD"))), "9F35"}
        };
    }

    @Test(dataProvider = "invalidDataObjectProvider")
    public void should_throw_expected_exception_when_mandatory_tag_missing(ResponseFormat2 configurationData, String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new IccTerminalDataObjectsTable(configurationData))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void should_return_expected_configuration_data_string() {
        final ResponseFormat2 configurationDataDummy = new ResponseFormat2(List.of(new TerminalType("AB"),
                new TerminalCountryCode("ABCD")));

        final String expectedString = EmvConfigMessageSubClass.TERMINAL_DATA_OBJECTS.toNdcString()
                + NdcConstants.FIELD_SEPARATOR_STRING
                + configurationDataDummy.toNdcString();

        final IccTerminalDataObjectsTable table = new IccTerminalDataObjectsTable(configurationDataDummy);
        Assertions.assertThat(table.toNdcString())
                .isEqualTo(expectedString);
    }
}
