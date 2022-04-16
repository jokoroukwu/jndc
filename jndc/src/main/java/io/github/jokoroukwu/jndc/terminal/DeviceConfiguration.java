package io.github.jokoroukwu.jndc.terminal;

import java.util.Set;

public interface DeviceConfiguration {

    boolean isMacEnabled();

    Set<Character> getTransactionRequestOptionalDataFieldsIds();

    ConfigurationOptions getConfigurationOptions();
}
