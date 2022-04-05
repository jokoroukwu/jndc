package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.factory;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Map;

public interface ConfigurationOptionFactory {

    DescriptiveOptional<? extends ConfigurationOption> getOption(int optionNumber,
                                                                 String optionCode,
                                                                 Map<Integer, ConfigurationOption> availableOptions);
}
