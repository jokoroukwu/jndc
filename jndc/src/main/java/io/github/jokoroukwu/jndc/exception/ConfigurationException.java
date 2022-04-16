package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.util.text.Strings;

public class ConfigurationException extends NdcException {
    public ConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public static ConfigurationException withMessage(String commandName, String fieldName, String errorMessage) {
        final String composedMessage = new ErrorMessageBuilder()
                .withCommandName(commandName)
                .withFieldName(fieldName)
                .withErrorMessage(errorMessage)
                .toString();
        return new ConfigurationException(composedMessage);
    }

    public static ConfigurationException withMessage(String commandName, String errorMessage) {
        return withMessage(commandName, Strings.EMPTY_STRING, errorMessage);
    }

    public static void onConfigError(String commandName, String errorMessage) {
        throw withMessage(commandName, Strings.EMPTY_STRING, errorMessage);
    }
    public static void onConfigError(String errorMessage) {
        throw new ConfigurationException(errorMessage);
    }
}
