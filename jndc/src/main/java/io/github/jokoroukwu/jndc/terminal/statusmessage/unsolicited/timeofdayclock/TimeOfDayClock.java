package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class TimeOfDayClock implements DeviceStatusInformation, UnsolicitedStatusInformation {
    public static final String COMMAND_NAME = TerminalMessageClass.UNSOLICITED
            + ": " + TerminalMessageSubClass.STATUS_MESSAGE
            + ": " + Dig.TIME_OF_DAY_CLOCK;

    private final ClockStatus deviceStatus;
    private final ErrorSeverity errorSeverity;

    public TimeOfDayClock(ClockStatus deviceStatus, ErrorSeverity errorSeverity) {
        this.deviceStatus = ObjectUtils.validateNotNull(deviceStatus, "'Device Status'");
        this.errorSeverity = validateErrorSeverity(errorSeverity);
    }

    TimeOfDayClock(ClockStatus deviceStatus, ErrorSeverity errorSeverity, Void unused) {
        this.deviceStatus = deviceStatus;
        this.errorSeverity = errorSeverity;
    }

    public ClockStatus getDeviceStatus() {
        return deviceStatus;
    }

    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(3)
                .appendComponent(Dig.TIME_OF_DAY_CLOCK)
                .appendComponent(deviceStatus)
                .appendFs()
                .appendComponent(errorSeverity)
                .toString();
    }

    @Override
    public Dig getDig() {
        return Dig.TIME_OF_DAY_CLOCK;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", TimeOfDayClock.class.getSimpleName() + ": {", "}")
                .add("deviceStatus: " + deviceStatus)
                .add("errorSeverity: " + errorSeverity)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfDayClock that = (TimeOfDayClock) o;
        return deviceStatus == that.deviceStatus && errorSeverity == that.errorSeverity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceStatus, errorSeverity);
    }

    private ErrorSeverity validateErrorSeverity(ErrorSeverity errorSeverity) {
        if (ErrorSeverity.WARNING == errorSeverity || ErrorSeverity.FATAL == errorSeverity) {
            return errorSeverity;
        }
        final String errorMessage = String.format("'Error Severity' should be %s or %s but was: %s", ErrorSeverity.WARNING,
                ErrorSeverity.FATAL, errorSeverity);
        throw new IllegalArgumentException(errorMessage);
    }
}
