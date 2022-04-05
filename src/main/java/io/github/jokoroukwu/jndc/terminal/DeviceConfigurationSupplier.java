package io.github.jokoroukwu.jndc.terminal;

public interface DeviceConfigurationSupplier<T> {

    DeviceConfiguration getConfiguration(T meta);
}
