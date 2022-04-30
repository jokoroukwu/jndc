package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreaderwriter;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.DeviceStatusInformationTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFault;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.CardReaderWriterFaultBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader.TransactionDeviceStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class CardReaderWriterFaultTest extends DeviceStatusInformationTest {
    private final Dig dig = Dig.MAGNETIC_CARD_READER_WRITER;

    private final ErrorSeverity dummyErrorSeverity = ErrorSeverity.NO_ERROR;
    private final DiagnosticStatus dummyDiagnosticStatus = new DiagnosticStatus(1, BmpStringGenerator.HEX.fixedLength(2));
    private final TransactionDeviceStatus dummyTransactionStatus = TransactionDeviceStatus.NO_EXCEPTION;

    @DataProvider
    public Object[][] validDataProvider() {
        final List<ErrorSeverity> errorSeverities = List.of(dummyErrorSeverity);
        final String withDig = dig.toNdcString();
        final SuppliesStatus suppliesStatus = SuppliesStatus.GOOD_STATE;

        final String withTransactionStatus = withDig + dummyTransactionStatus.toNdcString();
        final String withErrorSeverity = withTransactionStatus + NdcConstants.FIELD_SEPARATOR + dummyErrorSeverity.toNdcString();
        final String withDiagnosticStatus = withErrorSeverity + NdcConstants.FIELD_SEPARATOR + dummyDiagnosticStatus.toNdcString();
        final String withCompletionData = withDiagnosticStatus + NdcConstants.GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withSuppliesStatus = withCompletionData + NdcConstants.FIELD_SEPARATOR + suppliesStatus.toNdcString();
        final String withSuppliesStatusOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(3) + suppliesStatus.toNdcString();
        final String withCompletionDataOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + NdcConstants.GROUP_SEPARATOR + dummyCompletionData.toNdcString();
        final String withDiagnosticStatusOnly = withDig + NdcConstants.FIELD_SEPARATOR_STRING.repeat(2) + dummyDiagnosticStatus.toNdcString();
        final String withErrorSeverityOnly = withDig + NdcConstants.FIELD_SEPARATOR + dummyErrorSeverity.toNdcString();
        return new Object[][]{
                {"with DIG", withDig, null, List.of(), null, null, null},

                {"with Transaction Status", withTransactionStatus, dummyTransactionStatus, List.of(), null, null, null},

                {"with Error Severities", withErrorSeverity, dummyTransactionStatus, errorSeverities, null, null, null},

                {"with Diagnostic Status", withDiagnosticStatus, dummyTransactionStatus, errorSeverities,
                        dummyDiagnosticStatus, null, null},

                {"with Completion Data", withCompletionData, dummyTransactionStatus, errorSeverities,
                        dummyDiagnosticStatus, dummyCompletionData, null},

                {"with Supplies Statuses", withSuppliesStatus, dummyTransactionStatus, errorSeverities,
                        dummyDiagnosticStatus, dummyCompletionData, suppliesStatus},

                {"with Supply Statuses only", withSuppliesStatusOnly, null, List.of(), null, null, suppliesStatus},

                {"with Completion Data only", withCompletionDataOnly, null, List.of(), null, dummyCompletionData, null},

                {"with Diagnostic Statuses only", withDiagnosticStatusOnly, null, List.of(), dummyDiagnosticStatus, null, null},

                {"with Error Severities only", withErrorSeverityOnly, null, errorSeverities, null, null, null},

        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_return_expected_ndc_string(String unusedDescription,
                                                  String expectedString,
                                                  TransactionDeviceStatus transactionStatus,
                                                  List<ErrorSeverity> errorSeverities,
                                                  DiagnosticStatus diagnosticStatus,
                                                  CompletionData completionData,
                                                  SuppliesStatus suppliesStatuses) {
        final CardReaderWriterFault cardReaderWriterFault = new CardReaderWriterFaultBuilder()
                .withTransactionDeviceStatus(transactionStatus)
                .withErrorSeverities(errorSeverities)
                .withDiagnosticStatus(diagnosticStatus)
                .withCompletionData(completionData)
                .withSuppliesStatus(suppliesStatuses)
                .build();

        Assertions.assertThat(cardReaderWriterFault.toNdcString())
                .isEqualTo(expectedString);
    }
}
