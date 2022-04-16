package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload;

import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;

public interface EnhancedConfigParamsLoadCommandListener {

    void onEnhancedConfigParamsLoadCommand(DataCommand<EnhancedConfigParamsLoadCommand> enhancedConfigParamsLoadMessage);
}
