package io.github.jokoroukwu.jndc.central.terminalcommand;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.ResponseFlag;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.CentralOriginatedMessage;
import io.github.jokoroukwu.jndc.central.terminalcommand.commandcode.CommandCode;
import io.github.jokoroukwu.jndc.central.terminalcommand.commandcode.CommandModifier;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class TerminalCommand extends CentralOriginatedMessage {
    public static final String COMMAND_NAME = "Terminal Command";
    private final char responseFlag;
    private final Luno luno;
    private final long messageSequenceNumber;
    private final CommandCode commandCode;
    private final CommandModifier commandModifier;

    public TerminalCommand(char responseFlag, Luno luno, long messageSequenceNumber, CommandCode commandCode, CommandModifier commandModifier) {
        super(CentralMessageClass.TERMINAL_COMMAND);
        this.luno = ObjectUtils.validateNotNull(luno, "'LUNO'");
        this.commandCode = ObjectUtils.validateNotNull(commandCode, "'Command code'");
        this.responseFlag = ResponseFlag.validateResponseFlag(responseFlag);
        this.commandModifier = commandModifier;
        this.messageSequenceNumber = messageSequenceNumber;
    }

    public static TerminalCommandBuilder builder() {
        return new TerminalCommandBuilder();
    }

    public TerminalCommandBuilder copy() {
        return new TerminalCommandBuilder()
                .withResponseFlag(responseFlag)
                .withLuno(luno)
                .withMessageSequenceNumber(messageSequenceNumber)
                .withCommandCode(commandCode)
                .withCommandModifier(commandModifier);
    }

    public OptionalInt getResponseFlag() {
        return responseFlag != NdcConstants.NULL_CHAR ? OptionalInt.of(responseFlag) : OptionalInt.empty();
    }

    public Luno getLuno() {
        return luno;
    }

    public OptionalLong getMessageSequenceNumber() {
        return messageSequenceNumber >= 0 ? OptionalLong.of(messageSequenceNumber) : OptionalLong.empty();
    }

    public CommandCode getCommandCode() {
        return commandCode;
    }

    public Optional<CommandModifier> getCommandModifier() {
        return Optional.ofNullable(commandModifier);
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder()
                .appendComponent(messageClass)
                .append(responseFlag)
                .appendFs()
                .appendComponent(luno)
                .appendFs()
                .appendZeroPadded(messageSequenceNumber, 3)
                .appendFs()
                .append(commandCode.value())
                .append(commandModifier.value())
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TerminalCommand.class.getSimpleName() + ": {", "}")
                .add("messageClass: " + messageClass)
                .add("responseFlag: '" + responseFlag + "'")
                .add("LUNO: " + luno)
                .add("messageSequenceNumber: '" + messageSequenceNumber + "'")
                .add("commandCode: '" + commandCode + "'")
                .add("commandModifier: '" + commandModifier + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalCommand that = (TerminalCommand) o;
        return ((messageSequenceNumber < 0 && that.messageSequenceNumber < 0) || messageSequenceNumber == that.messageSequenceNumber) &&
                responseFlag == that.responseFlag &&
                messageClass == that.messageClass &&
                commandCode == that.commandCode &&
                commandModifier == that.commandModifier &&
                luno.equals(that.luno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageClass, responseFlag, luno, Math.max(messageSequenceNumber, -1), commandCode,
                commandModifier);
    }

}
