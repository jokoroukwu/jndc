package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.ScriptId;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.ProcessingResult;
import io.github.jokoroukwu.jndc.terminal.completiondata.ScriptResult;
import io.github.jokoroukwu.jndc.terminal.statusmessage.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader.*;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.mockito.Mockito.*;

public class SolicitedCardReaderWriterStatusInfoAppenderIntegrationTest extends DeviceStatusInformationTest {
    private final SolicitedStatusMessageBuilder<SolicitedStatusInformation> solicitedStatusMessageBuilder
            = new SolicitedStatusMessageBuilder<>()
            .withLuno(Luno.DEFAULT)
            .withTimeVariantNumber(-1)
            .withStatusDescriptor(StatusDescriptor.DEVICE_FAULT);
    private final TransactionCategoryCode dummyCurrencyCodeTlv = new TransactionCategoryCode("AB");
    private final CompletionData dummyCompletionData = new CompletionData(Map.of(dummyCurrencyCodeTlv.getTag(), dummyCurrencyCodeTlv),
            List.of(new ScriptResult(ProcessingResult.SUCCESS, 1,
                    new ScriptId(BmpStringGenerator.HEX.fixedLength(8)))));
    private final ErrorSeverity dummyErrorSeverity = ErrorSeverity.NO_ERROR;
    private final DiagnosticStatus dummyDiagnosticStatus = new DiagnosticStatus(1, BmpStringGenerator.HEX.fixedLength(2));
    private final SuppliesStatus dummySuppliesStatus = SuppliesStatus.GOOD_STATE;
    private final CardReaderWriterStatus dummyTransactionStatus = CardReaderWriterStatus.NO_EXCEPTION;
    private SolicitedCardReaderWriterStatusInfoMessageListener messageListenerMock;
    private ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<?>> macAppenderMock;


    private SolicitedCardReaderWriterStatusInfoAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        messageListenerMock = mock(SolicitedCardReaderWriterStatusInfoMessageListener.class);
        macAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appender = new SolicitedCardReaderWriterStatusInfoAppender(messageListenerMock,
                new CardReaderWriterTransactionStatusAppender("commandName"),
                macAppenderMock);

        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(false);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final List<ErrorSeverity> errorSeverityList = List.of(dummyErrorSeverity);
        final String withDig = EMPTY_STRING;
        final String withTransactionStatus = withDig + dummyTransactionStatus.toNdcString();
        final String withErrorSeverity = withTransactionStatus + NdcConstants.FIELD_SEPARATOR + dummyErrorSeverity.toNdcString();
        final String withDiagnosticStatus = withErrorSeverity + NdcConstants.FIELD_SEPARATOR + dummyDiagnosticStatus.toNdcString();
        final String withCompletionData = withDiagnosticStatus + NdcConstants.GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withSuppliesStatus = withCompletionData + NdcConstants.FIELD_SEPARATOR + dummySuppliesStatus.toNdcString();
        final String withSuppliesStatusOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(3) + dummySuppliesStatus.toNdcString();
        final String withCompletionDataOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + NdcConstants.GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withDiagnosticStatusOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + dummyDiagnosticStatus.toNdcString();
        final String withErrorSeverityOnly = withDig + NdcConstants.FIELD_SEPARATOR + dummyErrorSeverity.toNdcString();

        return new Object[][]{
                {"with DIG", withDig, null, List.of(), null, null, null},

                {"with Transaction Status", withTransactionStatus, dummyTransactionStatus, List.of(), null, null, null},

                {"with Error Severity", withErrorSeverity, dummyTransactionStatus, errorSeverityList, null, null, null},

                {"with Diagnostic Status", withDiagnosticStatus, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, null, null},

                {"with Completion Data", withCompletionData, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, dummyCompletionData, null},

                {"with Supplies Status", withSuppliesStatus, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, dummyCompletionData, dummySuppliesStatus},

                {"with Supplies Status only", withSuppliesStatusOnly, null, List.of(), null, null,
                        dummySuppliesStatus},

                {"with Completion Data only", withCompletionDataOnly, null, List.of(), null,
                        dummyCompletionData, null},

                {"with Diagnostic Status only", withDiagnosticStatusOnly, null, List.of(), dummyDiagnosticStatus,
                        null, null},

                {"with Error Severity only", withErrorSeverityOnly, null, errorSeverityList, null, null, null},
        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_result(String unusedDescription,
                                              String validData,
                                              CardReaderWriterStatus transactionStatus,
                                              List<ErrorSeverity> errorSeverities,
                                              DiagnosticStatus diagnosticStatus,
                                              CompletionData completionData,
                                              SuppliesStatus suppliesStatuses) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, solicitedStatusMessageBuilder, deviceConfigurationMock);
        final SolicitedStatusMessage<CardReaderWriterStatusInfo> expectedMessage = SolicitedStatusMessage.<CardReaderWriterStatusInfo>builder()
                .withLuno(solicitedStatusMessageBuilder.getLuno())
                .withTimeVariantNumber(solicitedStatusMessageBuilder.getTimeVariantNumber())
                .withMac(EMPTY_STRING)
                .withStatusDescriptor(StatusDescriptor.DEVICE_FAULT)
                .withStatusInformation(new CardReaderStatusInfoBuilder()
                        .withTransactionDeviceStatus(transactionStatus)
                        .withErrorSeverities(errorSeverities)
                        .withDiagnosticStatus(diagnosticStatus)
                        .withCompletionData(completionData)
                        .withSuppliesStatus(suppliesStatuses)
                        .build())
                .build();

        verify(messageListenerMock, times(1))
                .onSolicitedCardReaderWriterStatusInfoMessage(expectedMessage);
        verify(macAppenderMock, times(1))
                .appendComponent(any(), any(), any());
        verifyNoMoreInteractions(messageListenerMock, macAppenderMock);
    }
}
