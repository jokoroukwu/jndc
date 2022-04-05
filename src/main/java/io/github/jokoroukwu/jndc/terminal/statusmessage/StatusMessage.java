package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.TerminalOriginatedMessage;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

abstract public class StatusMessage<T extends NdcComponent> extends TerminalOriginatedMessage {
    private final T statusInformation;

    public StatusMessage(TerminalMessageSubClass messageSubclass, Luno luno, T statusInformation) {
        super(TerminalMessageClass.SOLICITED, messageSubclass);
        this.statusInformation = ObjectUtils.validateNotNull(statusInformation, "statusInformation");
    }

    public T getStatusInformation() {
        return statusInformation;
    }
}
