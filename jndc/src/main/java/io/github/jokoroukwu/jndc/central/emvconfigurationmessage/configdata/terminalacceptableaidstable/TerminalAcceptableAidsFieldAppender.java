package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;

public abstract class TerminalAcceptableAidsFieldAppender extends ChainedNdcComponentAppender<TerminalApplicationIdEntryBuilder> {

    protected TerminalAcceptableAidsFieldAppender(NdcComponentAppender<TerminalApplicationIdEntryBuilder> nextAppender) {
        super(nextAppender);
    }

    protected final void callNextIfFieldDataRemains(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder collector) {
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            callNextAppender(ndcCharBuffer, collector);
        }
    }
}
