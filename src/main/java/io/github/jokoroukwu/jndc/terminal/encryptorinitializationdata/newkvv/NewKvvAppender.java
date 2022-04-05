package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.*;
import io.github.jokoroukwu.jndc.util.ObjectUtils;


public class NewKvvAppender implements ConfigurableNdcComponentAppender<EncryptorInitialisationDataBuilder<EncryptorInformation>> {
    public static final String COMMAND_NAME = TerminalMessageClass.SOLICITED
            + ": "
            + TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA
            + ": "
            + InformationId.NEW_KVV;

    private final EncryptorInitialisationDataMessageListener messageListener;

    public NewKvvAppender(EncryptorInitialisationDataMessageListener messageListener) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                EncryptorInitialisationDataBuilder<EncryptorInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(COMMAND_NAME, "'Encryptor Information'", errorMessage, ndcCharBuffer));


        final EncryptorInitialisationData<? extends EncryptorInformation> message = stateObject
                .withEncryptorInfo(readNewKvv(ndcCharBuffer))
                .build();

        messageListener.onNewKeyVerificationValueMessage((EncryptorInitialisationData<NewKvv>) message);
    }

    private NewKvv readNewKvv(NdcCharBuffer ndcCharBuffer) {
        final int remaining = ndcCharBuffer.remaining();
        validateKvvLength(remaining, ndcCharBuffer);
        final String kvv = ndcCharBuffer.readCharSequence(remaining);
        return new NewKvv(kvv);
    }

    private void validateKvvLength(int length, NdcCharBuffer ndcCharBuffer) {
        if (length != 6 && length != 72) {
            final String errorMessage = "new KVV length should be 6 or 72 characters but %d character(s) remain at position %d";
            throw NdcMessageParseException.withMessage(COMMAND_NAME, "'Encryptor Information'",
                    String.format(errorMessage, length, ndcCharBuffer.position()), ndcCharBuffer);
        }
    }


}
