package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class ChequeReaderTest {
    private final ChequeReader reader = new ChequeReader();

    @DataProvider
    public Object[][] validDataProvider() {
        final String chequeText = BmpStringGenerator.HEX.fixedLength(10);
        return new Object[][]{
                {"0001500000" + chequeText, List.of(new Cheque(0, 15, ChequeStamp.NO_STAMP, chequeText))},

                {"0001500000", List.of(new Cheque(0, 15, ChequeStamp.NO_STAMP, Strings.EMPTY_STRING))},

                {"0001500000" + NdcConstants.GROUP_SEPARATOR + "0001500000" + chequeText,
                        List.of(new Cheque(0, 15, ChequeStamp.NO_STAMP, Strings.EMPTY_STRING),
                                new Cheque(0, 15, ChequeStamp.NO_STAMP, chequeText))}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_cheques(String data, List<Cheque> expectedCheques) {
        Assertions.assertThat(reader.readComponent(NdcCharBuffer.wrap(data)))
                .isEqualTo(expectedCheques);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {"00", "Cheque Identifier"},
                {"0001500000" + NdcConstants.GROUP_SEPARATOR + "00", "Cheque Identifier"},
                {"0001600000", "Cheque Destination"},
                {"000152", "Cheque Stamp"},
                {"000150000", "Reserved"},
                {"000150", "Reserved"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String invalidData, String expectedMessagePart) {
        Assertions.assertThatThrownBy(() -> reader.readComponent(NdcCharBuffer.wrap(invalidData)))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(expectedMessagePart);
    }
}
