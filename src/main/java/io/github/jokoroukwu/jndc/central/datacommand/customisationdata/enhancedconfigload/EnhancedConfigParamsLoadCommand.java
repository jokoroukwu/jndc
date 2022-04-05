package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandSubClass;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer.Timer;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

public class EnhancedConfigParamsLoadCommand extends CustomisationDataCommand {
    public static final String COMMAND_NAME = CentralMessageClass.DATA_COMMAND + ": "
            + DataCommandSubClass.CUSTOMISATION_DATA + ": " + MessageId.ENHANCED_CONFIG_DATA;

    private final Luno additionalLuno;
    private final Map<Integer, ConfigurationOption> optionMap;
    private final List<Timer> timers;

    public EnhancedConfigParamsLoadCommand(Luno additionalLuno,
                                           Map<Integer, ConfigurationOption> optionMap,
                                           List<Timer> timers) {
        super(MessageId.ENHANCED_CONFIG_DATA);
        this.additionalLuno = ObjectUtils.validateNotNull(additionalLuno, "lunoSetup");
        this.optionMap = ObjectUtils.validateNotNull(optionMap, "optionMap");
        this.timers = List.copyOf(timers);
    }

    public EnhancedConfigParamsLoadCommand(Luno additionalLuno, Collection<ConfigurationOption> optionMap, List<Timer> timers) {
        this(additionalLuno, toOptionMap(optionMap), timers);
    }

    private static Map<Integer, ConfigurationOption> toOptionMap(Collection<ConfigurationOption> options) {
        ObjectUtils.validateNotNull(options, "options cannot be null");
        if (options.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Integer, ConfigurationOption> optionMap = new HashMap<>(options.size());
        for (ConfigurationOption option : options) {
            optionMap.put(option.getNumber(), option);
        }
        return Collections.unmodifiableMap(optionMap);
    }

    public static EnhancedConfigParamsLoadCommandBuilder builder() {
        return new EnhancedConfigParamsLoadCommandBuilder();
    }


    public Luno getAdditionalLuno() {
        return additionalLuno;
    }

    public Map<Integer, ConfigurationOption> getOptionMap() {
        return Collections.unmodifiableMap(optionMap);
    }

    public Optional<ConfigurationOption> optionByNumber(int number) {
        return Optional.ofNullable(optionMap.get(number));
    }

    public List<Timer> getTimers() {
        return timers;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EnhancedConfigParamsLoadCommand.class.getSimpleName() + ": {", "}")
                .add("messageIdentifier: " + getMessageIdentifier())
                .add("additionalLuno: " + additionalLuno)
                .add("options: " + optionMap)
                .add("timers: " + timers)
                .toString();
    }

    @Override
    public String toNdcString() {
        return getMessageIdentifier().toNdcString() +
                NdcConstants.FIELD_SEPARATOR +
                additionalLuno.toNdcString() +
                optionMap.values().stream()
                        .map(NdcComponent::toNdcString)
                        .collect(Collectors.joining()) +
                timers.stream()
                        .map(NdcComponent::toNdcString)
                        .collect(Collectors.joining());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnhancedConfigParamsLoadCommand that = (EnhancedConfigParamsLoadCommand) o;
        return getMessageIdentifier().equals(that.getMessageIdentifier()) &&
                additionalLuno.equals(that.additionalLuno) &&
                optionMap.equals(that.optionMap) &&
                timers.equals(that.timers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageIdentifier(), additionalLuno, optionMap, timers);
    }
}
