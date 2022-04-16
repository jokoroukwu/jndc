package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class Track2ICCDataAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Track 2 Data To Be Used During ICC Transaction";

    public Track2ICCDataAppender(TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
    }

    public Track2ICCDataAppender() {
        super(new AdditionalTrack2DataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadInt(2)
                .flatMapToObject(Track2ICCData::forValue)
                .resolve(stateObject::withTrack2IccData,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
