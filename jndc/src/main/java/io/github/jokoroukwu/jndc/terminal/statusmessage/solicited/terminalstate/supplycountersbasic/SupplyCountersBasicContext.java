package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic.reserved.ReservedCounterFields;
import io.github.jokoroukwu.jndc.tsn.TransactionSerialNumberAcceptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SupplyCountersBasicContext implements TransactionSerialNumberAcceptor<SupplyCountersBasicContext> {
    private final SupplyCountersBasicBuilder countersBasicBuilder;
    private Map<Integer, String> reservedCounterFieldMap = Collections.emptyMap();

    public SupplyCountersBasicContext(SupplyCountersBasicBuilder countersBasicBuilder) {
        this.countersBasicBuilder = ObjectUtils.validateNotNull(countersBasicBuilder, "countersBasicBuilder");
    }

    public SupplyCountersBasicContext() {
        this(new SupplyCountersBasicBuilder());
    }

    public SupplyCountersBasicBuilder getCountersBasicBuilder() {
        return countersBasicBuilder;
    }

    public Map<Integer, String> getReservedCounterFieldMap() {
        return Collections.unmodifiableMap(reservedCounterFieldMap);
    }

    public void addReservedField(int fieldIndex, String fieldValue) {
        if (reservedCounterFieldMap == Collections.EMPTY_MAP) {
            reservedCounterFieldMap = new HashMap<>(12, 1);
        }
        reservedCounterFieldMap.put(fieldIndex, fieldValue);
    }

    public SupplyCountersBasic buildWithNoValidation() {
        final ReservedCounterFields reservedCounterFields = reservedCounterFieldMap.isEmpty()
                ? ReservedCounterFields.EMPTY
                : new ReservedCounterFields(reservedCounterFieldMap, null);

        return countersBasicBuilder
                .withReservedCounterFields(reservedCounterFields)
                .buildWithNoValidation();
    }

    @Override
    public SupplyCountersBasicContext withTransactionSerialNumber(int transactionSerialNumber) {
        countersBasicBuilder.withTransactionSerialNumber(transactionSerialNumber);
        return this;
    }

}
