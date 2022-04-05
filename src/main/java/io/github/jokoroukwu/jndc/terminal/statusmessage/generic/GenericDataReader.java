package io.github.jokoroukwu.jndc.terminal.statusmessage.generic;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.text.Strings;

public enum GenericDataReader implements ConfigurableNdcComponentReader<String> {
    INSTANCE;

    @Override
    public String readComponent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        if (deviceConfiguration.isMacEnabled()) {
            return readData(ndcCharBuffer.remaining() - 9, ndcCharBuffer);
        }
        return readData(ndcCharBuffer.remaining(), ndcCharBuffer);
    }

    private String readData(int dataLength, NdcCharBuffer ndcCharBuffer) {
        if (dataLength < 1) {
            //  field is empty
            return Strings.EMPTY_STRING;
        }
        return ndcCharBuffer.readCharSequence(dataLength);
    }
}
