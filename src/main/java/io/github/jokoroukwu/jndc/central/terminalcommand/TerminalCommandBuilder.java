package io.github.jokoroukwu.jndc.central.terminalcommand;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.terminalcommand.commandcode.CommandCode;
import io.github.jokoroukwu.jndc.central.terminalcommand.commandcode.CommandModifier;

import java.util.Objects;
import java.util.StringJoiner;

public final class TerminalCommandBuilder {
    private char responseFlag = '0';
    private Luno luno = Luno.DEFAULT;
    private long messageSequenceNumber = -1;
    private CommandCode commandCode;
    private CommandModifier commandModifier;

    public TerminalCommandBuilder withResponseFlag(char responseFlag) {
        this.responseFlag = responseFlag;
        return this;
    }

    public TerminalCommandBuilder withLuno(Luno luno) {
        this.luno = luno;
        return this;
    }

    public TerminalCommandBuilder withMessageSequenceNumber(long messageSequenceNumber) {
        this.messageSequenceNumber = messageSequenceNumber;
        return this;
    }


    public TerminalCommandBuilder withCommandCode(CommandCode commandCode) {
        this.commandCode = commandCode;
        return this;
    }

    public TerminalCommandBuilder withCommandModifier(CommandModifier commandModifier) {
        this.commandModifier = commandModifier;
        return this;
    }

    public TerminalCommand build() {
        return new TerminalCommand(
                responseFlag,
                luno,
                messageSequenceNumber,
                commandCode,
                commandModifier
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TerminalCommandBuilder.class.getSimpleName() + ": {", "}")
                .add("responseFlag: " + responseFlag)
                .add("luno: " + luno)
                .add("messageSequenceNumber: " + messageSequenceNumber)
                .add("commandCode: " + commandCode)
                .add("commandModifier: " + commandModifier)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminalCommandBuilder that = (TerminalCommandBuilder) o;
        return responseFlag == that.responseFlag &&
                messageSequenceNumber == that.messageSequenceNumber &&
                commandCode == that.commandCode &&
                Objects.equals(luno, that.luno) &&
                Objects.equals(commandModifier, that.commandModifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseFlag, luno, messageSequenceNumber, commandCode, commandModifier);
    }
}
