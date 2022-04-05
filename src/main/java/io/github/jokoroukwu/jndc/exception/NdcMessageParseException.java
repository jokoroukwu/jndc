package io.github.jokoroukwu.jndc.exception;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class NdcMessageParseException extends NdcException {

    public NdcMessageParseException(String message, Object... args) {
        super(message, args);
    }

    public NdcMessageParseException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public static NdcMessageParseException withMessage(String commandName, String commandPostfix, String fieldName, String message, String buffer) {
        final String composedMessage = new ErrorMessageBuilder()
                .withCommandName(commandName)
                .withCommandPostfix(commandPostfix)
                .withFieldName(fieldName)
                .withErrorMessage(message)
                .withMessageBuffer(buffer)
                .toString();
        return new NdcMessageParseException(composedMessage);
    }

    public static NdcMessageParseException withMessage(String commandName, String fieldName, String message, NdcCharBuffer buffer) {
        return withMessage(commandName, Strings.EMPTY_STRING, fieldName, message, buffer.toString());
    }

    public static NdcMessageParseException withMessage(String fieldName, String message, NdcCharBuffer buffer) {
        return withMessage(Strings.EMPTY_STRING, Strings.EMPTY_STRING, fieldName, message, buffer.toString());
    }

    public static NdcMessageParseException withComposedMessage(String commandName, String fieldName, String message) {
        return withMessage(commandName, Strings.EMPTY_STRING, fieldName, message, Strings.EMPTY_STRING);
    }

    public static NdcMessageParseException withFieldName(String fieldName, String message) {
        return withMessage(Strings.EMPTY_STRING, Strings.EMPTY_STRING, fieldName, message, Strings.EMPTY_STRING);
    }

    public static NdcMessageParseException withFieldName(String fieldName, String errorMessage, NdcCharBuffer ndcCharBuffer) {
        return withMessage(Strings.EMPTY_STRING, Strings.EMPTY_STRING, fieldName, errorMessage, ndcCharBuffer.toString());
    }

    public static NdcMessageParseException withCommandName(String commandName, String message) {
        return withMessage(commandName, Strings.EMPTY_STRING, Strings.EMPTY_STRING, message, Strings.EMPTY_STRING);
    }

    public static NdcMessageParseException withCommandName(String commandName, String message, NdcCharBuffer ndcCharBuffer) {
        return withMessage(commandName, Strings.EMPTY_STRING, Strings.EMPTY_STRING, message, ndcCharBuffer.toString());
    }

    public static void onMessageParseError(String command, String message, NdcCharBuffer buffer) {
        throw withMessage(command, Strings.EMPTY_STRING, Strings.EMPTY_STRING, message, buffer.toString());
    }

    public static void onMessageParseError(String command, String message) {
        throw withMessage(command, Strings.EMPTY_STRING, Strings.EMPTY_STRING, message, Strings.EMPTY_STRING);
    }

    public static void onFieldParseError(String commandName, String fieldName, String message, String buffer) {
        throw withMessage(commandName, "failed to parse field", fieldName, message, buffer);
    }

    public static void onFieldParseError(String commandName, String fieldName, String message, NdcCharBuffer buffer) {
        onFieldParseError(commandName, fieldName, message, buffer.toString());
    }

    public static void onFieldParseError(String commandName, String fieldName, String message) {
        onFieldParseError(commandName, fieldName, message, Strings.EMPTY_STRING);
    }

    public static void onFieldParseError(String fieldName, String message, NdcCharBuffer buffer) {
        onFieldParseError(Strings.EMPTY_STRING, fieldName, message, buffer.toString());
    }

    public static void onNoFieldSeparator(String commandName, String fieldName, String message, NdcCharBuffer buffer) {
        throw withMessage(commandName, "missing field separator before field", fieldName, message, buffer.toString());
    }

    public static void onNoFieldSeparator(String fieldName, String message, NdcCharBuffer buffer) {
        throw withMessage(Strings.EMPTY_STRING, "missing field separator before field", fieldName, message, buffer.toString());
    }

    public static void onNoGroupSeparator(String commandName, String fieldName, String message, NdcCharBuffer buffer) {
        throw withMessage(commandName, "missing group separator before field", fieldName, message, buffer.toString());
    }

    public static void onNoGroupSeparator(String fieldName, String message, NdcCharBuffer buffer) {
        throw withMessage(Strings.EMPTY_STRING, "missing group separator before field", fieldName, message, buffer.toString());
    }

    public static NdcMessageParseException withMessage(String message, NdcCharBuffer buffer) {
        return withMessage(Strings.EMPTY_STRING, Strings.EMPTY_STRING, Strings.EMPTY_STRING, message, buffer.toString());
    }
}
