package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.NdcComponent;

public interface TerminalOriginatedMessage extends NdcComponent {

    TerminalMessageClass getMessageClass();

    TerminalMessageSubClass getMessageSubclass();
}
