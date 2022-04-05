package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.Set;

public class DeviceConfigurationBase implements DeviceConfiguration {
    private final boolean isMacEnabled;
    private final Set<Character> optionalDataFields;
    private final ConfigurationOptions configurationOptions;

    public DeviceConfigurationBase(boolean isMacEnabled, Set<Character> optionalDataFields, ConfigurationOptions configurationOptions) {
        this.isMacEnabled = ObjectUtils.validateNotNull(isMacEnabled, "isMacEnabled");
        this.optionalDataFields = ObjectUtils.validateNotNull(optionalDataFields, "optionalDataFields");
        this.configurationOptions = ObjectUtils.validateNotNull(configurationOptions, "configurationOptions");
    }

    @Override
    public boolean isMacEnabled() {
        return isMacEnabled;
    }

    @Override
    public Set<Character> getTransactionRequestOptionalDataFieldsIds() {
        return optionalDataFields;
    }

    @Override
    public ConfigurationOptions getConfigurationOptions() {
        return configurationOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceConfigurationBase that = (DeviceConfigurationBase) o;
        return isMacEnabled == that.isMacEnabled &&
                optionalDataFields.equals(that.optionalDataFields) &&
                configurationOptions.equals(that.configurationOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isMacEnabled, optionalDataFields, configurationOptions);
    }
}
