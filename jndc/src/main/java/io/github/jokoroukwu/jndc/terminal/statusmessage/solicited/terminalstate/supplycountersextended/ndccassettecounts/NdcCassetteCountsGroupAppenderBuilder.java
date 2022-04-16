package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.ndccassettecounts;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class NdcCassetteCountsGroupAppenderBuilder {
    private char groupId;
    private String groupName;
    private BiConsumer<SupplyCountersExtendedBuilder, NdcCassetteCountsGroup> dataConsumer;
    private NdcComponentReader<List<NdcCassetteCounts>> cassetteCountsReader;
    private ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender;

    public NdcCassetteCountsGroupAppenderBuilder withGroupId(char groupId) {
        this.groupId = groupId;
        return this;
    }

    public NdcCassetteCountsGroupAppenderBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public NdcCassetteCountsGroupAppenderBuilder withDataConsumer(BiConsumer<SupplyCountersExtendedBuilder, NdcCassetteCountsGroup> dataConsumer) {
        this.dataConsumer = dataConsumer;
        return this;
    }

    public NdcCassetteCountsGroupAppenderBuilder withCassetteCountsReader(NdcComponentReader<List<NdcCassetteCounts>> cassetteCountsReader) {
        this.cassetteCountsReader = cassetteCountsReader;
        return this;
    }

    public NdcCassetteCountsGroupAppenderBuilder withNextAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public NdcCassetteCountsGroupAppender build() {
        return new NdcCassetteCountsGroupAppender(groupId, groupName, cassetteCountsReader, dataConsumer, nextAppender);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NdcCassetteCountsGroupAppenderBuilder.class.getSimpleName() + ": {", "}")
                .add("groupId: '" + groupId + "'")
                .add("groupName: '" + groupName + "'")
                .add("dataConsumer: " + dataConsumer)
                .add("cassetteCountsReader: " + cassetteCountsReader)
                .add("nextAppender: " + nextAppender)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdcCassetteCountsGroupAppenderBuilder that = (NdcCassetteCountsGroupAppenderBuilder) o;
        return groupId == that.groupId &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(dataConsumer, that.dataConsumer) &&
                Objects.equals(cassetteCountsReader, that.cassetteCountsReader) &&
                Objects.equals(nextAppender, that.nextAppender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, dataConsumer, cassetteCountsReader, nextAppender);
    }
}
