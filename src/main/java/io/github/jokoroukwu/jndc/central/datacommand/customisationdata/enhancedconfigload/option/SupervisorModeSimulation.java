package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.option;

import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.StringJoiner;


public enum SupervisorModeSimulation implements ConfigurationOption {
    /**
     * Default.
     */
    ON(0),

    OFF(1);

    public static final int NUMBER = 33;
    private static final SupervisorModeSimulation[] values = values();

    private final int code;
    private final String ndcString;

    SupervisorModeSimulation(int code) {
        this.code = code;
        ndcString = String.format("%02d%03d", NUMBER, code);
    }

    public static DescriptiveOptional<SupervisorModeSimulation> forCode(int code) {
        if (code == 0) {
            return DescriptiveOptional.of(ON);
        }
        if (code == 1) {
            return DescriptiveOptional.of(OFF);
        }
        return DescriptiveOptional.empty(()
                -> String.format("value '%s' is not a valid 'Simulate Supervisor Mode entry/exit' option code", code));
    }

    public static DescriptiveOptional<SupervisorModeSimulation> forCode(String code) {
        return DescriptiveOptional.ofNullable(code, () -> "'Simulate Supervisor Mode entry/exit' option code cannot be null")
                .flatMapToInt(Integers::tryParseInt).flatMapToObject(SupervisorModeSimulation::forCode);
    }

    @Override
    public int getNumber() {
        return NUMBER;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toNdcString() {
        return ndcString;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SupervisorModeSimulation.class.getSimpleName() + ": {", "}")
                .add("number: '" + NUMBER + "'")
                .add("code: " + code)
                .toString();
    }
}
