package io.github.jokoroukwu.jndc.central.datacommand;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcComponent;

public final class DataCommandBuilder<T extends NdcComponent> {
    private DataCommandSubClass messageSubclass;
    private char responseFlag;
    private Luno luno;
    private int messageSequenceNumber;
    private T commandData;

    public DataCommandSubClass getMessageSubclass() {
        return messageSubclass;
    }

    public DataCommandBuilder<T> withMessageSubclass(DataCommandSubClass messageSubclass) {
        this.messageSubclass = messageSubclass;
        return this;
    }

    public char getResponseFlag() {
        return responseFlag;
    }

    public DataCommandBuilder<T> withResponseFlag(char responseFlag) {
        this.responseFlag = responseFlag;
        return this;
    }

    public Luno getLuno() {
        return luno;
    }

    public DataCommandBuilder<T> withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public int getMessageSequenceNumber() {
        return messageSequenceNumber;
    }

    public DataCommandBuilder<T> withMessageSequenceNumber(int messageSequenceNumber) {
        this.messageSequenceNumber = messageSequenceNumber;
        return this;
    }

    public T getCommandData() {
        return commandData;
    }

    public DataCommandBuilder<T> withCommandData(T commandData) {
        this.commandData = commandData;
        return this;
    }

    public DataCommand<T> build() {
        return new DataCommand<>(responseFlag, luno, messageSequenceNumber, messageSubclass, commandData);
    }
}
