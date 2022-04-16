package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.NdcComponentReader;

import java.util.function.Function;

public enum CassetteReaderFactory implements Function<DataId, NdcComponentReader<? extends Cassette>> {
    INSTANCE;
    public static final CassetteReader CIM_CASSETTE_READER = new CassetteReader(0, 255,
            (type, notes, data) -> new CimRecycleCassette(type, notes, data, null));
    public static final CassetteReader CDM_CASSETTE_READER = new CassetteReader(1, 7,
            (type, notes, data) -> new CdmRecycleCassette(type, notes, data, null));

    @Override
    public NdcComponentReader<? extends Cassette> apply(DataId dataId) {
        switch (dataId) {
            case DEPOSIT_DATA:
                return CDM_CASSETTE_READER;
            case DISPENSE_DATA:
                return CIM_CASSETTE_READER;
            default: {
                throw new IllegalArgumentException("Invalid Data ID: " + dataId);
            }
        }
    }
}
