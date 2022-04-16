package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.DataObjectsContainer;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyCode;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyExponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;

import java.util.Objects;
import java.util.StringJoiner;

public final class CurrencyTableEntry extends DataObjectsContainer {
    public static final int STRING_LENGTH = 24;
    private final int currencyType;
    private final CompositeTlv<String> dataObjects;

    public CurrencyTableEntry(int currencyType, CompositeTlv<String> dataObjects) {
        this.currencyType = Integers.validateHexRange(currencyType, 1, 0xFF, "'Entry Type'");
        this.dataObjects = validateDataObjects(dataObjects, TransactionCurrencyCode.TAG, TransactionCurrencyExponent.TAG);
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public CompositeTlv<String> getDataObjects() {
        return dataObjects;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrencyTableEntry.class.getSimpleName() + ": {", "}")
                .add("currencyType: " + currencyType)
                .add("dataObjects: " + dataObjects)
                .toString();
    }

    @Override
    public String toNdcString() {
        return new NdcStringBuilder(STRING_LENGTH)
                .appendZeroPaddedHex(currencyType, 2)
                .appendComponent(dataObjects)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyTableEntry that = (CurrencyTableEntry) o;
        return currencyType == that.currencyType && dataObjects.equals(that.dataObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyType, dataObjects);
    }

}
