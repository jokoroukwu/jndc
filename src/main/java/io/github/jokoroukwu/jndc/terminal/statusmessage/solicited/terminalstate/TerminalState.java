package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;

public interface TerminalState extends SolicitedStatusInformation {
    String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " + StatusDescriptor.TERMINAL_STATE;

    TerminalStateMessageId getMessageId();
}
