package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.chequeprocessorgroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ChequeProcessorDataGroup implements IdentifiableCounterGroup {
    public static final char ID = 'J';
    private final List<Bin> bins;

    ChequeProcessorDataGroup(List<Bin> bins) {
        this.bins = bins;
    }

    public ChequeProcessorDataGroup(Collection<Bin> bins) {
        this.bins = List.copyOf(CollectionUtils.requireNonNullNonEmpty(bins, "Bins"));
    }

    public List<Bin> getBins() {
        return bins;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(6 * bins.size() + 1)
                .append(ID)
                .appendComponents(bins)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ChequeProcessorDataGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("bins: " + bins)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChequeProcessorDataGroup that = (ChequeProcessorDataGroup) o;
        return bins.equals(that.bins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bins);
    }

}
