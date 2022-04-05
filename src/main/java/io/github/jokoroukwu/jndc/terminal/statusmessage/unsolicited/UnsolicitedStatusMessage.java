package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class UnsolicitedStatusMessage<V extends UnsolicitedStatusInformation> extends TerminalOriginatedMessage {
    public static final String COMMAND_NAME = TerminalMessageClass.UNSOLICITED + ": " + TerminalMessageSubClass.STATUS_MESSAGE;
    private final Luno luno;
    private final V statusInformation;

    public UnsolicitedStatusMessage(Luno luno, V statusInformation) {
        super(TerminalMessageClass.UNSOLICITED, TerminalMessageSubClass.STATUS_MESSAGE);
        this.luno = ObjectUtils.validateNotNull(luno, "'LUNO'");
        this.statusInformation = ObjectUtils.validateNotNull(statusInformation, "'Status Information'");
    }

    public static <V extends UnsolicitedStatusInformation> UnsolicitedStatusMessageBuilder<V> builder() {
        return new UnsolicitedStatusMessageBuilder<>();
    }

    public UnsolicitedStatusMessageBuilder<V> copy() {
        return new UnsolicitedStatusMessageBuilder<V>()
                .withLuno(luno)
                .withStatusInformation(statusInformation);
    }

    public Luno getLuno() {
        return luno;
    }

    public V getStatusInformation() {
        return statusInformation;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(128)
                .append(super.toNdcString())
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendFs()
                .appendComponent(statusInformation)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UnsolicitedStatusMessage.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + messageClass)
                .add("messageSubclass: " + messageSubclass)
                .add("luno: " + luno)
                .add("statusInformation: " + statusInformation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnsolicitedStatusMessage<?> that = (UnsolicitedStatusMessage<?>) o;
        return luno.equals(that.luno) && statusInformation.equals(that.statusInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(luno, statusInformation);
    }

}
