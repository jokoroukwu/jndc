package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;

import io.github.jokoroukwu.jndc.Luno;

public class EncryptorInitialisationDataBuilder<T extends EncryptorInformation> {
    private Luno luno = Luno.DEFAULT;
    private T encryptorInfo;

    public EncryptorInitialisationDataBuilder<T> withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public EncryptorInitialisationDataBuilder<T> withEncryptorInfo(T encryptorInfo) {
        this.encryptorInfo = encryptorInfo;
        return this;
    }

    public EncryptorInitialisationData<T> build() {
        return new EncryptorInitialisationData<T>(luno, encryptorInfo);
    }
}
