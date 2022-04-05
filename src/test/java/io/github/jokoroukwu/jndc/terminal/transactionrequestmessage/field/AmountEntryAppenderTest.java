package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry.AmountEntry;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.amountentry.AmountEntryFieldAppender;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.IStringGenerator;
import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

public class AmountEntryAppenderTest extends TransactionRequestMsgAppenderTest {
    private final IStringGenerator generator = BmpStringGenerator.DECIMAL;
    private AmountEntryFieldAppender appender;

    @BeforeMethod
    public void setUp() {
        appender = new AmountEntryFieldAppender(nextAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final AmountEntry eightDigits = AmountEntry.base(ThreadLocalRandom.current().nextLong(0, 100_000_000));
        final AmountEntry twelveDigits = AmountEntry.extended(ThreadLocalRandom.current().nextLong(0, 1_000_000_000_000L));
        return new Object[][]{
                {eightDigits.toNdcString(), eightDigits},
                {twelveDigits.toNdcString(), twelveDigits},
                {Strings.EMPTY_STRING, null}
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData, AmountEntry expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getAmountEntry())
                .isEqualTo(expectedValue);
    }

    @Test(dataProvider = "validDataProvider")
    public void should_call_next_appender(String validData, AmountEntry unused) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Mockito.verify(nextAppenderMock, Mockito.times(1))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @DataProvider
    public Object[][] remainingDataLengthProvider() {
        final String remainingData = BmpStringGenerator.HEX.randomLength(20);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR + generator.fixedLength(8) + NdcConstants.FIELD_SEPARATOR + remainingData,
                        remainingData.length() + 1},
                {NdcConstants.FIELD_SEPARATOR + generator.fixedLength(12) + remainingData, remainingData.length()}

        };
    }

    @Test(dataProvider = "remainingDataLengthProvider")
    public void should_leave_remaining_data_untouched(String validData, int remainingDataLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingDataLength);
    }

    @Test
    public void should_throw_expected_exception_on_invalid_amount_entry() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR + BmpStringGenerator.HEX.fixedLength(7));
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AmountEntryFieldAppender.FIELD_NAME);
    }


    @Test
    public void should_throw_expected_exception_on_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.EMPTY;
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(AmountEntryFieldAppender.FIELD_NAME, "missing field separator");
    }
}
