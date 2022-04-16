package io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata;

import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.generic.GenericEncryptorInformation;
import io.github.jokoroukwu.jndc.terminal.encryptorinitializationdata.newkvv.NewKvv;

public interface EncryptorInitialisationDataMessageListener {

    void onNewKeyVerificationValueMessage(EncryptorInitialisationData<NewKvv> kvvMessage);

    void onGenericEncryptorDataMessage(EncryptorInitialisationData<GenericEncryptorInformation> genericEncryptorDataMessage);

}
