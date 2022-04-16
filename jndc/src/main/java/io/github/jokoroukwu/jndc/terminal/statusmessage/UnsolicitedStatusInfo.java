package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class UnsolicitedStatusInfo implements NdcComponent {
    public static final UnsolicitedStatusInfo EMPTY = new UnsolicitedStatusInfo(Strings.EMPTY_STRING, Strings.EMPTY_STRING);

    private final String dig;
    private final String deviceStatus;

    public UnsolicitedStatusInfo(String dig, String deviceStatus) {
        this.dig = ObjectUtils.validateNotNull(dig, "dig");
        this.deviceStatus = ObjectUtils.validateNotNull(deviceStatus, "deviceStatus");
    }

    public String getDig() {
        return dig;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add("dig: '" + dig + "'")
                .add("deviceStatus: '" + deviceStatus + "'")
                .toString();
    }

    @Override
    public String toNdcString() {
        return dig + deviceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnsolicitedStatusInfo that = (UnsolicitedStatusInfo) o;
        return dig.equals(that.dig) && deviceStatus.equals(that.deviceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dig, deviceStatus);
    }

    public static final class UnsolicitedStatusInfoBuilder {
        private String dig;
        private String deviceStatus;

        private UnsolicitedStatusInfoBuilder() {
        }

        public UnsolicitedStatusInfoBuilder setDig(String dig) {
            this.dig = ObjectUtils.validateNotNull(dig, "dig");
            return this;
        }

        public UnsolicitedStatusInfoBuilder setDeviceStatus(String deviceStatus) {
            this.deviceStatus = ObjectUtils.validateNotNull(deviceStatus, "deviceStatus");
            return this;
        }

        public UnsolicitedStatusInfo build() {
            return new UnsolicitedStatusInfo(dig, deviceStatus);
        }
    }
}
