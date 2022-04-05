package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public class DataObjectsTableEntryReader<V extends NdcComponent> implements NdcComponentReader<DescriptiveOptional<V>> {
    private final NdcComponentReader<DescriptiveOptional<ResponseFormat2>> dataObjectsReader;
    private final DataObjectsTableEntryFactory<V> entryFactory;

    public DataObjectsTableEntryReader(NdcComponentReader<DescriptiveOptional<ResponseFormat2>> dataObjectsReader,
                                       DataObjectsTableEntryFactory<V> entryFactory) {
        this.dataObjectsReader = ObjectUtils.validateNotNull(dataObjectsReader, "dataObjectsReader cannot be null");
        this.entryFactory = ObjectUtils.validateNotNull(entryFactory, "entryFactory cannot be null");
    }

    @Override
    public DescriptiveOptional<V> readComponent(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadHexInt(2)
                .filter(this::isEntryTypeValid, type -> () -> type + " is not within valid 'Entry Type' range (1-255)")
                .flatMapToObject(type -> dataObjectsReader.readComponent(ndcCharBuffer)
                        .map(dataObjects -> entryFactory.getEntry(type, dataObjects)));
    }

    public boolean isEntryTypeValid(int type) {
        return type >= 1;
    }
}
