package io.github.jokoroukwu.jndc.central.emvconfigurationmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.ResponseFlag;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.CentralOriginatedMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.ConfigurationData;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.StringJoiner;

public class EmvConfigurationMessage<V extends ConfigurationData> extends CentralOriginatedMessage {
    public static final String COMMAND_NAME = CentralMessageClass.EMV_CONFIGURATION.toString();

    protected final char responseFlag;
    protected final Luno luno;
    protected final String mac;
    private final V configurationData;

    protected EmvConfigurationMessage(char responseFlag, Luno luno, V configurationData, String mac) {
        super(CentralMessageClass.EMV_CONFIGURATION);
        this.responseFlag = ResponseFlag.validateResponseFlag(responseFlag);
        this.luno = ObjectUtils.validateNotNull(luno, "LUNO");
        this.configurationData = ObjectUtils.validateNotNull(configurationData, "'Configuration Data'");
        this.mac = MacReaderBase.validateMac(mac);
    }

    public static <V extends ConfigurationData> EmvConfigurationMessageBuilder<V> builder() {
        return new EmvConfigurationMessageBuilder<>();
    }

    public EmvConfigurationMessageBuilder<V> copy() {
        return new EmvConfigurationMessageBuilder<V>()
                .withResponseFlag(responseFlag)
                .withLuno(luno)
                .withConfigurationData(configurationData)
                .withMac(mac);
    }

    public OptionalInt getResponseFlag() {
        return responseFlag != NdcConstants.NULL_CHAR ? OptionalInt.of(responseFlag) : OptionalInt.empty();
    }

    public Luno getLuno() {
        return luno;
    }


    public V getConfigurationData() {
        return configurationData;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public final String toNdcString() {
        final String configDataString = configurationData.toNdcString();
        return new NdcStringBuilder(16 + configDataString.length() + mac.length())
                .appendComponent(messageClass)
                .append(responseFlag)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .append(configDataString)
                .appendFieldGroup(mac)
                .toString();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", EmvConfigurationMessage.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + messageClass)
                .add("responseFlag: '" + responseFlag + "'")
                .add("luno: " + luno)
                .add("configurationData: " + configurationData)
                .add("MAC: '" + mac + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmvConfigurationMessage<?> that = (EmvConfigurationMessage<?>) o;
        return responseFlag == that.responseFlag &&
                mac.equals(that.mac) &&
                luno.equals(that.luno) &&
                configurationData.equals(that.configurationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageClass, responseFlag, luno, configurationData, mac);
    }

}
