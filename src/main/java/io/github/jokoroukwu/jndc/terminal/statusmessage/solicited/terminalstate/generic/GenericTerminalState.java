package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.generic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalState;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.TerminalStateMessageId;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class GenericTerminalState implements TerminalState {
    private final TerminalStateMessageId messageId;
    private final String data;

    public GenericTerminalState(TerminalStateMessageId messageId, String data) {
        this.messageId = ObjectUtils.validateNotNull(messageId, "messageId");
        this.data = ObjectUtils.validateNotNull(data, "data");
    }

    GenericTerminalState(TerminalStateMessageId messageId, String data, Void unused) {
        this.messageId = messageId;
        this.data = data;
    }

    @Override
    public String toNdcString() {
        return messageId.toNdcString() + data;
    }

    @Override
    public TerminalStateMessageId getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericTerminalState.class.getSimpleName() + ": {", "}")
                .add("messageId: " + messageId)
                .add("data: '" + data + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericTerminalState that = (GenericTerminalState) o;
        return messageId == that.messageId && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, data);
    }
}
