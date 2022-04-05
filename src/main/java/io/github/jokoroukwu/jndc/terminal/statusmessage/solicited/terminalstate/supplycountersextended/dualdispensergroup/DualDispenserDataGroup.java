package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dualdispensergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class DualDispenserDataGroup implements IdentifiableCounterGroup {
    public static final char ID = 'l';

    private List<CassetteCounters> cassetteCounters;

    public DualDispenserDataGroup(Collection<CassetteCounters> cassetteCounters) {
        this.cassetteCounters
                = List.copyOf(CollectionUtils.requireNonNullNonEmpty(cassetteCounters, "cassetteCounters"));
    }

    DualDispenserDataGroup(List<CassetteCounters> cassetteCounters) {
        this.cassetteCounters = cassetteCounters;
    }


    public List<CassetteCounters> getCassetteCounters() {
        return cassetteCounters;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(28 * cassetteCounters.size() + 1)
                .append(ID)
                .appendComponents(cassetteCounters)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DualDispenserDataGroup.class.getSimpleName() + ": {", "}")
                .add("cassetteCounters: " + cassetteCounters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DualDispenserDataGroup that = (DualDispenserDataGroup) o;
        return cassetteCounters.equals(that.cassetteCounters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cassetteCounters);
    }
}
