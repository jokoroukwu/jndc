package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.cardreader;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataAcceptor;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAcceptor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CardReaderWriterFaultBuilder implements CompletionDataAcceptor<CardReaderWriterFaultBuilder>,
        DiagnosticStatusAcceptor<CardReaderWriterFaultBuilder> {
    private TransactionDeviceStatus transactionDeviceStatus;
    private List<ErrorSeverity> errorSeverities = Collections.emptyList();
    private DiagnosticStatus diagnosticStatus;
    private CompletionData completionData;
    private SuppliesStatus suppliesStatus;
    private CardReaderWriterAdditionalData additionalData;

    public CardReaderWriterFaultBuilder withTransactionDeviceStatus(TransactionDeviceStatus transactionDeviceStatus) {
        this.transactionDeviceStatus = transactionDeviceStatus;
        return this;
    }

    public CardReaderWriterFaultBuilder withErrorSeverities(List<ErrorSeverity> errorSeverities) {
        this.errorSeverities = errorSeverities;
        return this;
    }

    @Override
    public CardReaderWriterFaultBuilder withDiagnosticStatus(DiagnosticStatus diagnosticStatus) {
        this.diagnosticStatus = diagnosticStatus;
        return this;
    }

    @Override
    public CardReaderWriterFaultBuilder withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public CardReaderWriterFaultBuilder withSuppliesStatus(SuppliesStatus suppliesStatus) {
        this.suppliesStatus = suppliesStatus;
        return this;
    }

    public CardReaderWriterFaultBuilder withAdditionalData(CardReaderWriterAdditionalData additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public CardReaderWriterFault build() {
        return new CardReaderWriterFault(transactionDeviceStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatus, additionalData);
    }

    public TransactionDeviceStatus getTransactionDeviceStatus() {
        return transactionDeviceStatus;
    }

    public List<ErrorSeverity> getErrorSeverities() {
        return errorSeverities;
    }

    public DiagnosticStatus getDiagnosticStatus() {
        return diagnosticStatus;
    }

    public CompletionData getCompletionData() {
        return completionData;
    }

    public SuppliesStatus getSuppliesStatus() {
        return suppliesStatus;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CardReaderWriterFaultBuilder.class.getSimpleName() + ": {", "}")
                .add("transactionDeviceStatus: " + transactionDeviceStatus)
                .add("errorSeverities: " + errorSeverities)
                .add("diagnosticStatus: " + diagnosticStatus)
                .add("completionData: " + completionData)
                .add("suppliesStatus: " + suppliesStatus)
                .add("additionalData: " + additionalData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardReaderWriterFaultBuilder that = (CardReaderWriterFaultBuilder) o;
        return transactionDeviceStatus == that.transactionDeviceStatus &&
                suppliesStatus == that.suppliesStatus &&
                Objects.equals(errorSeverities, that.errorSeverities) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData) &&
                Objects.equals(additionalData, that.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionDeviceStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatus, additionalData);
    }
}
