package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.ArrayUtils;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class PrinterDataList extends LimitedSizeList<PrinterData> implements NdcComponent {
    public static final int MAX_SIZE = 13;

    public PrinterDataList() {
        this(10);
    }

    public PrinterDataList(int capacity) {
        super(MAX_SIZE, capacity);
    }

    public PrinterDataList(Collection<? extends PrinterData> collection) {
        super(MAX_SIZE, collection);
    }

    PrinterDataList(ArrayList<PrinterData> listDelegate) {
        super(MAX_SIZE, listDelegate);
    }

    public static PrinterDataList unmodifiable(PrinterDataList printerDataList) {
        return new PrinterDataList(List.copyOf(printerDataList));
    }

    public static PrinterDataList of(PrinterData... printerData) {
        if (ArrayUtils.isNullOrEmpty(printerData)) {
            throw new IllegalArgumentException("Printer Data cannot be null or empty");
        }
        final PrinterDataList printerDataList = new PrinterDataList(printerData.length);
        printerDataList.addAll(Arrays.asList(printerData));
        return printerDataList;
    }

    @Override
    protected void performElementChecks(PrinterData element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            return Strings.EMPTY_STRING;
        }
        //  printer data may contain up to at least 500 bytes
        //  so enough space should be allocated first
        return new NdcStringBuilder(252 * size())
                .appendComponents(this, NdcConstants.GROUP_SEPARATOR_STRING)
                .toString();
    }
}
