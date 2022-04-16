package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class OpcodeBufferPositionsFieldAppender extends ChainedNdcComponentAppender<LanguageSupportTableEntryBuilder> {
    public static final String FIELD_NAME = "Opcode Buffer Positions";

    public OpcodeBufferPositionsFieldAppender(NdcComponentAppender<LanguageSupportTableEntryBuilder> nextReader) {
        super(nextReader);
    }

    public OpcodeBufferPositionsFieldAppender() {
        this(OpcodeBufferValuesFieldAppender.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer buffer, LanguageSupportTableEntryBuilder stateObject) {
        buffer.tryReadInt(3)
                .filter(LanguageSupportTableEntry::areBufferPositionsValid,
                        value -> () -> "should three distinct decimal digits in range 0-7 but was: " + value)
                .resolve(stateObject::withOpcodeBufferPositions,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccLanguageSupportTableAppender.COMMAND_NAME, FIELD_NAME, errorMessage, buffer));

        callNextAppender(buffer, stateObject);
    }
}
