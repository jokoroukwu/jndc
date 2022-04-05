package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionData;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatus;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.*;

public class GenericDeviceFault implements SolicitedStatusInformation, UnsolicitedStatusInformation {
    protected final Dig dig;
    protected final String deviceTransactionStatus;
    protected final List<ErrorSeverity> errorSeverities;
    protected final DiagnosticStatus diagnosticStatus;
    protected final CompletionData completionData;
    protected final List<SuppliesStatus> suppliesStatuses;
    protected final String additionalData;

    public GenericDeviceFault(Dig dig,
                              String deviceTransactionStatus,
                              Collection<ErrorSeverity> errorSeverities,
                              DiagnosticStatus diagnosticStatus,
                              CompletionData completionData,
                              Collection<SuppliesStatus> suppliesStatuses,
                              String additionalData) {
        this.dig = ObjectUtils.validateNotNull(dig, "'DIG'");
        this.deviceTransactionStatus = ObjectUtils.validateNotNull(deviceTransactionStatus, "'Transaction Status''");
        this.additionalData = ObjectUtils.validateNotNull(additionalData, "'Additional Data'");
        this.errorSeverities = List.copyOf(errorSeverities);
        this.suppliesStatuses = List.copyOf(suppliesStatuses);
        this.diagnosticStatus = diagnosticStatus;
        this.completionData = completionData;
    }

    public GenericDeviceFault(Dig dig,
                              String deviceTransactionStatus,
                              Collection<ErrorSeverity> errorSeverities,
                              DiagnosticStatus diagnosticStatus,
                              CompletionData completionData,
                              Collection<SuppliesStatus> suppliesStatuses) {
        this(dig, deviceTransactionStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatuses, Strings.EMPTY_STRING);
    }

    GenericDeviceFault(Dig dig,
                       String deviceTransactionStatus,
                       List<ErrorSeverity> errorSeverities,
                       DiagnosticStatus diagnosticStatus,
                       CompletionData completionData,
                       List<SuppliesStatus> suppliesStatuses,
                       String additionalData,
                       Void unused) {
        this.dig = dig;
        this.deviceTransactionStatus = deviceTransactionStatus;
        this.errorSeverities = errorSeverities;
        this.suppliesStatuses = suppliesStatuses;
        this.diagnosticStatus = diagnosticStatus;
        this.completionData = completionData;
        this.additionalData = additionalData;
    }

    public Dig getDig() {
        return dig;
    }

    public String getDeviceTransactionStatus() {
        return deviceTransactionStatus;
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

    public List<SuppliesStatus> getSuppliesStatuses() {
        return suppliesStatuses;
    }

    /**
     * Additional data should always be empty
     * in Solicited Messages.
     *
     * @return additionalData
     */
    public String getAdditionalData() {
        return additionalData;
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(128)
                .append(getDig().getValue());
        if (!suppliesStatuses.isEmpty() || !additionalData.isEmpty()) {
            return builder
                    .appendComponent(dig)
                    .append(deviceTransactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .appendComponent(NdcConstants.GROUP_SEPARATOR_STRING, completionData)
                    .appendFs()
                    .appendComponents(suppliesStatuses)
                    .append(additionalData)
                    .toString();
        }
        if (completionData != null) {
            return builder
                    .appendComponent(dig)
                    .append(deviceTransactionStatus)
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
                    .appendComponent(dig)
                    .append(deviceTransactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .appendFs()
                    .appendComponent(diagnosticStatus)
                    .toString();
        }
        if (!errorSeverities.isEmpty()) {
            return builder
                    .appendComponent(dig)
                    .append(deviceTransactionStatus)
                    .appendFs()
                    .appendComponents(errorSeverities)
                    .toString();
        }
        return builder
                .appendComponent(dig)
                .append(deviceTransactionStatus)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericDeviceFault.class.getSimpleName() + ": {", "}")
                .add("dig: " + dig)
                .add("deviceTransactionStatus: '" + deviceTransactionStatus + '\'')
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
        GenericDeviceFault that = (GenericDeviceFault) o;
        return dig == that.dig &&
                deviceTransactionStatus.equals(that.deviceTransactionStatus) &&
                additionalData.equals(that.additionalData) &&
                errorSeverities.equals(that.errorSeverities) &&
                Objects.equals(diagnosticStatus, that.diagnosticStatus) &&
                Objects.equals(completionData, that.completionData) &&
                suppliesStatuses.equals(that.suppliesStatuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dig, deviceTransactionStatus, errorSeverities, diagnosticStatus, completionData, suppliesStatuses, additionalData);
    }
}
