package io.github.jokoroukwu.jndc.central.transactionreply.depositlimit;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Currencies;
import io.github.jokoroukwu.jndc.util.Integers;

import java.util.Objects;
import java.util.StringJoiner;

public class AmountLimit implements NdcComponent {
    private final String currencyCode;
    private final int value;

    public AmountLimit(String currencyCode, int value) {
        this.currencyCode = Currencies.validateIso4217CurrencyCode(currencyCode);
        this.value = Integers.validateNotNegative(value, "Amount Limit Value");
    }

    AmountLimit(String currencyCode, int value, Void unused) {
        this.currencyCode = currencyCode;
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toNdcString() {
        return ElementId.AMOUNT_LIMIT.toNdcString() + currencyCode + value + ".00";
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountLimit.class.getSimpleName() + ": {", "}")
                .add("currencyCode: '" + currencyCode + '\'')
                .add("value: " + value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountLimit that = (AmountLimit) o;
        return value == that.value && currencyCode.equals(that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, value);
    }

}
