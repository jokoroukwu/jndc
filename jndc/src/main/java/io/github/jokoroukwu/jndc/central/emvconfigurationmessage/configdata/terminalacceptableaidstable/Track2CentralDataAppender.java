package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class Track2CentralDataAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Track 2 Data for Central";

    public Track2CentralDataAppender(TerminalAcceptableAidsFieldAppender nextAppender) {
        super(nextAppender);
    }

    public Track2CentralDataAppender() {
        super(new Track2ICCDataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadInt(2)
                .flatMapToObject(Track2CentralData::forValue)
                .resolve(stateObject::withTrack2CentralData,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextIfFieldDataRemains(ndcCharBuffer, stateObject);
    }
}
