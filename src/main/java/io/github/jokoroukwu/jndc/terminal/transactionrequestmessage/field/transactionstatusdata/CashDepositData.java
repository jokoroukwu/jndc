package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.transactionstatusdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.*;

public class CashDepositData implements NdcComponent {
    private final DepositTransactionDirection depositDirection;
    private final CashDepositNotes cashDepositNotes;
    private final List<RecycleCassette> recycleCassettes;

    CashDepositData(DepositTransactionDirection depositDirection,
                    CashDepositNotes cashDepositNotes,
                    List<RecycleCassette> recycleCassettes,
                    Void unused) {
        this.depositDirection = depositDirection;
        this.recycleCassettes = recycleCassettes;
        this.cashDepositNotes = cashDepositNotes;
    }

    public CashDepositData(DepositTransactionDirection depositDirection,
                           CashDepositNotes cashDepositNotes,
                           Collection<RecycleCassette> recycleCassettes) {
        this.depositDirection = ObjectUtils.validateNotNull(depositDirection, "Last Cash Deposit Transaction Direction cannot be null");
        this.recycleCassettes = validateRecycleCassettes(recycleCassettes);
        this.cashDepositNotes = cashDepositNotes;
    }

    public CashDepositData(DepositTransactionDirection depositDirection) {
        this(depositDirection, null, null);
    }

    public DepositTransactionDirection getDepositDirection() {
        return depositDirection;
    }

    public Optional<CashDepositNotes> getExtendedNoteData() {
        return Optional.ofNullable(cashDepositNotes);
    }

    public Optional<List<RecycleCassette>> getRecycleCassettes() {
        return Optional.ofNullable(recycleCassettes);
    }

    @Override
    public String toNdcString() {
        final int capacity = 1 + (cashDepositNotes != null ? 20 : 0)
                + (recycleCassettes != null ? (recycleCassettes.size() * 6) + 2 : 0);

        final StringBuilder builder = new StringBuilder(capacity)
                .append(depositDirection.value());
        if (cashDepositNotes != null) {
            builder.append(cashDepositNotes.toNdcString());
        }
        appendRecycleCassettes(builder);
        return builder.toString();
    }


    private void appendRecycleCassettes(StringBuilder builder) {
        if (recycleCassettes != null) {
            builder.append(Integers.toZeroPaddedString(recycleCassettes.size(), 2));
            for (RecycleCassette recycleCassette : recycleCassettes) {
                builder.append(recycleCassette.toNdcString());
            }
        }
    }

    private List<RecycleCassette> validateRecycleCassettes(Collection<RecycleCassette> recycleCassettes) {
        if (recycleCassettes == null) {
            return null;
        }
        final int size = recycleCassettes.size();
        if (size > 99) {
            throw new IllegalArgumentException(size + " is not within valid 'Number of recycle cassettes' range (0-99 decimal)");
        }
        return List.copyOf(recycleCassettes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CashDepositData.class.getSimpleName() + ": {", "}")
                .add("depositDirection: " + depositDirection)
                .add("cashDepositNotes: " + cashDepositNotes)
                .add("recycleCassettes: " + recycleCassettes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashDepositData that = (CashDepositData) o;
        return depositDirection == that.depositDirection &&
                Objects.equals(cashDepositNotes, that.cashDepositNotes) &&
                Objects.equals(recycleCassettes, that.recycleCassettes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositDirection, cashDepositNotes, recycleCassettes);
    }
}
