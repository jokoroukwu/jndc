package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.statetableload;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class State implements NdcComponent {
    private final String stateNumber;
    private final String stateType;
    private final String stateData;

    public State(String stateNumber, String stateType, String stateData) {
        this.stateNumber = ObjectUtils.validateNotNull(stateNumber, "stateNumber");
        this.stateType = ObjectUtils.validateNotNull(stateType, "stateType");
        this.stateData = ObjectUtils.validateNotNull(stateData, "stateData");
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public String getStateType() {
        return stateType;
    }

    public String getStateData() {
        return stateData;
    }

    @Override
    public String toNdcString() {
        return stateNumber + stateType + stateData;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", State.class.getSimpleName() + ": {", "}")
                .add("stateNumber: '" + stateNumber + "'")
                .add("stateType: '" + stateType + "'")
                .add("stateData: '" + stateData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return stateNumber.equals(state.stateNumber) && stateType.equals(state.stateType) && stateData.equals(state.stateData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateNumber, stateType, stateData);
    }
}
