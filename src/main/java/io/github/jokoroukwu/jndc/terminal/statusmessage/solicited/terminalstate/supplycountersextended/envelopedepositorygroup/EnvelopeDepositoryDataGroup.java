package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.envelopedepositorygroup;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended.IdentifiableCounterGroup;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public class EnvelopeDepositoryDataGroup implements IdentifiableCounterGroup {
    public static final char ID = 'F';

    private final int envelopesDeposited;
    private final int lastEnvelopeSerialNumber;

    public EnvelopeDepositoryDataGroup(int envelopesDeposited, int lastEnvelopeSerialNumber) {
        this.envelopesDeposited = Integers.validateRange(envelopesDeposited, 0, 99999, "'Envelopes Deposited'");
        this.lastEnvelopeSerialNumber = Integers.validateRange(lastEnvelopeSerialNumber, 0, 99999,
                "'Last Envelope Serial Number'");
    }

    EnvelopeDepositoryDataGroup(int envelopesDeposited, int lastEnvelopeSerialNumber, Void unused) {
        this.envelopesDeposited = envelopesDeposited;
        this.lastEnvelopeSerialNumber = lastEnvelopeSerialNumber;
    }

    public int getEnvelopesDeposited() {
        return envelopesDeposited;
    }

    public int getLastEnvelopeSerialNumber() {
        return lastEnvelopeSerialNumber;
    }

    @Override
    public char getGroupId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(11)
                .append(ID)
                .appendZeroPadded(envelopesDeposited, 5)
                .appendZeroPadded(lastEnvelopeSerialNumber, 5)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EnvelopeDepositoryDataGroup.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("envelopesDeposited: " + envelopesDeposited)
                .add("lastEnvelopeSerialNumber: " + lastEnvelopeSerialNumber)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvelopeDepositoryDataGroup that = (EnvelopeDepositoryDataGroup) o;
        return envelopesDeposited == that.envelopesDeposited && lastEnvelopeSerialNumber == that.lastEnvelopeSerialNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(envelopesDeposited, lastEnvelopeSerialNumber);
    }
}
