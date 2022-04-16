package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class ErrorMessageBuilder {
    private String fieldName = Strings.EMPTY_STRING;
    private String commandName = Strings.EMPTY_STRING;
    private String commandPostfix = Strings.EMPTY_STRING;
    private String errorMessage = Strings.EMPTY_STRING;
    private String messageBuffer = Strings.EMPTY_STRING;

    public ErrorMessageBuilder withFieldName(String fieldName) {
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        return this;
    }

    public ErrorMessageBuilder withCommandName(String commandName) {
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
        return this;
    }

    public ErrorMessageBuilder withCommandPostfix(String commandPostfix) {
        this.commandPostfix = ObjectUtils.validateNotNull(commandPostfix, "commandPostfix");
        return this;
    }

    public ErrorMessageBuilder withErrorMessage(String errorMessage) {
        this.errorMessage = ObjectUtils.validateNotNull(errorMessage, "errorMessage");
        return this;
    }

    public ErrorMessageBuilder withMessageBuffer(String messageBuffer) {
        this.messageBuffer = ObjectUtils.validateNotNull(messageBuffer, "messageBuffer");
        return this;
    }

    public ErrorMessageBuilder withMessageBuffer(NdcCharBuffer messageBuffer) {
        if (messageBuffer != null) {
            withMessageBuffer(messageBuffer.toString());
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(messageBuffer.length() + commandName.length() + commandPostfix.length()
                + fieldName.length() + errorMessage.length());
        if (!commandName.isEmpty()) {
            builder.append(commandName)
                    .append(": ");
        }
        if (!commandPostfix.isEmpty()) {
            builder.append(commandPostfix)
                    .append(' ');
        }
        if (!fieldName.isEmpty()) {
            builder.append(fieldName)
                    .append(": ");
        }
        builder.append(errorMessage);
        if (!messageBuffer.isEmpty()) {
            builder.append(" in NDC message: ")
                    .append(messageBuffer);
        }
        return builder.toString();
    }
}
