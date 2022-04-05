package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;

public class ScreenBaseFieldAppender extends ChainedNdcComponentAppender<LanguageSupportTableEntryBuilder> {
    public static final String FIELD_NAME = "Screen Base";

    public ScreenBaseFieldAppender(NdcComponentAppender<LanguageSupportTableEntryBuilder> nextReader) {
        super(nextReader);
    }

    public ScreenBaseFieldAppender() {
        this(new AudioBaseFieldAppender());
    }

    @Override
    public void appendComponent(NdcCharBuffer buffer, LanguageSupportTableEntryBuilder stateObject) {
        buffer.tryReadInt(3)
                .resolve(stateObject::withScreenBase,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccLanguageSupportTableAppender.COMMAND_NAME, FIELD_NAME, errorMessage, buffer));

        callNextAppender(buffer, stateObject);
    }
}
