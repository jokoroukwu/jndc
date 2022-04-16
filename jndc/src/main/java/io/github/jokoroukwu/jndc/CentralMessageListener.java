package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload.ConfigurationIdNumberLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload.ConfigurationIdNumberLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload.StateTablesLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload.StateTablesLoadCommandListener;
import io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange.ExtendedEncryptionKeyChangeCommand;
import io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange.ExtendedEncryptionKeyChangeCommandListener;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.CurrencyDataObjectsTableMessageListener;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.IccCurrencyDataObjectsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable.IccLanguageSupportTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable.LanguageSupportDataObjectsTableMessageListener;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.IccTerminalAcceptableAppIdsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.TerminalAcceptableAidsTableMessageListener;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable.IccTerminalDataObjectsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable.TerminalDataObjectsTableMessageListener;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.IccTransactionDataObjectsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.TransactionDataObjectsTableMessageListener;
import io.github.jokoroukwu.jndc.central.terminalcommand.TerminalCommand;
import io.github.jokoroukwu.jndc.central.terminalcommand.TerminalCommandListener;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommand;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandListener;

public interface CentralMessageListener extends ConfigurationIdNumberLoadCommandListener, FitDataLoadCommandListener,
        EnhancedConfigParamsLoadCommandListener, DateTimeLoadCommandListener, StateTablesLoadCommandListener,
        TerminalCommandListener, ScreenKeyboardLoadCommandListener, CurrencyDataObjectsTableMessageListener,
        TransactionDataObjectsTableMessageListener, LanguageSupportDataObjectsTableMessageListener,
        ExtendedEncryptionKeyChangeCommandListener, TerminalDataObjectsTableMessageListener,
        TerminalAcceptableAidsTableMessageListener, TransactionReplyCommandListener {


    @Override
    default void onExtendedEncryptionKeyChangeCommand(DataCommand<ExtendedEncryptionKeyChangeCommand> message) {

    }

    @Override
    default void onScreenKeyboardLoadCommand(DataCommand<ScreenKeyboardLoadCommand> message) {

    }

    @Override
    default void onConfigurationIdNumberLoadCommand(DataCommand<ConfigurationIdNumberLoadCommand> message) {

    }

    @Override
    default void onEnhancedConfigParamsLoadCommand(DataCommand<EnhancedConfigParamsLoadCommand> message) {

    }

    @Override
    default void onFitDataLoadCommand(DataCommand<FitDataLoadCommand> message) {

    }

    @Override
    default void onDateTimeLoadCommand(DataCommand<DateTimeLoadCommand> message) {

    }

    @Override
    default void onStateTablesLoadCommand(DataCommand<StateTablesLoadCommand> message) {

    }

    @Override
    default void onTerminalCommand(TerminalCommand message) {

    }

    @Override
    default void onCurrencyDataObjectsTableMessage(EmvConfigurationMessage<IccCurrencyDataObjectsTable> message) {

    }

    @Override
    default void onTransactionDataObjectsTableMessage(EmvConfigurationMessage<IccTransactionDataObjectsTable> message) {
    }

    @Override
    default void onLanguageSupportDataObjectsTableMessage(EmvConfigurationMessage<IccLanguageSupportTable> message) {

    }

    @Override
    default void onTerminalDataObjectsTableMessage(EmvConfigurationMessage<IccTerminalDataObjectsTable> message) {

    }

    @Override
    default void onTerminalAcceptableAidsTableMessage(EmvConfigurationMessage<IccTerminalAcceptableAppIdsTable> message) {

    }

    @Override
    default void onTransactionReplyCommand(TransactionReplyCommand message) {

    }
}
