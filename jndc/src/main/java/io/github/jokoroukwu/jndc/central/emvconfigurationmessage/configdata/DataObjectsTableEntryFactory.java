package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;

public interface DataObjectsTableEntryFactory<V extends NdcComponent> {

    V getEntry(int entryType, ResponseFormat2 dataObjects);
}
