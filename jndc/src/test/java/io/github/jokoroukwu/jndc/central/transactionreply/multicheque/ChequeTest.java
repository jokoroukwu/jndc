package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ChequeTest {
    private final String text = BmpStringGenerator.HEX.fixedLength(10);

    @DataProvider
    public Object[][] chequeProvider() {
        return new Object[][]{
                {new Cheque(0, 0, ChequeStamp.STAMP, text), "000" + "00" + '1' + "0000" + text},
                {new Cheque(999, 15, ChequeStamp.NO_STAMP, text), "999" + "15" + '0' + "0000" + text}
        };
    }

    @Test(dataProvider = "chequeProvider")
    public void should_return_expected_ndc_string(Cheque cheque, String expectedString) {
        Assertions.assertThat(cheque.toNdcString())
                .isEqualTo(expectedString);
    }

    @DataProvider
    public Object[][] invalidArgsProvider() {
        return new Object[][]{
                {-1, 0, ChequeStamp.NO_STAMP, text, "Cheque Identifier"},
                {1000, 0, ChequeStamp.NO_STAMP, text, "Cheque Identifier"},
                {999, -1, ChequeStamp.NO_STAMP, text, "Cheque Destination"},
                {999, 16, ChequeStamp.NO_STAMP, text, "Cheque Destination"},
                {999, 15, null, text, "Cheque Stamp"},
                {998, 14, ChequeStamp.NO_STAMP, null, "Cheque Endorse Text"}
        };
    }

    @Test(dataProvider = "invalidArgsProvider")
    public void should_throw_expected_exception_on_invalid_constructor_arg(int chequeId,
                                                                           int chequeDestination,
                                                                           ChequeStamp stamp,
                                                                           String text,
                                                                           String expectedMessage) {
        Assertions.assertThatThrownBy(() -> new Cheque(chequeId, chequeDestination, stamp, text))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }
}
