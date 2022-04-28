package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited;

import io.github.jokoroukwu.jndc.Luno;

public final class UnsolicitedStatusMessageBuilder<T extends UnsolicitedStatusInformation> {
    private Luno luno;
    private T statusInformation;

    public UnsolicitedStatusMessageBuilder<T> withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public UnsolicitedStatusMessageBuilder<T> withStatusInformation(T statusInformation) {
        this.statusInformation = statusInformation;
        return this;
    }

    public Luno getLuno() {
        return luno;
    }

    public T getStatusInformation() {
        return statusInformation;
    }

    public UnsolicitedStatusMessage<T> build() {
        return new UnsolicitedStatusMessage<>(luno, statusInformation);
    }
}
