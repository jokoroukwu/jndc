package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class EncryptorInitialisationData<V extends EncryptorInformation> extends TerminalOriginatedMessage {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " + TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA;
    private final Luno luno;
    private final V encryptorInfo;

    public EncryptorInitialisationData(Luno luno, V encryptorInfo) {
        super(TerminalMessageClass.SOLICITED, TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA);
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

    public Luno getLuno() {
        return luno;
    }

    public V getEncryptorInfo() {
        return encryptorInfo;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(128)
                .append(super.toNdcString())
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendFs()
                .appendComponent(encryptorInfo)
                .toString();
    }
}
