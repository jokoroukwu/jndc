package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class AppSelectionIndicatorAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String FIELD_NAME = "Application Selection Indicator";

    public AppSelectionIndicatorAppender(TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
    }

    public AppSelectionIndicatorAppender() {
        super(new Track2CentralDataAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadInt(2)
                .flatMapToObject(AppSelectionIndicator::forValue)
                .resolve(stateObject::withAppSelectionIndicator,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));

        callNextIfFieldDataRemains(ndcCharBuffer, stateObject);
    }

}
