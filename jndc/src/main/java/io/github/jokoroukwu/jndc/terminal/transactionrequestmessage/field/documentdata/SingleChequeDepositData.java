package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public final class SingleChequeDepositData implements NdcComponent {
    public static final SingleChequeDepositData EMPTY = new SingleChequeDepositData(Strings.EMPTY_STRING);
    private final boolean codeLineDetected;
    private final String codeLineValue;

    SingleChequeDepositData(String codeLineValue, Void unused) {
        this.codeLineValue = codeLineValue;
        codeLineDetected = !codeLineValue.isEmpty();
    }

    public SingleChequeDepositData(String codeLineValue) {
        this.codeLineValue = validateCodeLineValue(codeLineValue);
        codeLineDetected = !codeLineValue.isEmpty();
    }

    public boolean isCodeLineDetected() {
        return codeLineDetected;
    }

    public String getCodeLineValue() {
        return codeLineValue;
    }


    @Override
    public String toNdcString() {
        return (codeLineDetected ? '1' : '0') + codeLineValue;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SingleChequeDepositData.class.getSimpleName() + ": {", "}")
                .add("codeLineDetected: " + codeLineDetected)
                .add("codeLineValue: '" + codeLineValue + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleChequeDepositData that = (SingleChequeDepositData) o;
        return codeLineDetected == that.codeLineDetected && codeLineValue.equals(that.codeLineValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeLineDetected, codeLineValue);
    }

    private String validateCodeLineValue(String codeLineValue) {
        if (codeLineValue == null) {
            throw new IllegalArgumentException("'Codeline value' may not be null");
        }
        if (codeLineValue.length() > 256) {
            throw new IllegalArgumentException("'Codeline value' may contain up to 256 characters but was: " + codeLineValue);
        }
        return codeLineValue;
    }

}
