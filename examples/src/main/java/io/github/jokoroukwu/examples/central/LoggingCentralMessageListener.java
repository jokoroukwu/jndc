package io.github.jokoroukwu.examples.central;

import io.github.jokoroukwu.jndc.CentralMessageListener;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload.ConfigurationIdNumberLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.datetimeload.DateTimeLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.screenkeyboardload.ScreenKeyboardLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload.StateTablesLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange.ExtendedEncryptionKeyChangeCommand;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.IccCurrencyDataObjectsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable.IccLanguageSupportTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable.IccTerminalAcceptableAppIdsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable.IccTerminalDataObjectsTable;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.IccTransactionDataObjectsTable;
import io.github.jokoroukwu.jndc.central.terminalcommand.TerminalCommand;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener implementation is just a demonstration of
 * how central messages are consumed.
 */
public class LoggingCentralMessageListener implements CentralMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCentralMessageListener.class);

    @Override
    public void onExtendedEncryptionKeyChangeCommand(DataCommand<ExtendedEncryptionKeyChangeCommand> message) {
        LOGGER.info("Extended Encryption Key Change Command received: {}", message);
    }

    @Override
    public void onScreenKeyboardLoadCommand(DataCommand<ScreenKeyboardLoadCommand> message) {
        LOGGER.info("Screen/Keyboard Load Command received: {}", message);
    }

    @Override
    public void onConfigurationIdNumberLoadCommand(DataCommand<ConfigurationIdNumberLoadCommand> message) {
        LOGGER.info("Configuration ID Number Load Command received: {}", message);
    }

    @Override
    public void onEnhancedConfigParamsLoadCommand(DataCommand<EnhancedConfigParamsLoadCommand> message) {
        LOGGER.info("Enhanced Configuration Params Load Command: {}", message);
    }

    @Override
    public void onFitDataLoadCommand(DataCommand<FitDataLoadCommand> message) {
        LOGGER.info("Fit Data Load Command received: {}", message);
    }

    @Override
    public void onDateTimeLoadCommand(DataCommand<DateTimeLoadCommand> message) {
        LOGGER.info("Date Time Load Command received: {}", message);
    }

    @Override
    public void onStateTablesLoadCommand(DataCommand<StateTablesLoadCommand> message) {
        LOGGER.info("State Tabled Load Command received: {}", message);
    }

    @Override
    public void onTerminalCommand(TerminalCommand message) {
        LOGGER.info("Terminal Command received: {}", message);
    }

    @Override
    public void onCurrencyDataObjectsTableMessage(EmvConfigurationMessage<IccCurrencyDataObjectsTable> message) {
        LOGGER.info("Currency Data Objects Table EMV Configuration message received: {}", message);
    }

    @Override
    public void onTransactionDataObjectsTableMessage(EmvConfigurationMessage<IccTransactionDataObjectsTable> message) {
        LOGGER.info("Transaction Data Objects Table EMV Configuration message received: {}", message);
    }

    @Override
    public void onLanguageSupportDataObjectsTableMessage(EmvConfigurationMessage<IccLanguageSupportTable> message) {
        LOGGER.info("Language Support Data Objects Table EMV Configuration message received: {}", message);
    }

    @Override
    public void onTerminalDataObjectsTableMessage(EmvConfigurationMessage<IccTerminalDataObjectsTable> message) {
        LOGGER.info("Terminal Data Objects Table EMV Configuration message received: {}", message);
    }

    @Override
    public void onTerminalAcceptableAidsTableMessage(EmvConfigurationMessage<IccTerminalAcceptableAppIdsTable> message) {
        LOGGER.info("Terminal Acceptable AIDs EMV Configuration message received: {}", message);
    }

    @Override
    public void onTransactionReplyCommand(TransactionReplyCommand message) {
        LOGGER.info("Transaction Reply Command message received: {}", message);
    }
}
