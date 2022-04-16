package io.github.jokoroukwu.jndc.central.terminalcommand;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.terminalcommand.commandcode.*;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.SequenceNumber;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class TerminalCommandAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String COMMAND_MODIFIER_FIELD = "Command Modifier";

    private final TerminalCommandListener terminalCommandListener;

    public TerminalCommandAppender(TerminalCommandListener terminalCommandListener) {
        this.terminalCommandListener = ObjectUtils.validateNotNull(terminalCommandListener, "terminalCommandListener");
    }

    @Override
    public void appendComponent(NdcCharBuffer messageStream, CentralMessageMeta stateObject) {
        final String messageName = CentralMessageClass.TERMINAL_COMMAND.toString();

        final long messageSequenceNumber = SequenceNumber.tryReadSequenceNumber(messageStream)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(messageName, "Message Sequence Number",
                        errorMessage, messageStream));

        messageStream.trySkipFieldSeparator()
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(messageName, "Command Code",
                        errorMessage, messageStream));
        final CommandCode commandCode = messageStream.tryReadNextChar()
                .flatMapToObject(CommandCode::forValue)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(messageName, "Command Code",
                        errorMessage, messageStream));

        final CommandModifier commandModifier = readCommandModifier(messageStream, commandCode);

        final TerminalCommand terminalCommand = TerminalCommand.builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withMessageSequenceNumber(messageSequenceNumber)
                .withCommandCode(commandCode)
                .withCommandModifier(commandModifier)
                .build();

        terminalCommandListener.onTerminalCommand(terminalCommand);
    }

    private CommandModifier readCommandModifier(NdcCharBuffer messageStream, CommandCode commandCode) {
        switch (commandCode) {
            case SEND_TALLY_INFO:
            case SEND_ERROR_LOG_INFO: {
                //  command modifier is mandatory
                return messageStream.tryReadInt(1)
                        .mapToObject(BaseCommandModifier::new)
                        .ifEmpty(errorMessage -> throwNoCommandModifierException(commandCode, errorMessage, messageStream))
                        .get();
            }
            case SEND_CONFIG_INFO: {
                //  command modifier is mandatory
                return messageStream.tryReadInt(1)
                        .flatMapToObject(SendConfigInfoModifier::forValue)
                        .ifEmpty(message -> throwNoCommandModifierException(commandCode, message, messageStream))
                        .get();
            }
            case SHUT_DOWN: {
                //  command modifier is optional
                if (messageStream.hasRemaining()) {
                    return messageStream.tryReadInt(1)
                            .flatMapToObject(ShutDownCommandModifier::forValue)
                            .ifEmpty(message -> NdcMessageParseException.onFieldParseError(TerminalCommand.COMMAND_NAME, COMMAND_MODIFIER_FIELD,
                                    message, messageStream))
                            .get();
                }
                return null;
            }
            case SEND_SUPPLY_COUNTERS: {
                //  command modifier is optional
                if (messageStream.hasRemaining(1)) {
                    messageStream.tryReadInt(1)
                            .flatMapToObject(SendSupplyCountersModifier::forValue)
                            .ifEmpty(message -> NdcMessageParseException.onFieldParseError(TerminalCommand.COMMAND_NAME, COMMAND_MODIFIER_FIELD,
                                    message, messageStream))
                            .get();
                }
                return null;
            }
            default: {
                //  the rest of command codes should have no command modifier
                if (messageStream.hasRemaining()) {
                    throwUnexpectedCommandModifierException(commandCode, messageStream);
                }
                return null;
            }
        }
    }

    private void throwNoCommandModifierException(CommandCode commandCode, String message, NdcCharBuffer messageStream) {
        var tagTrailer = String.format("must be present when command code is '%s': %s", commandCode, message);
        NdcMessageParseException.onFieldParseError(TerminalCommand.COMMAND_NAME, COMMAND_MODIFIER_FIELD, tagTrailer, messageStream);
    }

    private void throwUnexpectedCommandModifierException(CommandCode commandCode, NdcCharBuffer messageStream) {
        var tagPostfixMessage = String.format("must not be present when command code is '%s'", commandCode);
        NdcMessageParseException.onFieldParseError(TerminalCommand.COMMAND_NAME, COMMAND_MODIFIER_FIELD, tagPostfixMessage, messageStream);
    }

}
