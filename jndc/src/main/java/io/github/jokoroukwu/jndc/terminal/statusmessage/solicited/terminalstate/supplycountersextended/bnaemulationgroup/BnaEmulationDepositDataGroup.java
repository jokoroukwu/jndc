package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.bnaemulationgroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata.CashDepositNotes;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class BnaEmulationDepositDataGroup implements IdentifiableCounterGroup {
    public static final char ID = 'K';
    private final CashDepositNotes cashDepositNotes;

    public BnaEmulationDepositDataGroup(CashDepositNotes cashDepositNotes) {
        this.cashDepositNotes = ObjectUtils.validateNotNull(cashDepositNotes, "Cash Deposit Notes");
    }

    BnaEmulationDepositDataGroup(CashDepositNotes cashDepositNotes, Void unused) {
        this.cashDepositNotes = cashDepositNotes;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    public CashDepositNotes getCashDepositNotes() {
        return cashDepositNotes;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(21)
                .append(ID)
                .appendComponent(cashDepositNotes)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BnaEmulationDepositDataGroup.class.getSimpleName() + ": {", "}")
                .add("cashDepositNotes: " + cashDepositNotes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BnaEmulationDepositDataGroup that = (BnaEmulationDepositDataGroup) o;
        return cashDepositNotes.equals(that.cashDepositNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashDepositNotes);
    }
}
