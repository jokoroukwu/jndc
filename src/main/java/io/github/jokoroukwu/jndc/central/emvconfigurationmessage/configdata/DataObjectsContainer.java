package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.tlv.CompositeTlv;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public abstract class DataObjectsContainer implements NdcComponent {

    protected <T extends CompositeTlv<String>> T validateDataObjects(T dataObjects, int... tags) {
        ObjectUtils.validateNotNull(dataObjects, "'BER‐TLV Data Objects'");
        for (int tag : tags) {
            if (!dataObjects.contains(tag)) {
                throw new IllegalArgumentException(String.format("'BER‐TLV Data Objects' must contain tag %X", tag));
            }
        }
        return dataObjects;
    }
}
