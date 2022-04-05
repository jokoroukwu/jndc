package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.dpmgroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class DpmDataGroup implements IdentifiableCounterGroup {
    public static final char ID = 'H';

    private final String depositBinId;
    private final String documentsDeposited;


    public DpmDataGroup(String depositBinId, String documentsDeposited) {
        this.depositBinId = Strings.validateLength(depositBinId, 2, "'Deposit Bin Identifier'");
        this.documentsDeposited = Strings.validateLength(documentsDeposited, 5,
                "'Documents Deposited in Bin'");
    }

    DpmDataGroup(String depositBinId, String documentsDeposited, Void unused) {
        this.depositBinId = depositBinId;
        this.documentsDeposited = documentsDeposited;
    }


    public String getDepositBinId() {
        return depositBinId;
    }

    public String getDocumentsDeposited() {
        return documentsDeposited;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return ID + depositBinId + documentsDeposited;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DpmDataGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("depositBinId: '" + depositBinId + "'")
                .add("documentsDeposited: '" + documentsDeposited + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DpmDataGroup that = (DpmDataGroup) o;
        return depositBinId.equals(that.depositBinId) && documentsDeposited.equals(that.documentsDeposited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositBinId, documentsDeposited);
    }
}
