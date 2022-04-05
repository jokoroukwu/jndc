package io.github.jokoroukwu.jndc.central;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public abstract class CentralOriginatedMessage implements NdcComponent {
    protected final CentralMessageClass messageClass;

    public CentralOriginatedMessage(CentralMessageClass messageClass) {
        this.messageClass = ObjectUtils.validateNotNull(messageClass, "messageClass");
    }

    public CentralMessageClass getMessageClass() {
        return messageClass;
    }
}
