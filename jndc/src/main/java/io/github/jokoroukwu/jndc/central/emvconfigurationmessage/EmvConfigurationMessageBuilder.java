package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;

public class EmvConfigurationMessageBuilder<V extends ConfigurationData> {
    protected char responseFlag = NdcConstants.NULL_CHAR;
    protected Luno luno = Luno.DEFAULT;
    protected V configurationData;
    protected String mac = Strings.EMPTY_STRING;


    public EmvConfigurationMessageBuilder<V> withResponseFlag(char responseFlag) {
        this.responseFlag = responseFlag;
        return this;
    }

    public EmvConfigurationMessageBuilder<V> withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }


    public EmvConfigurationMessageBuilder<V> withConfigurationData(V configurationData) {
        this.configurationData = configurationData;
        return this;
    }

    public EmvConfigurationMessageBuilder<V> withMac(String mac) {
        this.mac = mac;
        return this;
    }

    public EmvConfigurationMessage<V> build() {
        return new EmvConfigurationMessage<V>(responseFlag, luno, configurationData, mac);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmvConfigurationMessageBuilder<?> that = (EmvConfigurationMessageBuilder<?>) o;
        return responseFlag == that.responseFlag &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(configurationData, that.configurationData) &&
                Objects.equals(mac, that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseFlag, luno, configurationData, mac);
    }
}
