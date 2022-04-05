package io.github.jokoroukwu.jndc.central.datacommand.customisationdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

abstract public class CustomisationDataCommand implements NdcComponent {
    protected final MessageId messageIdentifier;

    protected CustomisationDataCommand(MessageId messageIdentifier) {
        this.messageIdentifier = ObjectUtils.validateNotNull(messageIdentifier, "messageIdentifier");
    }

    public MessageId getMessageIdentifier() {
        return messageIdentifier;
    }

}
