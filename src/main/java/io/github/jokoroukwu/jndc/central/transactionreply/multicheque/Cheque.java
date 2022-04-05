package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class Cheque implements NdcComponent {
    private final int chequeId;
    private final int chequeDestination;
    private final ChequeStamp chequeStamp;
    private final String chequeEndorseText;

    public Cheque(int chequeId, int chequeDestination, ChequeStamp chequeStamp, String chequeEndorseText) {
        this.chequeId = Integers.validateRange(chequeId, 0, 999, "Cheque Identifier");
        this.chequeDestination = Integers.validateRange(chequeDestination, 0, 15, "Cheque Destination");
        this.chequeStamp = ObjectUtils.validateNotNull(chequeStamp, "Cheque Stamp");
        this.chequeEndorseText = ObjectUtils.validateNotNull(chequeEndorseText, "Cheque Endorse Text");
    }

    public Cheque(int chequeId, int chequeDestination, ChequeStamp chequeStamp, String chequeEndorseText, Void unused) {
        this.chequeId = chequeId;
        this.chequeDestination = chequeDestination;
        this.chequeStamp = chequeStamp;
        this.chequeEndorseText = chequeEndorseText;
    }

    public int getChequeId() {
        return chequeId;
    }

    public int getChequeDestination() {
        return chequeDestination;
    }

    public ChequeStamp getChequeStamp() {
        return chequeStamp;
    }

    public String getChequeEndorseText() {
        return chequeEndorseText;
    }

    @Override
    public String toNdcString() {
        //  capacity can be evaluated to exact number
        return new NdcStringBuilder(10 + chequeEndorseText.length())
                .appendZeroPadded(chequeId, 3)
                .appendZeroPadded(chequeDestination, 2)
                .append(chequeStamp.getValue())
                //  reserved bytes
                .append("0000")
                .append(chequeEndorseText)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cheque.class.getSimpleName() + ": {", "}")
                .add("chequeId: " + chequeId)
                .add("chequeDestination: " + chequeDestination)
                .add("chequeStamp: " + chequeStamp)
                .add("chequeEndorseText: '" + chequeEndorseText + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cheque cheque = (Cheque) o;
        return chequeId == cheque.chequeId &&
                chequeDestination == cheque.chequeDestination &&
                chequeStamp == cheque.chequeStamp &&
                chequeEndorseText.equals(cheque.chequeEndorseText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chequeId, chequeDestination, chequeStamp, chequeEndorseText);
    }
}
