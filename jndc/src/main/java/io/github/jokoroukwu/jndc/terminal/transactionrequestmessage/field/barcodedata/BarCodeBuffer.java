package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBuffer;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Represents data ID ‘e’field.
 * <br>
 * The field is optional
 */
public final class BarCodeBuffer implements IdentifiableBuffer {
    public static final char ID = 'e';
    public static final BarCodeBuffer EMPTY = new BarCodeBuffer(null);

    private final BarcodeData barcodeData;

    public BarCodeBuffer(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
    }

    public Optional<BarcodeData> getBarcodeData() {
        return Optional.ofNullable(barcodeData);
    }

    @Override
    public char getId() {
        return ID;
    }

    @Override
    public String toNdcString() {
        return barcodeData != null
                ? ID + barcodeData.toNdcString()
                : Character.toString(ID);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BarCodeBuffer.class.getSimpleName() + ": {", "}")
                .add("id: '" + ID + '\'')
                .add("barCode: " + barcodeData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarCodeBuffer that = (BarCodeBuffer) o;
        return Objects.equals(barcodeData, that.barcodeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, barcodeData);
    }
}
