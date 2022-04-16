package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.groupedcounters;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class GroupedCounterValues implements NdcComponent {
    private final List<Integer> counters;

    public GroupedCounterValues(int group1Value, int group2Value, int group3Value, int group4Value) {
        validateGroupValue(group1Value, "'Group 1' value");
        validateGroupValue(group2Value, "'Group 2' value");
        validateGroupValue(group3Value, "'Group 3' value");
        validateGroupValue(group4Value, "'Group 4' value");
        this.counters = List.of(group1Value, group2Value, group3Value, group4Value);
    }

    GroupedCounterValues(List<Integer> values) {
        this.counters = values;
    }
    GroupedCounterValues(int group1Value, int group2Value, int group3Value, int group4Value, Void unused) {
        this.counters = List.of(group1Value, group2Value, group3Value, group4Value);
    }

    public List<Integer> getValues() {
        return counters;
    }

    public int getValueForGroupIndex(int index) {
        return counters.get(index);
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(20)
                .appendZeroPadded(counters, Strings.EMPTY_STRING, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GroupedCounterValues.class.getSimpleName() + ": {", "}")
                .add("values: " + counters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupedCounterValues that = (GroupedCounterValues) o;
        return counters.equals(that.counters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counters);
    }

    private void validateGroupValue(int value, String desc) {
        Integers.validateRange(value, 0, 99999, desc);
    }
}
