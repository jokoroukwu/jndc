package io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface ExtendedEncryptionKeyChangeCommandListener {

    void onExtendedEncryptionKeyChangeCommand(DataCommand<ExtendedEncryptionKeyChangeCommand> message);
}


