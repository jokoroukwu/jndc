package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.dig.Dig;
import io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor;

public interface DeviceFault extends NdcComponent {
    String COMMAND_NAME = TerminalMessageClass.SOLICITED + ": " + TerminalMessageSubClass.STATUS_MESSAGE + ": "
            + StatusDescriptor.DEVICE_FAULT;

    Dig getDig();
}
