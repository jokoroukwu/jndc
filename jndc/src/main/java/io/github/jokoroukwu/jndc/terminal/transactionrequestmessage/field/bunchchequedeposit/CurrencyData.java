package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Currencies;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.Longs;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class CurrencyData implements NdcComponent {
    public static final String RESERVED_FIELD = "0000";

    private final String depositCurrency;
    private final char amountExponentSign;
    private final int amountExponentValue;
    private final long totalCustomerAmount;
    private final long totalDerivedAmount;
    private final List<DepositedCheque> depositedCheques;

    public CurrencyData(String depositCurrency,
                        char amountExponentSign,
                        int amountExponentValue,
                        long totalCustomerAmount,
                        Collection<DepositedCheque> depositedCheques,
                        long totalDerivedAmount) {
        this.amountExponentSign = validateSign(amountExponentSign);
        this.amountExponentValue = validateExponentValue(amountExponentValue);
        this.depositCurrency = Currencies.validateIso4217CurrencyCode(depositCurrency);
        this.totalCustomerAmount = validateTotalCustomerAmount(totalCustomerAmount);
        this.depositedCheques = copyChequesIfValid(depositedCheques);
        this.totalDerivedAmount = totalDerivedAmount;
    }

    public CurrencyData(String depositCurrency,
                        char amountExponentSign,
                        int amountExponentValue,
                        long totalCustomerAmount,
                        Collection<DepositedCheque> depositedCheques) {
        this(depositCurrency, amountExponentSign, amountExponentValue, totalCustomerAmount, depositedCheques, 0);
    }

    CurrencyData(String depositCurrency,
                 char amountExponentSign,
                 int amountExponentValue,
                 long totalCustomerAmount,
                 List<DepositedCheque> depositedCheques,
                 long totalDerivedAmount) {
        this.amountExponentSign = amountExponentSign;
        this.amountExponentValue = amountExponentValue;
        this.depositCurrency = depositCurrency;
        this.totalCustomerAmount = totalCustomerAmount;
        this.depositedCheques = depositedCheques;
        this.totalDerivedAmount = totalDerivedAmount;
    }

    public static CurrencyDataBuilder builder() {
        return new CurrencyDataBuilder();
    }

    public String getDepositCurrency() {
        return depositCurrency;
    }

    public char getAmountExponentSign() {
        return amountExponentSign;
    }

    public int getAmountExponentValue() {
        return amountExponentValue;
    }

    public long getTotalCustomerAmount() {
        return totalCustomerAmount;
    }

    public long getTotalDerivedAmount() {
        return totalDerivedAmount;
    }

    public List<DepositedCheque> getChequeInfoList() {
        return depositedCheques;
    }

    @Override
    public String toNdcString() {
        final StringBuilder builder = new StringBuilder(150)
                .append(depositCurrency)
                .append(amountExponentSign)
                .append(Integers.toZeroPaddedString(amountExponentValue, 2))
                .append(Longs.toZeroPaddedString(totalCustomerAmount, 12))
                .append(Longs.toZeroPaddedString(totalDerivedAmount, 12))
                .append(RESERVED_FIELD);

        for (DepositedCheque depositedCheque : depositedCheques) {
            builder.append(depositedCheque.toNdcString())
                    .append(NdcConstants.GROUP_SEPARATOR);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrencyData.class.getSimpleName() + ": {", "}")
                .add("depositCurrency: '" + depositCurrency + "'")
                .add("amountExponentSign: " + amountExponentSign)
                .add("amountExponentValue: " + amountExponentValue)
                .add("totalCustomerAmount: " + totalCustomerAmount)
                .add("totalDerivedAmount: " + totalDerivedAmount)
                .add("chequeInfoList: " + depositedCheques)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyData that = (CurrencyData) o;
        return amountExponentSign == that.amountExponentSign &&
                amountExponentValue == that.amountExponentValue &&
                totalCustomerAmount == that.totalCustomerAmount &&
                totalDerivedAmount == that.totalDerivedAmount &&
                depositCurrency.equals(that.depositCurrency) &&
                depositedCheques.equals(that.depositedCheques);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositCurrency,
                amountExponentSign,
                amountExponentValue,
                totalCustomerAmount,
                totalDerivedAmount,
                depositedCheques);
    }

    private char validateSign(char sign) {
        if (sign == '+' || sign == '-') {
            return sign;
        }
        throw new IllegalArgumentException("'Amount Exponent Sign' should be '-' or '+' but was: " + sign);
    }

    private int validateExponentValue(int exponentValue) {
        if (exponentValue < 0 || exponentValue > 99) {
            final String message = "'Amount Exponent Value' should be within valid range (0-99 dec) but was: " + exponentValue;
            throw new IllegalArgumentException(message);
        }
        return exponentValue;
    }

    private long validateTotalCustomerAmount(long value) {
        if (value < 0 || value > 999_999_999_999L) {
            final String message = "'Total Customer Amount' should be within valid range (0-999999999999 dec) but was: " + value;
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private List<DepositedCheque> copyChequesIfValid(Collection<DepositedCheque> depositedCheque) {
        if (depositedCheque == null) {
            throw new IllegalArgumentException("Deposited Cheque Data must not be null");
        }
        if (depositedCheque.size() < 1) {
            throw new IllegalArgumentException("At least one cheque must be present in Deposited Cheque Data");
        }
        return List.copyOf(depositedCheque);
    }
}
