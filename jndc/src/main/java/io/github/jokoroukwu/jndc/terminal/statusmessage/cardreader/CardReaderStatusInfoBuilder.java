package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

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

public class CardReaderStatusInfoBuilder implements CompletionDataAcceptor<CardReaderStatusInfoBuilder>,
        DiagnosticStatusAcceptor<CardReaderStatusInfoBuilder> {
    private CardReaderWriterStatus transactionDeviceStatus;
    private List<ErrorSeverity> errorSeverities = Collections.emptyList();
    private DiagnosticStatus diagnosticStatus;
    private CompletionData completionData;
    private SuppliesStatus suppliesStatus;
    private CardReaderWriterAdditionalData additionalData;

    public CardReaderStatusInfoBuilder withTransactionDeviceStatus(CardReaderWriterStatus transactionDeviceStatus) {
        this.transactionDeviceStatus = transactionDeviceStatus;
        return this;
    }

    public CardReaderStatusInfoBuilder withErrorSeverities(List<ErrorSeverity> errorSeverities) {
        this.errorSeverities = errorSeverities;
        return this;
    }

    @Override
    public CardReaderStatusInfoBuilder withDiagnosticStatus(DiagnosticStatus diagnosticStatus) {
        this.diagnosticStatus = diagnosticStatus;
        return this;
    }

    @Override
    public CardReaderStatusInfoBuilder withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public CardReaderStatusInfoBuilder withSuppliesStatus(SuppliesStatus suppliesStatus) {
        this.suppliesStatus = suppliesStatus;
        return this;
    }

    public CardReaderStatusInfoBuilder withAdditionalData(CardReaderWriterAdditionalData additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public CardReaderWriterStatusInfo build() {
        return new CardReaderWriterStatusInfo(transactionDeviceStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatus, additionalData);
    }

    public CardReaderWriterStatus getTransactionDeviceStatus() {
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
        return new StringJoiner(", ", CardReaderStatusInfoBuilder.class.getSimpleName() + ": {", "}")
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
        CardReaderStatusInfoBuilder that = (CardReaderStatusInfoBuilder) o;
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
