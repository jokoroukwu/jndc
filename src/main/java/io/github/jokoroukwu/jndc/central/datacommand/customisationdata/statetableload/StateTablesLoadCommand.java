package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload;

import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.CustomisationDataCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.MessageId;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class StateTablesLoadCommand extends CustomisationDataCommand {

    private final List<State> states;
    private final String mac;

    public StateTablesLoadCommand(List<State> states, String mac) {
        super(MessageId.STATE_TABLE);
        this.states = ObjectUtils.validateNotNull(states, "states");
        this.mac = ObjectUtils.validateNotNull(mac, "mac");
    }

    public static StateTablesLoadCommandBuilder builder() {
        return new StateTablesLoadCommandBuilder();
    }


    public List<State> getStates() {
        return Collections.unmodifiableList(states);
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder((32 * states.size()) + mac.length());
        appendStates(builder);
        if (!mac.isEmpty()) {
            builder.append(NdcConstants.FIELD_SEPARATOR)
                    .append(mac);
        }
        return builder.toString();
    }


    private void appendStates(StringBuilder builder) {
        for (var state : states) {
            builder.append(NdcConstants.FIELD_SEPARATOR).append(state.toNdcString());
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StateTablesLoadCommand.class.getSimpleName() + ": {", "}")
                .add("messageIdentifier: " + getMessageIdentifier())
                .add("states: " + states)
                .add("mac: '" + mac + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StateTablesLoadCommand that = (StateTablesLoadCommand) o;
        return getMessageIdentifier().equals(that.getMessageIdentifier()) &&
                states.equals(that.states) &&
                mac.equals(that.mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageIdentifier(), states, mac);
    }


}
