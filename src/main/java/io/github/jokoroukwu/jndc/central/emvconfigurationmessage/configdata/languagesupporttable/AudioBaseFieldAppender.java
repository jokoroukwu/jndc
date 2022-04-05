package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;


public class AudioBaseFieldAppender extends ChainedNdcComponentAppender<LanguageSupportTableEntryBuilder> {
    public static final String FIELD_NAME = "Audio Base";

    public AudioBaseFieldAppender(NdcComponentAppender<LanguageSupportTableEntryBuilder> nextReader) {
        super(nextReader);
    }

    public AudioBaseFieldAppender() {
        this(new OpcodeBufferPositionsFieldAppender());
    }


    @Override
    public void appendComponent(NdcCharBuffer buffer, LanguageSupportTableEntryBuilder stateObject) {
        buffer.tryReadInt(3)
                .filter(value -> value <= 7, value -> () -> "should be in range 0-7 but was: " + value)
                .resolve(stateObject::withAudioBase,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccLanguageSupportTable.COMMAND_NAME, FIELD_NAME, errorMessage, buffer));

        callNextAppender(buffer, stateObject);
    }

}
