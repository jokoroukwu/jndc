package io.github.jokoroukwu.jndc.central.transactionreply.transactionupdate;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable.CurrencyTableEntry;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable.TransactionTableEntry;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class TransactionUpdateBuffer implements IdentifiableBuffer {
    public static final char ID = 'c';
    private final CurrencyTableEntry currencyTableEntry;
    private final TransactionTableEntry transactionTableEntry;
    private final TargetBufferId targetBufferId;
    private final String bufferData;

    public TransactionUpdateBuffer(TargetBufferId targetBufferId, String bufferData, CurrencyTableEntry currencyTableEntry,
                                   TransactionTableEntry transactionTableEntry) {
        this.currencyTableEntry = currencyTableEntry;
        this.transactionTableEntry = transactionTableEntry;
        this.targetBufferId = ObjectUtils.validateNotNull(targetBufferId, "Target Buffer Identifier");
        this.bufferData = validateBufferData(targetBufferId, bufferData);
    }

    public static String errorMessageOnInvalidBufferData(TargetBufferId targetBufferId, String bufferData) {
        switch (targetBufferId) {
            case NO_BUFFER: {
                return checkLength(0, bufferData, targetBufferId);
            }
            case BUFFER_B:
            case BUFFER_C: {
                return checkLength(32, bufferData, targetBufferId);
            }
            default: {
                return checkLength(8, 12, bufferData, targetBufferId);
            }
        }
    }

    private static String checkLength(int expectedLength, String data, TargetBufferId targetBufferId) {
        if (data.length() != expectedLength) {
            return String.format("'Buffer Length' should be %d when 'Target Buffer Identifier' is %s",
                    expectedLength, targetBufferId);
        }
        return Strings.EMPTY_STRING;
    }

    private static String checkLength(int expectedLength, int alternativeLength, String data, TargetBufferId targetBufferId) {
        if (!(data.length() == expectedLength || data.length() == alternativeLength)) {
            return String.format("'Buffer Length' should be %d or %d when 'Target Buffer Identifier' is %s",
                    expectedLength, alternativeLength, targetBufferId);

        }
        return Strings.EMPTY_STRING;
    }

    public TargetBufferId getTargetBufferId() {
        return targetBufferId;
    }

    public Optional<CurrencyTableEntry> getCurrencyTableEntry() {
        return Optional.ofNullable(currencyTableEntry);
    }

    public Optional<TransactionTableEntry> getTransactionTableEntry() {
        return Optional.ofNullable(transactionTableEntry);
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransactionUpdateBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + "'")
                .add("currencyTableEntry: " + currencyTableEntry)
                .add("transactionTableEntry: " + transactionTableEntry)
                .add("targetBufferId: " + targetBufferId)
                .add("bufferData: '" + bufferData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionUpdateBuffer that = (TransactionUpdateBuffer) o;
        return Objects.equals(currencyTableEntry, that.currencyTableEntry) &&
                Objects.equals(transactionTableEntry, that.transactionTableEntry) &&
                targetBufferId == that.targetBufferId && bufferData.equals(that.bufferData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, currencyTableEntry, transactionTableEntry, targetBufferId, bufferData);
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder()
                .append(ID)
                .append(entryToString(currencyTableEntry))
                .append(entryToString(transactionTableEntry))
                .append(targetBufferId.getValue());
        if (!bufferData.isEmpty()) {
            builder.append(Integers.toEvenLengthHexString(bufferData.length()))
                    .append(bufferData);
        }
        return builder.toString();
    }

    private String entryToString(NdcComponent ndcComponent) {
        return ndcComponent != null ? ndcComponent.toNdcString() : "00";
    }

    private String validateBufferData(TargetBufferId targetBufferId, String bufferData) {
        ObjectUtils.validateNotNull(bufferData, "Buffer Data");
        final String errorMessage = errorMessageOnInvalidBufferData(targetBufferId, bufferData);
        if (errorMessage.isEmpty()) {
            return bufferData;
        }
        throw new IllegalArgumentException(errorMessage);
    }
}
