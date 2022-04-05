package io.github.jokoroukwu.jndc.terminal.meta;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class TerminalMessageMetaBase implements TerminalMessageMeta {
    private final TerminalMessageClass messageClass;
    private final TerminalMessageSubClass messageSubClass;

    public TerminalMessageMetaBase(TerminalMessageClass messageClass,
                                   TerminalMessageSubClass messageSubClass) {
        this.messageClass = ObjectUtils.validateNotNull(messageClass, "messageClass");
        this.messageSubClass = ObjectUtils.validateNotNull(messageSubClass, "messageSubClass");

    }

    public TerminalMessageClass getMessageClass() {
        return messageClass;
    }

    public TerminalMessageSubClass getMessageSubClass() {
        return messageSubClass;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TerminalMessageMetaBase.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + messageClass)
                .add("messageSubClass: " + messageSubClass)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalMessageMetaBase that = (TerminalMessageMetaBase) o;
        return messageClass == that.messageClass && messageSubClass == that.messageSubClass;

    }

    @Override
    public int hashCode() {
        return Objects.hash(messageClass, messageSubClass);
    }
}
