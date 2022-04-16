package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.configidload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface ConfigurationIdNumberLoadCommandListener {

    void onConfigurationIdNumberLoadCommand(DataCommand<ConfigurationIdNumberLoadCommand> configurationIdNumberLoadMessage);
}
