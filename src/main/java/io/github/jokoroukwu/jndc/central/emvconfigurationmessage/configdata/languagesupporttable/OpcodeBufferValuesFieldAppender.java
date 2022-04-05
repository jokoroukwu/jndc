package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;


public enum OpcodeBufferValuesFieldAppender implements NdcComponentAppender<LanguageSupportTableEntryBuilder> {
    INSTANCE;
    public static final String FIELD_NAME = "Opcode Buffer Values";

    @Override
    public void appendComponent(NdcCharBuffer buffer, LanguageSupportTableEntryBuilder stateObject) {
        buffer.tryReadCharSequence(3)
                .filter(LanguageSupportTableEntry::areOpcodeBufferValuesValid,
                        value -> () -> "characters should be in range A-D or F-I or @ but was: " + value)
                .resolve(stateObject::withOpCodeBufferValues,
                        errorMessage -> NdcMessageParseException.onFieldParseError(IccLanguageSupportTable.COMMAND_NAME, FIELD_NAME, errorMessage, buffer));
    }
}
