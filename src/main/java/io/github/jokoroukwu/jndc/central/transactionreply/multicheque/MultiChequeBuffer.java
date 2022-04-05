package io.github.jokoroukwu.jndc.central.transactionreply.multicheque;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.CollectionUtils;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class MultiChequeBuffer implements IdentifiableBuffer {
    public static final char ID = 'b';
    private final List<Cheque> cheques;

    MultiChequeBuffer(List<Cheque> cheques) {
        this.cheques = cheques;
    }

    public MultiChequeBuffer(Collection<Cheque> cheques) {
        this.cheques = List.copyOf(CollectionUtils.requireNonNullNonEmpty(cheques, "Cheque list"));
    }

    public static MultiChequeBuffer of(Cheque... cheques) {
        return new MultiChequeBuffer(List.of(cheques));
    }

    public List<Cheque> getCheques() {
        return cheques;
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        //  allocate enough space for cheque data
        return new NdcStringBuilder(60 * cheques.size())
                .append(ID)
                .appendComponents(cheques, NdcConstants.GROUP_SEPARATOR_STRING)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MultiChequeBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("cheques: " + cheques)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiChequeBuffer that = (MultiChequeBuffer) o;
        return cheques.equals(that.cheques);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, cheques);
    }
}
