package io.github.jokoroukwu.examples.central;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationBase;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;

import java.util.Set;

/**
 * Just a dummy implementation for demonstration purposes.
 */
class FakeDeviceConfigurationSupplier implements DeviceConfigurationSupplier<CentralMessageMeta> {

    @Override
    public DeviceConfiguration getConfiguration(CentralMessageMeta meta) {
        return new DeviceConfigurationBase(true, Set.of(), ConfigurationOptions.EMPTY);
    }
}
