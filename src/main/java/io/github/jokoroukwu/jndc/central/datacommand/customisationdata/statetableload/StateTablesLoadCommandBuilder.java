package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload;

import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.LinkedList;
import java.util.List;

public final class StateTablesLoadCommandBuilder {
    private final List<State> states;
    private String mac;

    StateTablesLoadCommandBuilder() {
        this.states = new LinkedList<>();
        this.mac = Strings.EMPTY_STRING;
    }


    public StateTablesLoadCommandBuilder withStates(List<State> states) {
        ObjectUtils.validateNotNull(states, "stateBlocks");
        this.states.addAll(states);
        return this;
    }

    public StateTablesLoadCommandBuilder withState(State state) {
        ObjectUtils.validateNotNull(states, "stateBlock");
        states.add(state);
        return this;
    }

    public StateTablesLoadCommandBuilder withMac(String mac) {
        this.mac = ObjectUtils.validateNotNull(mac, "mac");
        return this;
    }

    public StateTablesLoadCommand build() {
        return new StateTablesLoadCommand(
                states,
                mac
        );
    }
}
