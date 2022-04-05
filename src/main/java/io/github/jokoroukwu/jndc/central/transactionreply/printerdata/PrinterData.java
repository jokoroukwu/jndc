package io.github.jokoroukwu.jndc.central.transactionreply.printerdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class PrinterData implements NdcComponent {
    private final PrinterFlag printerFlag;
    private final String data;

    public PrinterData(PrinterFlag printerFlag, String data) {
        this.printerFlag = ObjectUtils.validateNotNull(printerFlag, "Printer Flag");
        this.data = ObjectUtils.validateNotNull(data, "Data");
    }

    //  no-validation constructor
    //  is used internally by the corresponding reader
    public PrinterData(PrinterFlag printerFlag, String data, Void unused) {
        this.printerFlag = printerFlag;
        this.data = data;
    }

    public PrinterFlag getPrinterFlag() {
        return printerFlag;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PrinterData.class.getSimpleName() + ": {", "}")
                .add("printerFlag: " + printerFlag)
                .add("data: '" + data + '\'')
                .toString();
    }

    @Override
    public String toNdcString() {
        return printerFlag.getValue() + data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrinterData that = (PrinterData) o;
        return printerFlag == that.printerFlag && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(printerFlag, data);
    }
}
