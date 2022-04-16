package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.SupplyModeReadyStatusAmountLength;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.statusmessage.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.Cassette;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.CdmRecycleCassette;
import io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata.TransactionData;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.mockito.Mockito.*;

public class ReadyBStatusAppenderTest extends DeviceFaultTest {
    private final TransactionData<? extends Cassette> transactionData
            = TransactionData.depositData(new CdmRecycleCassette(1, 1,
            BmpStringGenerator.HEX.fixedLength(10)));
    private ReadyBStatusMessageListener messageListenerMock;
    private SolicitedStatusMessageBuilder<SolicitedStatusInformation> solicitedStatusMessageBuilder;


    private ReadyBStatusAppender appender;
    private NdcComponentReader<TransactionData<? extends Cassette>> transactionDataReaderMock;
    private NdcComponentReader<Optional<CompletionData>> completionDataReaderMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        solicitedStatusMessageBuilder = new SolicitedStatusMessageBuilder<>()
                .withLuno(Luno.DEFAULT)
                .withTimeVariantNumber(-1)
                .withStatusDescriptor(StatusDescriptor.READY_B);

        messageListenerMock = mock(ReadyBStatusMessageListener.class);
        transactionDataReaderMock = mock(NdcComponentReader.class);
        completionDataReaderMock = mock(NdcComponentReader.class);
        appender = new ReadyBStatusAppender(messageListenerMock,
                transactionDataReaderMock,
                completionDataReaderMock,
                nextAppenderMock);

        doReturn(transactionData)
                .when(transactionDataReaderMock)
                .readComponent(any());
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String mac = BmpStringGenerator.HEX.fixedLength(8);

        final ReadyBStatus readyBWithTransactionData = new ReadyBStatus(1, transactionData);
        final ReadyBStatus readyBWithTsn = new ReadyBStatus(1);


        final String withTsn = NdcConstants.FIELD_SEPARATOR
                + readyBWithTsn.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        final String withTransactionData = NdcConstants.FIELD_SEPARATOR
                + readyBWithTransactionData.toNdcString()
                + NdcConstants.FIELD_SEPARATOR
                + mac;

        return new Object[][]{
                {EMPTY_STRING, dummyCompletionData, null, SupplyModeReadyStatusAmountLength.DEFAULT},
                {EMPTY_STRING, null, null, SupplyModeReadyStatusAmountLength.DEFAULT},
                {withTsn, dummyCompletionData, readyBWithTsn, SupplyModeReadyStatusAmountLength.TRANSACTION_STATUS_DATA},
                {withTransactionData, dummyCompletionData, readyBWithTransactionData, SupplyModeReadyStatusAmountLength.TRANSACTION_STATUS_DATA}
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String validData,
                                             CompletionData completionData,
                                             ReadyBStatus readyBStatus,
                                             ConfigurationOption readyStatusOption) {
        when(deviceConfigurationMock.getConfigurationOptions())
                .thenReturn(ConfigurationOptions.of(readyStatusOption));
        when(completionDataReaderMock.readComponent(any()))
                .thenReturn(Optional.ofNullable(completionData));

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, solicitedStatusMessageBuilder, deviceConfigurationMock);

        final SolicitedStatusMessage<ReadyBStatus> expectedMessage = SolicitedStatusMessage.<ReadyBStatus>builder()
                .withLuno(solicitedStatusMessageBuilder.getLuno())
                .withTimeVariantNumber(solicitedStatusMessageBuilder.getTimeVariantNumber())
                .withStatusDescriptor(solicitedStatusMessageBuilder.getStatusDescriptor())
                .withCompletionData(completionData)
                .withStatusInformation(readyBStatus)
                .withMac(EMPTY_STRING)
                .build();

        verify(messageListenerMock, times(1))
                .onReadyBStatusMessage(expectedMessage);
        verify(nextAppenderMock, times(1))
                .appendComponent(any(), any(), any());

        verifyNoMoreInteractions(messageListenerMock, nextAppenderMock);
    }
}
