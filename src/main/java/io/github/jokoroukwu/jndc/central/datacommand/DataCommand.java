package io.github.jokoroukwu.jndc.central.datacommand;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.ResponseFlag;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.CentralOriginatedMessage;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.StringJoiner;

public class DataCommand<V extends NdcComponent> extends CentralOriginatedMessage {
    private final DataCommandSubClass messageSubclass;
    private final char responseFlag;
    private final Luno luno;
    private final int messageSequenceNumber;
    private final V commandData;

    public DataCommand(char responseFlag, Luno luno, int messageSequenceNumber, DataCommandSubClass messageSubclass, V commandData) {
        super(CentralMessageClass.DATA_COMMAND);
        this.messageSubclass = ObjectUtils.validateNotNull(messageSubclass, "messageSubclass");
        this.luno = ObjectUtils.validateNotNull(luno, "luno");
        this.commandData = ObjectUtils.validateNotNull(commandData, "commandData");
        this.responseFlag = ResponseFlag.validateResponseFlag(responseFlag);
        this.messageSequenceNumber = Integers.validateRange(messageSequenceNumber, 0, 999, "Message Sequence Number");
    }

    public static <V extends NdcComponent> DataCommandBuilder<V> builder() {
        return new DataCommandBuilder<>();
    }

    public DataCommandBuilder<V> copy() {
        return new DataCommandBuilder<V>()
                .withMessageSubclass(messageSubclass)
                .withLuno(luno)
                .withResponseFlag(responseFlag)
                .withMessageSequenceNumber(messageSequenceNumber)
                .withCommandData(commandData);
    }

    public DataCommandSubClass getMessageSubclass() {
        return messageSubclass;
    }

    public char getResponseFlag() {
        return responseFlag;
    }

    public Luno getLuno() {
        return luno;
    }

    public OptionalLong getMessageSequenceNumber() {
        return messageSequenceNumber >= 0 ? OptionalLong.of(messageSequenceNumber) : OptionalLong.empty();
    }

    public V getCommandData() {
        return commandData;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataCommand.class.getSimpleName() + ": {", "}")
                .add("messageClass: '" + messageClass + "'")
                .add("messageSubclass: '" + messageSubclass + "'")
                .add("responseFlag: '" + responseFlag + "'")
                .add("LUNO: " + luno)
                .add("messageSequenceNumber: '" + messageSequenceNumber + "'")
                .add("commandData: " + commandData)
                .toString();
    }

    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(13);
        return builder
                .appendComponent(messageClass)
                .append(responseFlag)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendZeroPadded(messageSequenceNumber, 3)
                .appendFs()
                .appendComponent(messageSubclass)
                .appendComponent(commandData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataCommand<?> that = (DataCommand<?>) o;
        return ((messageSequenceNumber < 0 && that.messageSequenceNumber < 0) || messageSequenceNumber == that.messageSequenceNumber) &&
                messageClass == that.messageClass &&
                responseFlag == that.responseFlag &&
                messageSubclass == that.messageSubclass &&
                luno.equals(that.luno) &&
                commandData.equals(that.commandData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageClass, messageSubclass, responseFlag, luno, Math.max(messageSequenceNumber, -1),
                commandData);
    }

}
