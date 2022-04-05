package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;

public interface LanguageSupportDataObjectsTableMessageListener {

    void onLanguageSupportDataObjectsTableMessage(EmvConfigurationMessage<IccLanguageSupportTable> languageSupportTableMessage);
}
