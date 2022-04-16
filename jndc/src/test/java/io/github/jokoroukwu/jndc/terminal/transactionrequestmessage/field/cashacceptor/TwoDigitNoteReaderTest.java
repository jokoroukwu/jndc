package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TwoDigitNoteReaderTest {

    @DataProvider
    public static Object[][] validDataProvider() {
        return new Object[][]{
                {"0101", new TwoDigitNote(1, 1)},
                {"3290", new TwoDigitNote(50, 90)}
        };
    }

    @DataProvider
    public static Object[][] invalidNoteTypeProvider() {
        return new Object[][]{
                {"00"},
                {"33"},
                {"34"}
        };
    }

    @DataProvider
    public static Object[][] invalidNoteNumberProvider() {
        return new Object[][]{
                {"00"},
                {"91"},
                {"92"}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_value(String bufferData, CashAcceptorNote expectedResult) {
        final CashAcceptorNote actualResult = TwoDigitNoteReader.INSTANCE.readComponent(NdcCharBuffer.wrap(bufferData));

        Assertions.assertThat(actualResult)
                .isEqualTo(expectedResult);
    }

    @Test
    public void should_leave_remaining_data_untouched() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("0101" + remainingData);

        TwoDigitNoteReader.INSTANCE.readComponent(buffer);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingData.length());
    }

    @Test(dataProvider = "invalidNoteTypeProvider")
    public void should_throw_expected_exception_on_invalid_note_type(String invalidNoteType) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(invalidNoteType + "90");

        Assertions.assertThatThrownBy(() -> TwoDigitNoteReader.INSTANCE.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("should be in range 1-50 dec")
                .hasMessageContaining("Cash Acceptor Buffer");
    }

    @Test(dataProvider = "invalidNoteNumberProvider")
    public void should_throw_expected_exception_on_invalid_number_of_notes(String invalidNoteNumber) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap("01" + invalidNoteNumber);

        Assertions.assertThatThrownBy(() -> TwoDigitNoteReader.INSTANCE.readComponent(buffer))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString())
                .hasMessageContaining("should be in range 01-90")
                .hasMessageContaining("Cash Acceptor Buffer")
                .hasMessageContaining("Number of notes");
    }
}
