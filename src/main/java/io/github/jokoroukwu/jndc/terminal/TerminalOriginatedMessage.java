package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

abstract public class TerminalOriginatedMessage implements NdcComponent {
    protected final TerminalMessageClass messageClass;
    protected final TerminalMessageSubClass messageSubclass;

    public TerminalOriginatedMessage(TerminalMessageClass messageClass, TerminalMessageSubClass messageSubclass) {
        this.messageClass = ObjectUtils.validateNotNull(messageClass, "messageClass");
        this.messageSubclass = ObjectUtils.validateNotNull(messageSubclass, "messageSubclass");
    }

    public TerminalMessageClass getMessageClass() {
        return messageClass;
    }

    public TerminalMessageSubClass getMessageSubclass() {
        return messageSubclass;
    }

    @Override
    public String toNdcString() {
        return messageClass.toNdcString() + messageSubclass.toNdcString();
    }
}
