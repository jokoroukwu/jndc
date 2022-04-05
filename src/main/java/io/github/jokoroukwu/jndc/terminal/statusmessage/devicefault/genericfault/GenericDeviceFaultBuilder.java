package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataAcceptor;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAcceptor;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class GenericDeviceFaultBuilder implements DiagnosticStatusAcceptor<GenericDeviceFaultBuilder>,
        CompletionDataAcceptor<GenericDeviceFaultBuilder> {
    private Dig dig;
    private String transactionStatus = Strings.EMPTY_STRING;
    private List<ErrorSeverity> errorSeverities = Collections.emptyList();
    private DiagnosticStatus diagnosticStatus;
    private CompletionData completionData;
    private List<SuppliesStatus> suppliesStatuses = Collections.emptyList();
    private String additionalData = Strings.EMPTY_STRING;

    public GenericDeviceFaultBuilder withDig(Dig dig) {
        this.dig = dig;
        return this;
    }

    public GenericDeviceFaultBuilder withTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public GenericDeviceFaultBuilder withErrorSeverities(List<ErrorSeverity> errorSeverities) {
        this.errorSeverities = errorSeverities;
        return this;
    }

    @Override
    public GenericDeviceFaultBuilder withDiagnosticStatus(DiagnosticStatus diagnosticStatus) {
        this.diagnosticStatus = diagnosticStatus;
        return this;
    }

    @Override
    public GenericDeviceFaultBuilder withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public GenericDeviceFaultBuilder withSuppliesStatuses(List<SuppliesStatus> suppliesStatuses) {
        this.suppliesStatuses = suppliesStatuses;
        return this;
    }

    public GenericDeviceFaultBuilder withAdditionalData(String additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public GenericDeviceFault build() {
        return new GenericDeviceFault(dig,
                transactionStatus,
                errorSeverities,
                diagnosticStatus,
                completionData,
                suppliesStatuses);
    }

    GenericDeviceFault buildWithNoValidation() {
        return new GenericDeviceFault(dig,
                transactionStatus,
                errorSeverities,
                diagnosticStatus,
                completionData,
                suppliesStatuses,
                additionalData,
                null);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericDeviceFaultBuilder.class.getSimpleName() + ": {", "}")
                .add("dig: " + dig)
                .add("transactionStatus: '" + transactionStatus + '\'')
                .add("errorSeverities: " + errorSeverities)
                .add("diagnosticStatus: " + diagnosticStatus)
                .add("completionData: " + completionData)
                .add("suppliesStatuses: " + suppliesStatuses)
                .add("additionalData: '" + additionalData + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericDeviceFaultBuilder that = (GenericDeviceFaultBuilder) o;
        return dig == that.dig &&
                Objects.equals(transactionStatus, that.transactionStatus) &&
                Objects.equals(errorSeverities, that.errorSeverities) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData) &&
                Objects.equals(suppliesStatuses, that.suppliesStatuses) &&
                Objects.equals(additionalData, that.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dig, transactionStatus, errorSeverities, diagnosticStatus, completionData,
                suppliesStatuses, additionalData);
    }
}
