package io.github.jokoroukwu.jndc.terminal.meta;

import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;

public interface TerminalMessageMeta {
    TerminalMessageClass getMessageClass();

    TerminalMessageSubClass getMessageSubClass();
}
