package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class BarcodeData implements NdcComponent {
    public static final int UNKNOWN_FORMAT = 0;
    public static final String RESERVED_FIELD = "00";

    private final int barCodeFormatId;
    private final String scannedBarCodeData;

    BarcodeData(int barCodeFormatId, String scannedBarCodeData, Void unused) {
        this.barCodeFormatId = barCodeFormatId;
        this.scannedBarCodeData = scannedBarCodeData;
    }

    public BarcodeData(int barCodeFormatId, String scannedBarCodeData) {
        this.barCodeFormatId = validateBarcodeFormat(barCodeFormatId);
        this.scannedBarCodeData = ObjectUtils.validateNotNull(scannedBarCodeData, "Scanned Barcode Data cannot be null");
    }

    public BarcodeData(int barCodeFormatId) {
        this(barCodeFormatId, Strings.EMPTY_STRING);
    }

    public BarcodeData(String scannedBarCodeData) {
        this(UNKNOWN_FORMAT, scannedBarCodeData);
    }

    public int getBarCodeFormatId() {
        return barCodeFormatId;
    }

    public String getScannedBarCodeData() {
        return scannedBarCodeData;
    }

    @Override
    public String toNdcString() {
        return new StringBuilder(6 + scannedBarCodeData.length())
                .append(Integers.toZeroPaddedHexString(barCodeFormatId, 4))
                .append(RESERVED_FIELD)
                .append(scannedBarCodeData)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BarcodeData.class.getSimpleName() + ": {", "}")
                .add("barCodeFormatId: " + barCodeFormatId)
                .add("scannedBarCodeData: '" + scannedBarCodeData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarcodeData barCode = (BarcodeData) o;
        return barCodeFormatId == barCode.barCodeFormatId && scannedBarCodeData.equals(barCode.scannedBarCodeData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barCodeFormatId, scannedBarCodeData);
    }

    private int validateBarcodeFormat(int value) {
        if (value < 0 || value > 0xFFFF) {
            final String template = "'Barcode Format identifier' should be within valid range (0x00-0xFFFF) but was: %#X";
            throw new IllegalArgumentException(String.format(template, value));
        }
        return value;
    }
}
