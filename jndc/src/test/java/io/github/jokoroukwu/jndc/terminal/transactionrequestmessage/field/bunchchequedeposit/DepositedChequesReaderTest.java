package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;

public class DepositedChequesReaderTest {
    private DepositedChequesReader reader;

    @BeforeMethod
    public void setUp() {
        reader = new DepositedChequesReader();
    }

    @DataProvider
    public Object[][] validDataProvider() {
        var twelveDigits = BmpStringGenerator.DECIMAL.fixedLength(12);
        var twelveDigitsNumber = Long.parseLong(twelveDigits);
        return new Object[][]{
                {"001" + twelveDigits + twelveDigits + "000" + NdcConstants.GROUP_SEPARATOR,
                        List.of(new DepositedCheque(1, twelveDigitsNumber, twelveDigitsNumber, EMPTY_STRING))},

                {"001" + twelveDigits + twelveDigits + "001" + "A" + NdcConstants.GROUP_SEPARATOR,
                        List.of(new DepositedCheque(1, twelveDigitsNumber, twelveDigitsNumber, "A"))},

                {"999" + twelveDigits + twelveDigits + "999" + "A".repeat(999) + NdcConstants.GROUP_SEPARATOR,
                        List.of(new DepositedCheque(999, twelveDigitsNumber, twelveDigitsNumber, "A".repeat(999)))},

                {"001" + twelveDigits + twelveDigits + "999" + "A".repeat(999) + NdcConstants.GROUP_SEPARATOR,
                        List.of(new DepositedCheque(1, twelveDigitsNumber, twelveDigitsNumber, "A".repeat(999)))},

                {("001" + twelveDigits + twelveDigits + "001" + "A" + NdcConstants.GROUP_SEPARATOR).repeat(2),
                        Collections.nCopies(2, new DepositedCheque(1, twelveDigitsNumber, twelveDigitsNumber, "A"))}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String bufferData, List<DepositedCheque> expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        final List<DepositedCheque> actualResult = reader.readComponent(buffer);

        Assertions.assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final String bufferData = "001" + "000000000000".repeat(2) + "000" + NdcConstants.GROUP_SEPARATOR_STRING.repeat(2) + remainingData;
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);

        reader.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length() + 1);
    }

    @DataProvider
    public Object[][] invalidDataProvider() {
        return new Object[][]{
                {"001" + "000000000000".repeat(2) + "000", "missing trailing group separator"},
                {"000", "Cheque Identifier"},
                {"001" + "0", "Customer Cheque Amount"},
                {"001" + "000000000000" + "0", "Derived Cheque Amount"},
                {"001" + "000000000000".repeat(2) + "0", "Codeline Length"}
        };
    }

    @Test(dataProvider = "invalidDataProvider")
    public void should_throw_expected_exception_on_invalid_data(String invalidData, String messagePart) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidData);

        Assertions.assertThatThrownBy(() -> reader.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContainingAll("Bunch Cheque Deposit Buffer", messagePart);
    }
}
