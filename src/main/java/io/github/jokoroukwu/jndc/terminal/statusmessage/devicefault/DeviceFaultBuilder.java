package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class DeviceFaultBuilder {
    private Dig dig;
    private String transactionStatus = Strings.EMPTY_STRING;
    private List<ErrorSeverity> errorSeverity = Collections.emptyList();
    private DiagnosticStatus diagnosticStatus;
    private CompletionData completionData;
    private List<SuppliesStatus> suppliesStatus = Collections.emptyList();

    public DeviceFaultBuilder withDig(Dig dig) {
        this.dig = dig;
        return this;
    }

    public DeviceFaultBuilder withTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public DeviceFaultBuilder withErrorSeverity(List<ErrorSeverity> errorSeverity) {
        this.errorSeverity = errorSeverity;
        return this;
    }

    public DeviceFaultBuilder withDiagnosticStatus(DiagnosticStatus diagnosticStatus) {
        this.diagnosticStatus = diagnosticStatus;
        return this;
    }

    public DeviceFaultBuilder withCompletionData(CompletionData completionData) {
        this.completionData = completionData;
        return this;
    }

    public DeviceFaultBuilder withSuppliesStatus(List<SuppliesStatus> suppliesStatus) {
        this.suppliesStatus = suppliesStatus;
        return this;
    }

    public DeviceFaultBase build() {
        return new DeviceFaultBase(dig, transactionStatus, errorSeverity, diagnosticStatus, completionData, suppliesStatus);
    }

    DeviceFaultBase buildWithNoValidation() {
        return new DeviceFaultBase(dig, transactionStatus, errorSeverity, diagnosticStatus, completionData, suppliesStatus);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceFaultBuilder.class.getSimpleName() + ": {", "}")
                .add("dig: " + dig)
                .add("transactionStatus: '" + transactionStatus + '\'')
                .add("errorSeverity: " + errorSeverity)
                .add("diagnosticStatus: " + diagnosticStatus)
                .add("completionData: " + completionData)
                .add("suppliesStatus: " + suppliesStatus)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceFaultBuilder that = (DeviceFaultBuilder) o;
        return Objects.equals(dig, that.dig) &&
                Objects.equals(transactionStatus, that.transactionStatus) &&
                Objects.equals(errorSeverity, that.errorSeverity) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData) &&
                Objects.equals(suppliesStatus, that.suppliesStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dig, transactionStatus, errorSeverity, diagnosticStatus, completionData, suppliesStatus);
    }
}
