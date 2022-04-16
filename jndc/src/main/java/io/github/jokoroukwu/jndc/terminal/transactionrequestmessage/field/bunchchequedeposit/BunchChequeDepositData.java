package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.bunchchequedeposit;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.NdcConstants;

import java.util.*;

public final class BunchChequeDepositData implements NdcComponent {
    public static final String RESERVED_FIELD = "0000";
    private final int totalChequesToReturn;
    private final List<CurrencyData> currencyDataList;

    public BunchChequeDepositData(int totalChequesToReturn, Collection<CurrencyData> currencyData) {
        this.totalChequesToReturn = validateChequesToReturn(totalChequesToReturn);
        this.currencyDataList = List.copyOf(currencyData);
    }

    BunchChequeDepositData(int totalChequesToReturn, List<CurrencyData> currencyDataList) {
        this.totalChequesToReturn = totalChequesToReturn;
        this.currencyDataList = currencyDataList;
    }

    public BunchChequeDepositData(int totalChequesToReturn) {
        this(totalChequesToReturn, List.of());
    }


    public int getTotalChequesToReturn() {
        return totalChequesToReturn;
    }

    public List<CurrencyData> getDepositInfoList() {
        return currencyDataList;
    }

    @Override
    public String toNdcString() {
        if (currencyDataList.isEmpty()) {
            return Integers.toZeroPaddedString(totalChequesToReturn, 3);
        }
        final StringBuilder builder = new StringBuilder(3 + 100 * currencyDataList.size())
                .append(Integers.toZeroPaddedString(totalChequesToReturn, 3))
                .append(RESERVED_FIELD);

        final Iterator<CurrencyData> depositInfoIterator = currencyDataList.iterator();
        builder.append(depositInfoIterator.next().toNdcString());

        while (depositInfoIterator.hasNext()) {
            builder.append(NdcConstants.GROUP_SEPARATOR)
                    .append(depositInfoIterator.next().toNdcString());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BunchChequeDepositBuffer.class.getSimpleName() + ": {", "}")
                .add("totalChequesToReturn: " + totalChequesToReturn)
                .add("depositInfoList: " + currencyDataList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BunchChequeDepositData that = (BunchChequeDepositData) o;
        return totalChequesToReturn == that.totalChequesToReturn &&
                currencyDataList.equals(that.currencyDataList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalChequesToReturn, currencyDataList);
    }

    private int validateChequesToReturn(int value) {
        if (value < 0 || value > 999) {
            final String message = "'Total Cheques to Return' value should be within range 0-999 dec but was: " + value;
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
