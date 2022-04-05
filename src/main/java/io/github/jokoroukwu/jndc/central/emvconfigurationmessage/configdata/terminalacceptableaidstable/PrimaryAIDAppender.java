package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.IccTerminalAcceptableAppIdsTable.COMMAND_NAME;
import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.onFieldParseError;

public class PrimaryAIDAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Primary AID";
    private final AidReader aidReader;

    public PrimaryAIDAppender(TerminalAcceptableAidsFieldAppender nextReader, AidReader aidReader) {
        super(nextReader);
        this.aidReader = ObjectUtils.validateNotNull(aidReader, "aidReader");
    }

    public PrimaryAIDAppender() {
        this(new DefaultAppLabelAppender(), BaseAidReader.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        aidReader.readAid(ndcCharBuffer)
                .resolve(stateObject::withPrimaryAid,
                        errorMessage -> onFieldParseError(COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
        callNextAppender(ndcCharBuffer, stateObject);
    }

}
