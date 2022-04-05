package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.coinhoppers;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessage;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.*;

/**
 * Represents the Data ID ‘f’ field.
 * When used in {@link TransactionRequestMessage}
 * this field indicates that more than four coin
 * hopper types are being reported.
 * <br>
 * The field is optional.
 */
public final class CoinHoppersBuffer implements IdentifiableBuffer {
    public static final char ID = 'f';
    public static final CoinHoppersBuffer EMPTY = new CoinHoppersBuffer(Collections.emptyList());
    private final List<Integer> coinsDispensed;

    /**
     * Constructs a new instance.
     * Each element in the coinsDispensed collection
     * represents the corresponding hopper type.
     * <br>
     * Depending on the number of hopper types present,
     * size should be between 5 and 8.
     * <br>
     * If the collection's size does not fit in the above range
     * an {@link IllegalArgumentException} will be thrown.
     *
     * @param coinsDispensed coins dispensed for each hopper type present
     */
    public CoinHoppersBuffer(Collection<Integer> coinsDispensed) {
        this.coinsDispensed = copyIfValid(coinsDispensed);
    }

    CoinHoppersBuffer(List<Integer> coinsDispensed) {
        this.coinsDispensed = coinsDispensed;
    }

    public List<Integer> getCoinsDispensed() {
        return coinsDispensed;
    }

    public OptionalInt coinsDispensedForHopperType(int hopperType) {
        return hopperType >= 0 && hopperType < coinsDispensed.size()
                ? OptionalInt.of(coinsDispensed.get(hopperType))
                : OptionalInt.empty();
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder(1 + coinsDispensed.size() * 2)
                .append(ID);
        for (Integer value : coinsDispensed) {
            builder.append(Integers.toZeroPaddedString(value, 2));
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CoinHoppersBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("coinsDispensed: " + coinsDispensed)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinHoppersBuffer that = (CoinHoppersBuffer) o;
        return coinsDispensed.equals(that.coinsDispensed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, coinsDispensed);
    }

    private List<Integer> copyIfValid(Collection<Integer> coinsDispensed) {
        if (coinsDispensed == null) {
            throw new IllegalArgumentException("'Coins Dispensed' must not be null");
        }
        if (coinsDispensed.isEmpty()) {
            return Collections.emptyList();
        }
        final int size = coinsDispensed.size();
        if (size < 5 || size > 8) {
            final String errorMessage = "Number of hopper types should be in range (5-8) but actual was: %d";
            throw new IllegalArgumentException(String.format(errorMessage, size));
        }
        final List<Integer> copy = new ArrayList<>(size);
        for (Integer value : coinsDispensed) {
            if (value == null || value < 0 || value > 99) {
                final String errorMessage = "'Coins Dispensed' value should be within valid range (0-99 dec) but actual was: %s";
                throw new IllegalArgumentException(errorMessage);
            }
            copy.add(value);
        }
        return Collections.unmodifiableList(copy);
    }
}
