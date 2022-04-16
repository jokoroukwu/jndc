package io.github.jokoroukwu.jndc.terminal;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class ConfigurationOptions {
    public static final ConfigurationOptions EMPTY = new ConfigurationOptions(Collections.emptyMap());
    private final Map<Integer, ConfigurationOption> optionMap;

    private ConfigurationOptions(Map<Integer, ConfigurationOption> optionMap) {
        this.optionMap = optionMap;
    }

    public static ConfigurationOptions of(Collection<ConfigurationOption> options) {
        ObjectUtils.validateNotNull(options, "options");
        if (options.isEmpty()) {
            return EMPTY;
        }
        final Map<Integer, ConfigurationOption> optionMap = new HashMap<>(options.size(), 1);
        for (ConfigurationOption option : options) {
            optionMap.put(option.getCode(), option);
        }
        return new ConfigurationOptions(Collections.unmodifiableMap(optionMap));
    }

    public static ConfigurationOptions of(ConfigurationOption... options) {
        //  implicit null check
        if (options.length == 0) {
            return EMPTY;
        }
        if (options.length == 1) {
            final ConfigurationOption option = options[0];
            return new ConfigurationOptions(Map.of(option.getNumber(), option));
        }
        final Map<Integer, ConfigurationOption> optionMap = new HashMap<>(options.length, 1);
        for (ConfigurationOption option : options) {
            optionMap.put(option.getNumber(), option);
        }
        return new ConfigurationOptions(Collections.unmodifiableMap(optionMap));
    }


    public Optional<ConfigurationOption> getOption(int optionNumber) {
        return Optional.ofNullable(optionMap.get(optionNumber));
    }

    public Map<Integer, ConfigurationOption> getOptionMap() {
        return optionMap;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigurationOptions.class.getSimpleName() + ": {", "}")
                .add("options: " + optionMap.values())
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationOptions that = (ConfigurationOptions) o;
        return optionMap.equals(that.optionMap);
    }

    @Override
    public int hashCode() {
        return optionMap.hashCode();
    }
}
