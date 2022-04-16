package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class DeviceFaultBase implements SolicitedStatusInformation {
    private final Dig dig;
    private final String transactionStatus;
    private final List<ErrorSeverity> errorSeverity;
    private final DiagnosticStatus diagnosticStatus;
    private final CompletionData completionData;
    private final List<SuppliesStatus> suppliesStatus;

    public DeviceFaultBase(Dig dig,
                           String transactionStatus,
                           Collection<ErrorSeverity> errorSeverity,
                           DiagnosticStatus diagnosticStatus,
                           CompletionData completionData,
                           Collection<SuppliesStatus> suppliesStatus) {
        this.dig = ObjectUtils.validateNotNull(dig, "'DIG'");
        this.transactionStatus = ObjectUtils.validateNotNull(transactionStatus, "'Transaction Status'");
        this.errorSeverity = List.copyOf(errorSeverity);
        this.suppliesStatus = List.copyOf(suppliesStatus);
        this.completionData = completionData;
        this.diagnosticStatus = diagnosticStatus;
    }

    DeviceFaultBase(Dig dig,
                    String transactionStatus,
                    List<ErrorSeverity> errorSeverity,
                    DiagnosticStatus diagnosticStatus,
                    CompletionData completionData,
                    List<SuppliesStatus> suppliesStatus) {
        this.dig = dig;
        this.transactionStatus = transactionStatus;
        this.errorSeverity = errorSeverity;
        this.suppliesStatus = suppliesStatus;
        this.completionData = completionData;
        this.diagnosticStatus = diagnosticStatus;
    }

    public static DeviceFaultBuilder builder() {
        return new DeviceFaultBuilder();
    }

    public Dig getDig() {
        return dig;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public List<ErrorSeverity> getErrorSeverity() {
        return errorSeverity;
    }

    public Optional<DiagnosticStatus> getDiagnosticStatus() {
        return Optional.ofNullable(diagnosticStatus);
    }

    public Optional<CompletionData> getCompletionData() {
        return Optional.ofNullable(completionData);
    }

    public List<SuppliesStatus> getSuppliesStatus() {
        return suppliesStatus;
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(128)
                .append(dig.getValue());
        if (!suppliesStatus.isEmpty()) {
            return builder.append(transactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverity, Strings.EMPTY_STRING)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendComponent(NdcConstants.GROUP_SEPARATOR_STRING, completionData)
                    .appendFs()
                    .appendComponents(suppliesStatus, Strings.EMPTY_STRING)
                    .toString();
        }
        if (completionData != null) {
            return builder.append(transactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverity, Strings.EMPTY_STRING)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendGs()
                    .appendComponent(completionData)
                    .toString();
        }
        if (diagnosticStatus != null) {
            return builder.append(transactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverity, Strings.EMPTY_STRING)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .toString();
        }
        if (!errorSeverity.isEmpty()) {
            return builder.append(transactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverity, Strings.EMPTY_STRING)
                    .toString();
        }
        return builder.append(transactionStatus).toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceFaultBase.class.getSimpleName() + ": {", "}")
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
        DeviceFaultBase that = (DeviceFaultBase) o;
        return dig.equals(that.dig) &&
                transactionStatus.equals(that.transactionStatus) &&
                errorSeverity.equals(that.errorSeverity) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData) &&
                suppliesStatus.equals(that.suppliesStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dig, transactionStatus, errorSeverity, diagnosticStatus, completionData, suppliesStatus);
    }
}
