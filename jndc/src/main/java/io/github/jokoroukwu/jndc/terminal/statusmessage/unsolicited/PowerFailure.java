package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited;

import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFault;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public final class PowerFailure implements DeviceFault, UnsolicitedStatusInformation {
    private final int configurationId;

    public PowerFailure(int configurationId) {
        this.configurationId = Integers.validateRange(configurationId, 0, 9999, "'Configuration ID'");
    }

    public int getConfigurationId() {
        return configurationId;
    }

    @Override
    public Dig getDig() {
        return Dig.COMMUNICATIONS;
    }

    @Override
    public String toNdcString() {
        return Dig.COMMUNICATIONS.toNdcString() + Integers.toZeroPaddedString(configurationId, 4);
    }

    @Override
    public String toString() {
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
