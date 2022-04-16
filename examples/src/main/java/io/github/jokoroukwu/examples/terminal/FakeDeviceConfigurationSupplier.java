package io.github.jokoroukwu.examples.terminal;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.BnaSettings;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.CashHandlers;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationBase;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;

import java.util.Set;

/**
 * Just a dummy implementation for demonstration purposes.
 */
class FakeDeviceConfigurationSupplier implements DeviceConfigurationSupplier<TerminalMessageMeta> {

    @Override
    public DeviceConfiguration getConfiguration(TerminalMessageMeta meta) {
        return new DeviceConfigurationBase(true,
                Set.of('U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', '5', 'w', 'a', 'c', 'd', 'e', 'f', 'g', '<', '3'),
                ConfigurationOptions.of(BnaSettings.ACCEPT_MAX_NOTES, CashHandlers.DEFAULT));
    }
}
