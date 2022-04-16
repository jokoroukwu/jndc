package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMsgAppenderTest;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class BunchChequeDepositBufferAppenderTest extends TransactionRequestMsgAppenderTest {
    private final BunchChequeDepositData dummyBunchChequeDepositData = new BunchChequeDepositData(1);
    private NdcComponentReader<BunchChequeDepositData> depositDataReaderMock;
    private BunchChequeDepositBufferAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        depositDataReaderMock = mock(NdcComponentReader.class);
        when(depositDataReaderMock.readComponent(any()))
                .thenReturn(dummyBunchChequeDepositData);

        appender = new BunchChequeDepositBufferAppender(nextAppenderMock, depositDataReaderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID, BunchChequeDepositBuffer.EMPTY},
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID + "A", new BunchChequeDepositBuffer(dummyBunchChequeDepositData)},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData, BunchChequeDepositBuffer expectedValue) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(messageBuilder.getBunchChequeDepositBuffer())
                .isEqualTo(expectedValue);
    }

    @DataProvider
    public Object[][] nextAppenderCallTestProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID, 0},
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID + "A", 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID + NdcConstants.FIELD_SEPARATOR, 1},
        };
    }

    @Test(dataProvider = "nextAppenderCallTestProvider")
    public void should_optionally_call_next_appender(String validData, int expectedNumberOfInvocations) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        verify(nextAppenderMock, times(expectedNumberOfInvocations))
                .appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @DataProvider
    public Object[][] remainingDataProvider() {
        return new Object[][]{
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID, 1},
                {NdcConstants.FIELD_SEPARATOR_STRING + BunchChequeDepositBuffer.ID + "A", 2},
        };
    }

    @Test(dataProvider = "remainingDataProvider")
    public void should_leave_remaining_data_untouched(String validData, int remainingDataLength) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData + NdcConstants.FIELD_SEPARATOR);

        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        Assertions.assertThat(buffer.remaining())
                .isEqualTo(remainingDataLength);
    }

    @Test
    public void should_skip_buffer_meta() {
        final FakerBunchChequeDepositBufferAppender fakeAppenderMock =
                mock(FakerBunchChequeDepositBufferAppender.class, CALLS_REAL_METHODS);

        fakeAppenderMock.appendComponent(NdcCharBuffer.EMPTY, messageBuilder, deviceConfigurationMock);

        verify(fakeAppenderMock, times(1))
                .skipBufferMeta(NdcCharBuffer.EMPTY);
    }

    private static class FakerBunchChequeDepositBufferAppender extends BunchChequeDepositBufferAppender {
        public FakerBunchChequeDepositBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                                     NdcComponentReader<BunchChequeDepositData> depositDataReader) {
            super(nextAppender, depositDataReader);
        }

        @Override
        protected void skipBufferMeta(NdcCharBuffer ndcCharBuffer) {

        }
    }
}
