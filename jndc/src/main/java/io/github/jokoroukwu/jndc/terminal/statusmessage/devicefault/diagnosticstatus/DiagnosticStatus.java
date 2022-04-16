package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class DiagnosticStatus implements NdcComponent {
    private final int mainErrorStatus;
    private final String diagnosticInfo;

    public DiagnosticStatus(int mainErrorStatus, String diagnosticInfo) {
        this.mainErrorStatus = Integers.validateRange(mainErrorStatus, 0, 99, "M-Status");
        this.diagnosticInfo = validateData(diagnosticInfo);
    }

    DiagnosticStatus(int mainErrorStatus, String diagnosticInfo, Void unused) {
        this.mainErrorStatus = mainErrorStatus;
        this.diagnosticInfo = diagnosticInfo;
    }

    public int getMainErrorStatus() {
        return mainErrorStatus;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }

    private String validateData(String value) {
        ObjectUtils.validateNotNull(value, "Diagnostic Information");
        Integers.validateIsEven(value.length(), "Diagnostic Information");
        Strings.validateIsHex(value, "Diagnostic Information");
        return value;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(2 + diagnosticInfo.length())
                .appendZeroPadded(mainErrorStatus, 2)
                .append(diagnosticInfo)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiagnosticStatus.class.getSimpleName() + ": {", "}")
                .add("mainErrorStatus: " + mainErrorStatus)
                .add("diagnosticInfo: '" + diagnosticInfo + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticStatus that = (DiagnosticStatus) o;
        return mainErrorStatus == that.mainErrorStatus && diagnosticInfo.equals(that.diagnosticInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainErrorStatus, diagnosticInfo);
    }
}
