package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageClass;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.*;
import io.github.jokoroukwu.jndc.util.ObjectUtils;


public class GenericEncryptorInformationAppender implements ConfigurableNdcComponentAppender<EncryptorInitialisationDataBuilder<EncryptorInformation>> {
    private final EncryptorInitialisationDataMessageListener messageListener;
    private final InformationId informationId;

    public GenericEncryptorInformationAppender(EncryptorInitialisationDataMessageListener messageListener, InformationId informationId) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.informationId = ObjectUtils.validateNotNull(informationId, "informationId");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                EncryptorInitialisationDataBuilder<EncryptorInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(getCommandName(), "'Encryptor Information'", errorMessage, ndcCharBuffer));

        final String data = ndcCharBuffer.readCharSequence(ndcCharBuffer.remaining());
        final EncryptorInitialisationData<? extends EncryptorInformation> message = stateObject
                .withEncryptorInfo(new GenericEncryptorInformation(informationId, data))
                .build();

        messageListener.onGenericEncryptorDataMessage((EncryptorInitialisationData<GenericEncryptorInformation>) message);
    }

    private String getCommandName() {
        return TerminalMessageClass.SOLICITED
                + ": "
                + TerminalMessageSubClass.ENCRYPTOR_INITIALISATION_DATA
                + ": "
                + informationId;

    }
}
