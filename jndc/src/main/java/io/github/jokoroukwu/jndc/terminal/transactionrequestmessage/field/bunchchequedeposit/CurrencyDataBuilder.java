package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CurrencyDataBuilder {
    private String depositCurrency;
    private char amountExponentSign = '+';
    private int amountExponentValue;
    private long totalCustomerAmount;
    private List<DepositedCheque> depositedCheques;


    public CurrencyDataBuilder withDepositCurrency(String depositCurrency) {
        this.depositCurrency = depositCurrency;
        return this;
    }

    public CurrencyDataBuilder withPositiveExponentSign() {
        this.amountExponentSign = '+';
        return this;
    }

    public CurrencyDataBuilder withNegativeExponentSign() {
        this.amountExponentSign = '-';
        return this;
    }

    public CurrencyDataBuilder withAmountExponentValue(int amountExponentValue) {
        this.amountExponentValue = amountExponentValue;
        return this;
    }

    public CurrencyDataBuilder withTotalCustomerAmount(long totalCustomerAmount) {
        this.totalCustomerAmount = totalCustomerAmount;
        return this;
    }

    public CurrencyDataBuilder withDepositedCheques(List<DepositedCheque> depositedCheques) {
        this.depositedCheques = depositedCheques;
        return this;
    }

    public CurrencyDataBuilder addDepositedCheque(DepositedCheque depositedCheque) {
        ObjectUtils.validateNotNull(depositedCheque, "depositedCheques");
        lazyInitList();
        depositedCheques.add(depositedCheque);
        return this;
    }

    private void lazyInitList() {
        if (depositedCheques == null) {
            depositedCheques = new LinkedList<>();
        }
    }

    public CurrencyData build() {
        return new CurrencyData(depositCurrency,
                amountExponentSign,
                amountExponentValue,
                totalCustomerAmount,
                depositedCheques);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrencyDataBuilder.class.getSimpleName() + ": {", "}")
                .add("depositCurrency: '" + depositCurrency + "'")
                .add("amountExponentSign: " + amountExponentSign)
                .add("amountExponentValue: " + amountExponentValue)
                .add("totalCustomerAmount: " + totalCustomerAmount)
                .add("depositedCheques: " + depositedCheques)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyDataBuilder that = (CurrencyDataBuilder) o;
        return amountExponentSign == that.amountExponentSign &&
                amountExponentValue == that.amountExponentValue &&
                totalCustomerAmount == that.totalCustomerAmount &&
                Objects.equals(depositCurrency, that.depositCurrency) &&
                Objects.equals(depositedCheques, that.depositedCheques);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositCurrency,
                amountExponentSign,
                amountExponentValue,
                totalCustomerAmount,
                depositedCheques);
    }
}
