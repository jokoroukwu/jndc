package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class EncryptorInitialisationData<V extends EncryptorInformation> implements TerminalOriginatedMessage {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED
            + ": " + TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA;

    private final Luno luno;
    private final V encryptorInfo;

    public EncryptorInitialisationData(Luno luno, V encryptorInfo) {
        this.luno = ObjectUtils.validateNotNull(luno, "'LUNO'");
        this.encryptorInfo = ObjectUtils.validateNotNull(encryptorInfo, "encryptorInfo");
    }

    public static <V extends EncryptorInformation> EncryptorInitialisationDataBuilder<V> builder() {
        return new EncryptorInitialisationDataBuilder<>();
    }

    public EncryptorInitialisationDataBuilder<V> copy() {
        return new EncryptorInitialisationDataBuilder<V>()
                .withLuno(luno)
                .withEncryptorInfo(encryptorInfo);
    }

    @Override
    public TerminalMessageClass getMessageClass() {
        return TerminalMessageClass.SOLICITED;
    }

    @Override
    public TerminalMessageSubClass getMessageSubclass() {
        return TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA;
    }

    public Luno getLuno() {
        return luno;
    }

    public V getEncryptorInfo() {
        return encryptorInfo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EncryptorInitialisationData.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + TerminalMessageClass.SOLICITED)
                .add("messageSubclass: " + TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA)
                .add("luno: " + luno)
                .add("encryptorInfo: " + encryptorInfo)
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(128)
                .appendComponent(TerminalMessageClass.SOLICITED)
                .appendComponent(TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendFs()
                .appendComponent(encryptorInfo)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptorInitialisationData<?> that = (EncryptorInitialisationData<?>) o;
        return luno.equals(that.luno) && encryptorInfo.equals(that.encryptorInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(luno, encryptorInfo);
    }

}
