package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class EntryNumberAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String ENTRY_NUMBER_FIELD = "Entry Number";

    public EntryNumberAppender(TerminalAcceptableAidsFieldAppender nextAppender) {
        super(nextAppender);
    }

    public EntryNumberAppender() {
        this(new PrimaryAIDAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadHexInt(2)
                .resolve(stateObject::withEntryNumber,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, ENTRY_NUMBER_FIELD, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject);
    }
}
