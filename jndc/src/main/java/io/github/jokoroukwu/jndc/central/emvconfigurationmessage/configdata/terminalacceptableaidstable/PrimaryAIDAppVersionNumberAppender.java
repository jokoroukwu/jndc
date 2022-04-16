package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

import java.util.function.ObjIntConsumer;

public class PrimaryAIDAppVersionNumberAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String PRIMARY_AID_LOWEST_APP_VERSION_NUM = "Primary AID Lowest Application Version Number";
    public static final String PRIMARY_AID_HIGHEST_APP_VERSION_NUM = "Primary AID Highest Application Version Number";

    private final String fieldName;
    private final ObjIntConsumer<TerminalApplicationIdEntryBuilder> consumer;

    public PrimaryAIDAppVersionNumberAppender(String fieldName,
                                              ObjIntConsumer<TerminalApplicationIdEntryBuilder> consumer,
                                              TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
        this.fieldName = fieldName;
        this.consumer = consumer;
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        ndcCharBuffer.tryReadHexInt(4)
                .resolve(value -> consumer.accept(stateObject, value),
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, fieldName, errorMessage, ndcCharBuffer));

        callNextAppender(ndcCharBuffer, stateObject);
    }
}
