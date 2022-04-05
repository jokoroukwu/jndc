package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class DepositLimitsBuffer implements IdentifiableBuffer {
    public static final char ID = 'd';
    private final List<AmountLimit> amountLimits;
    private final List<Integer> noteLimits;

    public DepositLimitsBuffer(Collection<AmountLimit> amountLimits, Collection<Integer> noteLimits) {
        validateLimits(amountLimits, noteLimits);
        this.amountLimits = List.copyOf(amountLimits);
        this.noteLimits = copyOnValidation(noteLimits);
    }

    DepositLimitsBuffer(List<AmountLimit> amountLimits, List<Integer> noteLimits) {
        this.amountLimits = amountLimits;
        this.noteLimits = noteLimits;
    }

    public DepositLimitsBuffer(Collection<AmountLimit> amountLimits, int noteLimit) {
        this.amountLimits = List.copyOf(amountLimits);
        this.noteLimits = List.of(Integers.validateRange(noteLimit, 0, 999, "Note Limit value"));
    }

    public DepositLimitsBuffer(int noteLimit) {
        this(List.of(), noteLimit);
    }

    public DepositLimitsBuffer(AmountLimit amountLimit) {
        this(List.of(amountLimit), List.of());
    }

    public List<AmountLimit> getAmountLimits() {
        return amountLimits;
    }

    public List<Integer> getNoteLimits() {
        return noteLimits;
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        //  evaluate average capacity
        final NdcStringBuilder builder = new NdcStringBuilder(5 * (amountLimits.size() + noteLimits.size()) + 11)
                .append(ID)
                //  reserved bytes
                .append("0000000000")
                .appendComponents(amountLimits, NdcConstants.GROUP_SEPARATOR_STRING);
        if (!noteLimits.isEmpty()) {
            if (!amountLimits.isEmpty()) {
                builder.appendGs();
            }
            builder.append(ElementId.NOTE_LIMIT.toNdcString())
                    .appendZeroPadded(noteLimits, NdcConstants.GROUP_SEPARATOR_STRING + ElementId.NOTE_LIMIT.toNdcString(), 3);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DepositLimitsBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("amountLimits: " + amountLimits)
                .add("noteLimits: " + noteLimits)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositLimitsBuffer that = (DepositLimitsBuffer) o;
        return amountLimits.equals(that.amountLimits) && noteLimits.equals(that.noteLimits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, amountLimits, noteLimits);
    }


    private void validateLimits(Collection<AmountLimit> amountLimits, Collection<Integer> noteLimits) {
        ObjectUtils.validateNotNull(amountLimits, "Amount Limits");
        ObjectUtils.validateNotNull(noteLimits, "Note Limits");
        if (amountLimits.isEmpty() && noteLimits.isEmpty()) {
            throw new IllegalArgumentException("At least single limit must be provided");
        }
    }

    private List<Integer> copyOnValidation(Collection<Integer> noteLimits) {
        final List<Integer> copy = new ArrayList<>(noteLimits.size());
        for (Integer noteLimit : noteLimits) {
            ObjectUtils.validateNotNull(noteLimit, "Note Limit value");
            Integers.validateRange(noteLimit, 0, 999, "Note Limit value");
            copy.add(noteLimit);
        }
        return Collections.unmodifiableList(copy);
    }
}
