package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.*;

public class CardReaderWriterStatusInfo implements DeviceStatusInformation, SolicitedStatusInformation, UnsolicitedStatusInformation {
    private final TransactionDeviceStatus transactionDeviceStatus;
    private final List<ErrorSeverity> errorSeverities;
    private final DiagnosticStatus diagnosticStatus;
    private final CompletionData completionData;
    private final SuppliesStatus suppliesStatus;
    private final CardReaderWriterAdditionalData additionalData;

    public CardReaderWriterStatusInfo(TransactionDeviceStatus transactionDeviceStatus,
                                      Collection<ErrorSeverity> errorSeverities,
                                      DiagnosticStatus diagnosticStatus,
                                      CompletionData completionData,
                                      SuppliesStatus suppliesStatus,
                                      CardReaderWriterAdditionalData additionalData) {
        this.errorSeverities = List.copyOf(errorSeverities);
        this.transactionDeviceStatus = transactionDeviceStatus;
        this.diagnosticStatus = diagnosticStatus;
        this.completionData = completionData;
        this.suppliesStatus = suppliesStatus;
        this.additionalData = additionalData;
    }

    public CardReaderWriterStatusInfo(TransactionDeviceStatus transactionDeviceStatus,
                                      Collection<ErrorSeverity> errorSeverities,
                                      DiagnosticStatus diagnosticStatus,
                                      CompletionData completionData,
                                      SuppliesStatus suppliesStatus) {
        this(transactionDeviceStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatus, null);
    }

    @Override
    public Dig getDig() {
        return Dig.MAGNETIC_CARD_READER_WRITER;
    }

    public Optional<TransactionDeviceStatus> getTransactionDeviceStatus() {
        return Optional.ofNullable(transactionDeviceStatus);
    }

    public List<ErrorSeverity> getErrorSeverities() {
        return errorSeverities;
    }

    public Optional<DiagnosticStatus> getDiagnosticStatus() {
        return Optional.ofNullable(diagnosticStatus);
    }

    public Optional<CompletionData> getCompletionData() {
        return Optional.ofNullable(completionData);
    }

    public Optional<CardReaderWriterAdditionalData> getAdditionalData() {
        return Optional.ofNullable(additionalData);
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(128)
                .append(getDig().getValue());
        if (additionalData != null) {
            return builder
                    .appendComponent(transactionDeviceStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendComponent(NdcConstants.GROUP_SEPARATOR_STRING, completionData)
                    .appendFs()
                    .appendComponent(suppliesStatus)
                    .appendFs()
                    .appendComponent(additionalData)
                    .toString();
        }
        if (suppliesStatus != null) {
            return builder
                    .appendComponent(transactionDeviceStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendComponent(NdcConstants.GROUP_SEPARATOR_STRING, completionData)
                    .appendFs()
                    .appendComponent(suppliesStatus)
                    .toString();
        }
        if (completionData != null) {
            return builder
                    .appendComponent(transactionDeviceStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendGs()
                    .appendComponent(completionData)
                    .toString();
        }
        if (diagnosticStatus != null) {
            return builder
                    .appendComponent(transactionDeviceStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .toString();
        }
        if (!errorSeverities.isEmpty()) {
            return builder
                    .appendComponent(transactionDeviceStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .toString();
        }
        return builder.appendComponent(transactionDeviceStatus).toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CardReaderWriterStatusInfo.class.getSimpleName() + ": {", "}")
                .add("transactionDeviceStatus: " + transactionDeviceStatus)
                .add("errorSeverities: " + errorSeverities)
                .add("diagnosticStatus: " + diagnosticStatus)
                .add("completionData: " + completionData)
                .add("suppliesStatus: " + suppliesStatus)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardReaderWriterStatusInfo that = (CardReaderWriterStatusInfo) o;
        return transactionDeviceStatus == that.transactionDeviceStatus &&
                suppliesStatus == that.suppliesStatus &&
                Objects.equals(errorSeverities, that.errorSeverities) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionDeviceStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatus);
    }
}
