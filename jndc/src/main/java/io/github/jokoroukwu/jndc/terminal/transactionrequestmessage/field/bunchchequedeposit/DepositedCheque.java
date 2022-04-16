package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Objects;
import java.util.StringJoiner;

public class DepositedCheque implements NdcComponent {
    //  Derived Cheque Amount should always be zero
    public static final long DERIVED_CHEQUE_AMOUNT = 0;

    private final int codeLineLength;
    private final int chequeId;
    private final long customerChequeAmount;
    private final long derivedChequeAmount;
    private final String codeLineData;

    DepositedCheque(int chequeId, long customerChequeAmount, long derivedChequeAmount, String codeLineData) {
        this.chequeId = chequeId;
        this.customerChequeAmount = customerChequeAmount;
        this.derivedChequeAmount = derivedChequeAmount;
        this.codeLineData = codeLineData;
        this.codeLineLength = codeLineData.length();
    }

    public DepositedCheque(int chequeId, long customerChequeAmount, String codeLineData) {
        this.chequeId = validateChequeId(chequeId);
        this.customerChequeAmount = validateCustomerChequeAmount(customerChequeAmount);
        this.codeLineData = validateCodeLineData(codeLineData);
        this.codeLineLength = codeLineData.length();
        this.derivedChequeAmount = DERIVED_CHEQUE_AMOUNT;
    }

    public DepositedCheque(int chequeId, long customerChequeAmount) {
        this(chequeId, customerChequeAmount, Strings.EMPTY_STRING);
    }

    public int getChequeId() {
        return chequeId;
    }

    public long getCustomerChequeAmount() {
        return customerChequeAmount;
    }

    public long getDerivedChequeAmount() {
        return derivedChequeAmount;
    }

    public int getCodeLineLength() {
        return codeLineLength;
    }

    public String getCodeLineData() {
        return codeLineData;
    }

    @Override
    public String toNdcString() {
        return new StringBuilder(30 + codeLineLength)
                .append(Integers.toZeroPaddedString(chequeId, 3))
                .append(Longs.toZeroPaddedString(customerChequeAmount, 12))
                .append(Longs.toZeroPaddedString(derivedChequeAmount, 12))
                .append(Integers.toZeroPaddedString(codeLineLength, 3))
                .append(codeLineData)
                .toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DepositedCheque.class.getSimpleName() + ": {", "}")
                .add("codeLineLength: " + codeLineLength)
                .add("chequeId: " + chequeId)
                .add("customerChequeAmount: " + customerChequeAmount)
                .add("derivedChequeAmount: " + derivedChequeAmount)
                .add("codeLineData: '" + codeLineData + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositedCheque that = (DepositedCheque) o;
        return codeLineLength == that.codeLineLength &&
                chequeId == that.chequeId &&
                customerChequeAmount == that.customerChequeAmount &&
                derivedChequeAmount == that.derivedChequeAmount &&
                codeLineData.equals(that.codeLineData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeLineLength, chequeId, customerChequeAmount, derivedChequeAmount, codeLineData);
    }

    private int validateChequeId(int chequeId) {
        if (chequeId < 1 || chequeId > 999) {
            final String message = "'Cheque Identifier' should be within valid range (1-999 dec) but was: " + chequeId;
            throw new IllegalArgumentException(message);
        }
        return chequeId;
    }

    private long validateCustomerChequeAmount(long value) {
        if (value < 0 || value > 999999999999L) {
            final String message =
                    "'Customer Cheque Amount' value should be within valid range (0-999999999999 dec) but was: " + value;
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private String validateCodeLineData(String codeLineData) {
        if (codeLineData == null) {
            throw new IllegalArgumentException("'Codeline Data' may not be null");
        }
        final int length = codeLineData.length();
        if (length > 999) {
            final String message = "'Codeline Length' should be within valid range (0-999 dec) but was: " + length;
            throw new IllegalArgumentException(message);
        }
        return codeLineData;
    }
}
