package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class PrimaryAidTerminalActionCodeDenialAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Primary AID Terminal Action Code - Denial";

    public PrimaryAidTerminalActionCodeDenialAppender(TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
    }

    public PrimaryAidTerminalActionCodeDenialAppender() {
        super(new DataObjectsAppender(DataObjectsAppender.TRANSACTION_RQ_OBJECTS,
                TerminalApplicationIdEntryBuilder::withTransactionRqDataObjects,
                new DataObjectsAppender(DataObjectsAppender.COMPLETION_DATA_OBJECTS,
                        TerminalApplicationIdEntryBuilder::withCompletionDataObjects, new SecondaryAIDsAppender())));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadHexLong(10)
                .resolve(stateObject::withPrimaryAidActionCodeDenial,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
