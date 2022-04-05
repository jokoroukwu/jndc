package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cashhandlergroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.cassettecounters.CassetteCounters;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CashHandlerDataGroup implements IdentifiableCounterGroup {
    public static final char HANDLER_0_ID = 'C';
    public static final char HANDLER_1_ID = 'D';
    private final char groupId;
    private final List<CassetteCounters> cassetteCounters;

    CashHandlerDataGroup(char groupId, List<CassetteCounters> cassetteCounters) {
        this.groupId = groupId;
        this.cassetteCounters = cassetteCounters;
    }

    public static CashHandlerDataGroup handler0(Collection<CassetteCounters> cassetteCounters) {
        return new CashHandlerDataGroup(HANDLER_0_ID, List.copyOf(validateCassettesSize(cassetteCounters)));
    }

    public static CashHandlerDataGroup handler0(CassetteCounters... cassetteCounters) {
        return new CashHandlerDataGroup(HANDLER_0_ID, validateCassettesSize(List.of(cassetteCounters)));
    }

    public static CashHandlerDataGroup handler1(Collection<CassetteCounters> cassetteCounters) {
        CollectionUtils.requireNonNullNonEmpty(cassetteCounters, "'Cash Handler Cassettes'");
        return new CashHandlerDataGroup(HANDLER_1_ID, List.copyOf(validateCassettesSize(cassetteCounters)));
    }

    public static CashHandlerDataGroup handler1(CassetteCounters... cassetteCounters) {
        return new CashHandlerDataGroup(HANDLER_1_ID, validateCassettesSize(List.of(cassetteCounters)));
    }

    private static <T extends Collection<CassetteCounters>> T validateCassettesSize(T cassettes) {
        if (cassettes.size() == 4 || cassettes.size() == 7) {
            return cassettes;
        }
        final String errorMessage = "Number of cassettes should be either 4 or 7 but was: " + cassettes.size();
        throw new IllegalArgumentException(errorMessage);
    }

    public List<CassetteCounters> getCashHandlerCassettes() {
        return cassetteCounters;
    }

    @Override
    public char getGroupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CashHandlerDataGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + groupId + "'")
                .add("cashHandlerCassettes: " + cassetteCounters)
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(28 * cassetteCounters.size() + 1)
                .append(groupId)
                .appendComponents(cassetteCounters)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashHandlerDataGroup that = (CashHandlerDataGroup) o;
        return groupId == that.groupId && cassetteCounters.equals(that.cassetteCounters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, cassetteCounters);
    }
}
