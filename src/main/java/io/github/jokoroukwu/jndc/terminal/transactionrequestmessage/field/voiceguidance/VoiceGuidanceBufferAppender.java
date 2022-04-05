package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.voiceguidance;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;

public class VoiceGuidanceBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Voice Guidance Buffer";

    public VoiceGuidanceBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public VoiceGuidanceBufferAppender() {
        super(null);
    }

    @Override
    protected char getBufferId() {
        return VoiceGuidanceBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        final VoiceGuidanceBuffer voiceGuidanceBuffer = readVoiceGuidanceBuffer(ndcCharBuffer);
        stateObject.withVoiceGuidanceBuffer(voiceGuidanceBuffer);

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private VoiceGuidanceBuffer readVoiceGuidanceBuffer(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final String languageId = readLanguageId(ndcCharBuffer);
            return new VoiceGuidanceBuffer(languageId);
        }
        return VoiceGuidanceBuffer.EMPTY;
    }

    private String readLanguageId(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadCharSequence(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        "Voice Guidance Buffer: Language Identifier", errorMessage, ndcCharBuffer))
                .get();
    }
}
