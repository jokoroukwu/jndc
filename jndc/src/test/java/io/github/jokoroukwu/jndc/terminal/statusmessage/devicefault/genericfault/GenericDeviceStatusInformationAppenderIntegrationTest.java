package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.transactionreply.smartcard.ScriptId;
import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.ProcessingResult;
import io.github.jokoroukwu.jndc.terminal.completiondata.ScriptResult;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.github.jokoroukwu.jndc.util.NdcConstants.*;
import static io.github.jokoroukwu.jndc.util.text.Strings.EMPTY_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GenericDeviceStatusInformationAppenderIntegrationTest {
    private final Dig dig = Dig.MAGNETIC_CARD_READER_WRITER;
    private final SolicitedStatusMessageBuilder<SolicitedStatusInformation> solicitedDeviceFaultMeta
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
    private final String dummyTransactionStatus = BmpStringGenerator.HEX.randomLength(20);

    private SolicitedGenericDeviceFaultMessageListener messageListenerMock;
    private SolicitedGenericDeviceFaultAppender appender;
    private DeviceConfiguration deviceConfigurationMock;
    private ConfigurableNdcComponentAppender<MacAcceptor<?>> macAppenderMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(false);
        macAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        messageListenerMock = mock(SolicitedGenericDeviceFaultMessageListener.class);
        appender = new SolicitedGenericDeviceFaultAppender(dig, messageListenerMock,
                new GenericTransactionStatusAppender("command-name"), macAppenderMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final List<SuppliesStatus> suppliesStatuses = List.of(dummySuppliesStatus, dummySuppliesStatus);
        final List<ErrorSeverity> errorSeverityList = List.of(dummyErrorSeverity, dummyErrorSeverity);
        final String withDig = EMPTY_STRING;
        final String withTransactionStatus = withDig + dummyTransactionStatus;
        final String withErrorSeverity = withTransactionStatus + FIELD_SEPARATOR + dummyErrorSeverity.toNdcString().repeat(2);
        final String withDiagnosticStatus = withErrorSeverity + FIELD_SEPARATOR + dummyDiagnosticStatus.toNdcString();
        final String withCompletionData = withDiagnosticStatus + GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withSuppliesStatus = withCompletionData + FIELD_SEPARATOR + dummySuppliesStatus.toNdcString().repeat(2);
        final String withSuppliesStatusOnly = withDig + FIELD_SEPARATOR_STRING.repeat(3) + dummySuppliesStatus.toNdcString().repeat(2);
        final String withCompletionDataOnly = withDig + FIELD_SEPARATOR_STRING.repeat(2) + GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withDiagnosticStatusOnly = withDig + FIELD_SEPARATOR_STRING.repeat(2) + dummyDiagnosticStatus.toNdcString();
        final String withErrorSeverityOnly = withDig + FIELD_SEPARATOR + dummyErrorSeverity.toNdcString().repeat(2);

        return new Object[][]{
                {"with DIG", withDig, EMPTY_STRING, List.of(), null, null, List.of()},

                {"with Transaction Status", withTransactionStatus, dummyTransactionStatus, List.of(), null, null, List.of()},

                {"with Error Severity", withErrorSeverity, dummyTransactionStatus, errorSeverityList, null, null, List.of()},

                {"with Diagnostic Status", withDiagnosticStatus, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, null, List.of()},

                {"with Completion Data", withCompletionData, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, dummyCompletionData, List.of()},

                {"with Supplies Status", withSuppliesStatus, dummyTransactionStatus, errorSeverityList,
                        dummyDiagnosticStatus, dummyCompletionData, suppliesStatuses},

                {"with Supplies Status only", withSuppliesStatusOnly, EMPTY_STRING, List.of(), null, null,
                        suppliesStatuses},

                {"with Completion Data only", withCompletionDataOnly, EMPTY_STRING, List.of(), null,
                        dummyCompletionData, List.of()},

                {"with Diagnostic Status only", withDiagnosticStatusOnly, EMPTY_STRING, List.of(), dummyDiagnosticStatus,
                        null, List.of()},

                {"with Error Severity only", withErrorSeverityOnly, EMPTY_STRING, errorSeverityList, null, null, List.of()},
        };
    }


    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_result(String unusedDescription,
                                              String validData,
                                              String transactionStatus,
                                              List<ErrorSeverity> errorSeverities,
                                              DiagnosticStatus diagnosticStatus,
                                              CompletionData completionData,
                                              List<SuppliesStatus> suppliesStatuses) {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, solicitedDeviceFaultMeta, deviceConfigurationMock);
        final SolicitedStatusMessage<GenericDeviceFault> expectedMessage = SolicitedStatusMessage.<GenericDeviceFault>builder()
                .withLuno(solicitedDeviceFaultMeta.getLuno())
                .withTimeVariantNumber(solicitedDeviceFaultMeta.getTimeVariantNumber())
                .withMac(EMPTY_STRING)
                .withStatusDescriptor(StatusDescriptor.DEVICE_FAULT)
                .withStatusInformation(new GenericDeviceFaultBuilder()
                        .withDig(dig)
                        .withTransactionStatus(transactionStatus)
                        .withErrorSeverities(errorSeverities)
                        .withDiagnosticStatus(diagnosticStatus)
                        .withCompletionData(completionData)
                        .withSuppliesStatuses(suppliesStatuses)
                        .build())
                .build();

        verify(messageListenerMock, times(1))
                .onSolicitedGenericDeviceFaultMessage(expectedMessage);
        verify(macAppenderMock, times(1))
                .appendComponent(any(), any(), any());

        verifyNoMoreInteractions(messageListenerMock, macAppenderMock);
    }
}
