package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class PrimaryAidICCAppTypeAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Primary AID ICC Application Type";

    public PrimaryAidICCAppTypeAppender(TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
    }

    public PrimaryAidICCAppTypeAppender() {
        super(new PrimaryAIDAppVersionNumberAppender(PrimaryAIDAppVersionNumberAppender.PRIMARY_AID_LOWEST_APP_VERSION_NUM,
                TerminalApplicationIdEntryBuilder::withPrimaryAidLowestAppVersion,
                new PrimaryAIDAppVersionNumberAppender(PrimaryAIDAppVersionNumberAppender.PRIMARY_AID_HIGHEST_APP_VERSION_NUM,
                        TerminalApplicationIdEntryBuilder::withPrimaryAidHighestAppVersion,
                        new PrimaryAidTerminalActionCodeDenialAppender())));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadCharSequence(3)
                .resolve(stateObject::withPrimaryAidIccAppType,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
