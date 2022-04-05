package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.collection.LimitedSizeList;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class TransactionData<E extends Cassette> extends LimitedSizeList<E> implements NdcComponent {
    public static final TransactionData<Cassette> EMPTY = new TransactionData<>(null, Collections.emptyList());
    public static final int MAX_SIZE = 99;
    private final DataId dataId;

    TransactionData(DataId dataId, List<E> listDelegate) {
        super(MAX_SIZE, listDelegate);
        this.dataId = dataId;
    }

    private TransactionData(DataId dataId, int initCapacity) {
        super(MAX_SIZE, initCapacity);
        this.dataId = dataId;
    }

    public static <E extends Cassette> TransactionData<E> unmodifiable(TransactionData<E> transactionData) {
        return new TransactionData<>(transactionData.dataId, List.copyOf(transactionData));
    }

    public static TransactionData<CdmRecycleCassette> depositData(int capacity) {
        return new TransactionData<>(DataId.DEPOSIT_DATA, capacity);
    }

    public static TransactionData<CdmRecycleCassette> depositData() {
        return new TransactionData<>(DataId.DEPOSIT_DATA, 10);
    }

    public static TransactionData<CdmRecycleCassette> depositData(CdmRecycleCassette... cassettes) {
        final TransactionData<CdmRecycleCassette> transactionData = new TransactionData<>(DataId.DEPOSIT_DATA, cassettes.length);
        for (CdmRecycleCassette cassette : cassettes) {
            transactionData.add(cassette);
        }
        return transactionData;
    }

    public static TransactionData<CimRecycleCassette> dispenseData(int capacity) {
        return new TransactionData<>(DataId.DISPENSE_DATA, capacity);
    }

    public static TransactionData<CimRecycleCassette> dispenseData() {
        return new TransactionData<>(DataId.DISPENSE_DATA, 10);
    }

    public static TransactionData<CimRecycleCassette> dispenseData(CimRecycleCassette... cassettes) {
        final TransactionData<CimRecycleCassette> transactionData = new TransactionData<>(DataId.DISPENSE_DATA, cassettes.length);
        for (CimRecycleCassette cassette : cassettes) {
            transactionData.add(cassette);
        }
        return transactionData;
    }

    public Optional<DataId> getDataId() {
        return Optional.ofNullable(dataId);
    }

    @Override
    protected void performElementChecks(Cassette element) {
        //  no additional checks
    }

    @Override
    public String toNdcString() {
        if (isEmpty()) {
            return Strings.EMPTY_STRING;
        }
        return new NdcStringBuilder(48)
                .appendComponent(dataId)
                .appendZeroPadded(size(), 2)
                .appendComponents(this, NdcConstants.GROUP_SEPARATOR_STRING)
                .toString();
    }
}
