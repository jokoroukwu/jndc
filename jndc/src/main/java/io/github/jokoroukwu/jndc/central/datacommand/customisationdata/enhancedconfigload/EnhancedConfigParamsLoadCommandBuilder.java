package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option.ConfigurationOption;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer.Timer;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public final class EnhancedConfigParamsLoadCommandBuilder {
    private Luno additionalLuno;
    private Map<Integer, ConfigurationOption> optionMap;
    private List<Timer> timers;

    public EnhancedConfigParamsLoadCommandBuilder() {
        additionalLuno = Luno.DEFAULT;
        timers = Collections.emptyList();
        optionMap = Collections.emptyMap();
    }

    public EnhancedConfigParamsLoadCommandBuilder withAdditionalLuno(Luno additionalLuno) {
        this.additionalLuno = ObjectUtils.validateNotNull(additionalLuno, "additionalLuno");
        return this;
    }

    public Luno getAdditionalLuno() {
        return additionalLuno;
    }

    public Map<Integer, ConfigurationOption> getOptionMap() {
        return Collections.unmodifiableMap(optionMap);
    }

    public List<Timer> getTimers() {
        return Collections.unmodifiableList(timers);
    }

    public Optional<ConfigurationOption> getOption(int number) {
        return Optional.ofNullable(optionMap.get(number));
    }

    public EnhancedConfigParamsLoadCommandBuilder withOptions(Map<Integer, ConfigurationOption> optionMap) {
        ObjectUtils.validateNotNull(optionMap, "optionMap");
        this.optionMap = optionMap;
        return this;
    }

    public EnhancedConfigParamsLoadCommandBuilder addOptions(Map<Integer, ConfigurationOption> optionMap) {
        ObjectUtils.validateNotNull(optionMap, "optionMap");
        lazyInitOptions();
        this.optionMap.putAll(optionMap);
        return this;
    }

    public EnhancedConfigParamsLoadCommandBuilder addOption(ConfigurationOption option) {
        ObjectUtils.validateNotNull(option, "option");
        lazyInitOptions();
        optionMap.put(option.getNumber(), option);
        return this;
    }

    private void lazyInitOptions() {
        if (optionMap == Collections.EMPTY_MAP) {
            optionMap = new HashMap<>();
        }
    }

    public EnhancedConfigParamsLoadCommandBuilder withTimers(List<Timer> timers) {
        ObjectUtils.validateNotNull(timers, "timers");
        this.timers = timers;
        return this;
    }

    public EnhancedConfigParamsLoadCommandBuilder addTimers(Collection<Timer> timers) {
        ObjectUtils.validateNotNull(timers, "timers");
        lazyInitTimers();
        this.timers.addAll(timers);
        return this;
    }

    public EnhancedConfigParamsLoadCommandBuilder addTimer(Timer timer) {
        ObjectUtils.validateNotNull(timer, "timer");
        lazyInitTimers();
        timers.add(timer);
        return this;
    }


    private void lazyInitTimers() {
        if (timers == Collections.EMPTY_LIST) {
            timers = new LinkedList<>();
        }
    }

    public EnhancedConfigParamsLoadCommand build() {
        return new EnhancedConfigParamsLoadCommand(additionalLuno, optionMap, timers);
    }
}
