package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.ScriptId;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.ProcessingResult;
import io.github.jokoroukwu.jndc.terminal.completiondata.ScriptResult;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LastTransactionStatusDataAppenderTest extends TransactionRequestMsgAppenderTest {
    private final CashDepositData dummyCashDepositData = new CashDepositData(DepositTransactionDirection.REFUND_DIRECTION);
    private final TransactionCategoryCode dummyCurrencyCodeTlv = new TransactionCategoryCode("AB");
    private final CompletionData dummyCompletionData = new CompletionData(Map.of(dummyCurrencyCodeTlv.getTag(), dummyCurrencyCodeTlv),
            List.of(new ScriptResult(ProcessingResult.SUCCESS, 1,
                    new ScriptId(BmpStringGenerator.HEX.fixedLength(8)))));
    private ConfigurableNdcComponentReader<List<Integer>> notesDispensedReaderMock;
    private NdcComponentReader<List<Integer>> coinsDispensedReaderMock;
    private NdcComponentReader<Optional<CashDepositData>> cashDepositDataReaderMock;
    private NdcComponentReader<Optional<CompletionData>> completionDataReaderMock;

    private LastTransactionStatusDataAppender appender;


    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        notesDispensedReaderMock = mock(ConfigurableNdcComponentReader.class);
        coinsDispensedReaderMock = mock(NdcComponentReader.class);
        cashDepositDataReaderMock = mock(NdcComponentReader.class);
        completionDataReaderMock = mock(NdcComponentReader.class);
        appender = new LastTransactionStatusDataAppender(nextAppenderMock,
                notesDispensedReaderMock,
                coinsDispensedReaderMock,
                cashDepositDataReaderMock,
                completionDataReaderMock);

        when(notesDispensedReaderMock.readComponent(any(), any()))
                .thenReturn(Collections.emptyList());
        when(coinsDispensedReaderMock.readComponent(any()))
                .thenReturn(Collections.emptyList());
        when(cashDepositDataReaderMock.readComponent(any()))
                .thenReturn(Optional.of(dummyCashDepositData));
        when(completionDataReaderMock.readComponent(any()))
                .thenReturn(Optional.of(dummyCompletionData));
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final List<Integer> emptyList = List.of();
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "9999" + '0', new LastTransactionStatusDataBase(9999,
                        LastStatusIssued.NONE_SENT, emptyList, emptyList, dummyCashDepositData, dummyCompletionData)
                },
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "0009" + '0', new LastTransactionStatusDataBase(9,
                        LastStatusIssued.NONE_SENT, emptyList, emptyList, dummyCashDepositData, dummyCompletionData)
                },
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "0009" + '0' + NdcConstants.GROUP_SEPARATOR_STRING + dummyCompletionData.toNdcString(),
                        new LastTransactionStatusDataBase(9, LastStatusIssued.NONE_SENT, emptyList, emptyList,
                                dummyCashDepositData, dummyCompletionData)
                },
                {NdcConstants.FIELD_SEPARATOR_STRING.repeat(2), null},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData, LastTransactionStatusDataBase expectedResult) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getLastTransactionStatusData())
                .isEqualTo(expectedResult);
    }

    @DataProvider
    public Object[][] nestedReadersTestProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "9999" + '0', 1},
                {NdcConstants.FIELD_SEPARATOR_STRING.repeat(2), 0},
        };
    }

    @Test(dataProvider = "nestedReadersTestProvider")
    public void should_call_nested_readers_expected_number_of_times(String validData, int expectedNumberOfInvocations) {

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(notesDispensedReaderMock, times(expectedNumberOfInvocations))
                .readComponent(buffer, deviceConfigurationMock);
        verify(coinsDispensedReaderMock, times(expectedNumberOfInvocations))
                .readComponent(buffer);
        verify(cashDepositDataReaderMock, times(expectedNumberOfInvocations))
                .readComponent(buffer);

        verifyNoMoreInteractions(notesDispensedReaderMock, coinsDispensedReaderMock, cashDepositDataReaderMock);
    }

    @DataProvider
    public Object[][] nextAppenderCallTestProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "9999" + '0', 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "9999" + '0' + BmpStringGenerator.HEX.fixedLength(1), 1},
                {NdcConstants.FIELD_SEPARATOR_STRING.repeat(2), 1},
        };
    }

    @Test(dataProvider = "nextAppenderCallTestProvider")
    public void should_call_next_appender_expected_number_of_times(String validData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verifyNoMoreInteractions(nextAppenderMock);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        final String randomRemainingData = BmpStringGenerator.HEX.randomLength(20);
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "9999" + '0' + randomRemainingData, randomRemainingData.length()},
                {NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + randomRemainingData, randomRemainingData.length() + 1}
        };
    }


    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String bufferData, int remainingDataLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(bufferData);
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .as("expected number of characters should remain")
                .isEqualTo(remainingDataLength);
    }

    @Test
    public void should_throw_expected_exception_on_id_mismatch_remaining_data_untouched() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + '8');
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(LastTransactionStatusDataAppender.FIELD_NAME, "ID");
    }

    @Test
    public void should_throw_expected_exception_on_preceding_field_separator_absence() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(Character.toString(LastTransactionStatusDataBase.ID));
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(LastTransactionStatusDataAppender.FIELD_NAME, "missing field separator");
    }


    @Test
    public void should_throw_expected_exception_on_invalid_serial_number() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "ABCD");
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContainingAll(LastTransactionStatusDataAppender.FIELD_NAME, "Last Transaction Serial Number");
    }

    @Test
    public void should_throw_expected_exception_on_invalid_last_status_issued() {
        final String invalidStatusIssued = "4";
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(NdcConstants.FIELD_SEPARATOR_STRING + LastTransactionStatusDataBase.ID + "0008" + invalidStatusIssued);
        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isExactlyInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(LastTransactionStatusDataAppender.FIELD_NAME)
                .hasMessageContaining("does not match any 'Last Status Issued'")
                .hasMessageContaining(invalidStatusIssued);
    }
}
