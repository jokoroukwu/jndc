package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.SupplyCountersExtendedBuilder;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class CashHandlerDataGroupAppenderBuilder {
    private char groupId;
    private String groupName;
    private NdcComponentReader<List<CassetteCounters>> cassettesReader;
    private BiConsumer<SupplyCountersExtendedBuilder, CashHandlerDataGroup> dataAcceptor;
    private ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender;


    public CashHandlerDataGroupAppenderBuilder withHandler1Id() {
        groupId = CashHandlerDataGroup.HANDLER_1_ID;
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withHandler0Id() {
        groupId = CashHandlerDataGroup.HANDLER_0_ID;
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withHandler0GroupName() {
        this.groupName = "Cash Handler 0 data group";
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withHandler1GroupName() {
        this.groupName = "Cash Handler 1 data group";
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withCassettesReader(NdcComponentReader<List<CassetteCounters>> cassettesReader) {
        this.cassettesReader = cassettesReader;
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withDataAcceptor(BiConsumer<SupplyCountersExtendedBuilder, CashHandlerDataGroup> dataAcceptor) {
        this.dataAcceptor = dataAcceptor;
        return this;
    }

    public CashHandlerDataGroupAppenderBuilder withNextAppender(ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public CashHandlerDataGroupAppender build() {
        return new CashHandlerDataGroupAppender(groupId, groupName, cassettesReader, dataAcceptor, nextAppender);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CashHandlerDataGroupAppenderBuilder.class.getSimpleName() + ": {", "}")
                .add("groupId: '" + groupId + "'")
                .add("groupName: '" + groupName + '\'')
                .add("cassettesReader: " + cassettesReader)
                .add("dataAcceptor: " + dataAcceptor)
                .add("nextAppender: " + nextAppender)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashHandlerDataGroupAppenderBuilder that = (CashHandlerDataGroupAppenderBuilder) o;
        return groupId == that.groupId &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(cassettesReader, that.cassettesReader) &&
                Objects.equals(dataAcceptor, that.dataAcceptor) &&
                Objects.equals(nextAppender, that.nextAppender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, cassettesReader, dataAcceptor, nextAppender);
    }
}
