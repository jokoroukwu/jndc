package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;

public interface TransactionDataObjectsTableMessageListener {

    void onTransactionDataObjectsTableMessage(EmvConfigurationMessage<IccTransactionDataObjectsTable> emvConfigurationMessage);
}
