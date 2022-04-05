package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.Languages;


public class LanguageCodeFieldAppender extends ChainedNdcComponentAppender<LanguageSupportTableEntryBuilder> {
    public static final String FIELD_NAME = "Language Code";

    public LanguageCodeFieldAppender(NdcComponentAppender<LanguageSupportTableEntryBuilder> nextReader) {
        super(nextReader);
    }

    public LanguageCodeFieldAppender() {
        this(new ScreenBaseFieldAppender());
    }


    @Override
    public void appendComponent(NdcCharBuffer buffer, LanguageSupportTableEntryBuilder stateObject) {
        buffer.tryReadCharSequence(2)
                .filter(Languages.ISO_639_LANGUAGE_CODES::contains, code -> () -> String.format("'%s' is not a valid ISO-639 code", code))
                .resolve(stateObject::withLanguageCode,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccLanguageSupportTable.COMMAND_NAME, FIELD_NAME, errorMessage, buffer));

        callNextAppender(buffer, stateObject);
    }
}
