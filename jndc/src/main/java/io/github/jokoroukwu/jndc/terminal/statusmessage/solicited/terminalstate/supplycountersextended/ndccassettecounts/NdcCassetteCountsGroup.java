package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class NdcCassetteCountsGroup implements IdentifiableCounterGroup {
    public static final int MAX_SIZE = 999;
    public static final char BNA_GROUP_ID = 'I';
    public static final char ECB_2_GROUP_ID = 'N';
    public static final char ECB_3_GROUP_ID = 'O';

    private final char groupId;
    private final List<NdcCassetteCounts> cassetteCounts;

    private NdcCassetteCountsGroup(char groupId, Collection<NdcCassetteCounts> cassetteCounts) {
        this.cassetteCounts = List.copyOf(CollectionUtils.requireNonNullNonEmpty(cassetteCounts, "cassetteCounts"));
        this.groupId = groupId;
    }

    NdcCassetteCountsGroup(char groupId, List<NdcCassetteCounts> cassetteCounts) {
        this.groupId = groupId;
        this.cassetteCounts = cassetteCounts;
    }

    public static NdcCassetteCountsGroup bna(Collection<NdcCassetteCounts> cassetteCounts) {
        final List<NdcCassetteCounts> ndcCassetteCounts = List.copyOf(cassetteCounts);
        return new NdcCassetteCountsGroup(BNA_GROUP_ID, ndcCassetteCounts);
    }

    public static NdcCassetteCountsGroup bna(NdcCassetteCounts... ndcCassetteCounts) {
        final List<NdcCassetteCounts> ndcCassetteCountsList = List.of(ndcCassetteCounts);
        return new NdcCassetteCountsGroup(BNA_GROUP_ID, ndcCassetteCountsList);
    }

    public static NdcCassetteCountsGroup ecb2(NdcCassetteCounts... ndcCassetteCounts) {
        final List<NdcCassetteCounts> ndcCassetteCountsList = List.of(ndcCassetteCounts);
        return new NdcCassetteCountsGroup(ECB_2_GROUP_ID, ndcCassetteCountsList);
    }

    public static NdcCassetteCountsGroup ecb2(Collection<NdcCassetteCounts> cassetteCounts) {
        final List<NdcCassetteCounts> ndcCassetteCounts = List.copyOf(cassetteCounts);
        return new NdcCassetteCountsGroup(ECB_2_GROUP_ID, ndcCassetteCounts);
    }

    public static NdcCassetteCountsGroup ecb3(NdcCassetteCounts... ndcCassetteCounters) {
        final List<NdcCassetteCounts> ndcCassetteCounts = List.of(ndcCassetteCounters);
        return new NdcCassetteCountsGroup(ECB_2_GROUP_ID, ndcCassetteCounts);
    }

    public static NdcCassetteCountsGroup ecb3(Collection<NdcCassetteCounts> cassetteCounts) {
        final List<NdcCassetteCounts> ndcCassetteCounts = List.copyOf(cassetteCounts);
        return new NdcCassetteCountsGroup(ECB_3_GROUP_ID, ndcCassetteCounts);
    }

    @Override
    public char getGroupId() {
        return groupId;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(256)
                .append(groupId)
                .appendComponents(cassetteCounts)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NdcCassetteCountsGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + groupId + "'")
                .add("cassetteCounts: " + cassetteCounts)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdcCassetteCountsGroup that = (NdcCassetteCountsGroup) o;
        return groupId == that.groupId && cassetteCounts.equals(that.cassetteCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, cassetteCounts);
    }
}
