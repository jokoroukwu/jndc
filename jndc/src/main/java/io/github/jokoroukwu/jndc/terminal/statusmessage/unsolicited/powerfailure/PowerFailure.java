package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public final class PowerFailure implements DeviceStatusInformation, UnsolicitedStatusInformation {
    public static final java.lang.String COMMAND_NAME = TerminalMessageClass.UNSOLICITED
            + ": " + TerminalMessageSubClass.STATUS_MESSAGE
            + ": " + Dig.COMMUNICATIONS;

    private final int configurationId;

    public PowerFailure(int configurationId) {
        this.configurationId = Integers.validateRange(configurationId, 0, 9999, "'Configuration ID'");
    }

    PowerFailure(int configurationId, Void unused) {
        this.configurationId = configurationId;
    }

    public int getConfigurationId() {
        return configurationId;
    }

    @Override
    public Dig getDig() {
        return Dig.COMMUNICATIONS;
    }

    @Override
    public java.lang.String toNdcString() {
        return Dig.COMMUNICATIONS.toNdcString() + Integers.toZeroPaddedString(configurationId, 4);
    }

    @Override
    public java.lang.String toString() {
        return new StringJoiner(", ", PowerFailure.class.getSimpleName() + ": {", "}")
                .add("dig: " + Dig.COMMUNICATIONS)
                .add("configurationId: " + configurationId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerFailure that = (PowerFailure) o;
        return configurationId == that.configurationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Dig.COMMUNICATIONS, configurationId);
    }
}
