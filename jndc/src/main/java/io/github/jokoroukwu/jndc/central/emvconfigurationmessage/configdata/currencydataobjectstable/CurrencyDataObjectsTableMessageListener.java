package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;

public interface CurrencyDataObjectsTableMessageListener {

    void onCurrencyDataObjectsTableMessage(EmvConfigurationMessage<IccCurrencyDataObjectsTable> currencyTableMessage);
}
