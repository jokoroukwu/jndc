package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.text.Strings;

public enum GenericAdditionalDataAppender implements ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> {
    INSTANCE;

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, GenericDeviceFaultBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final String data = readData(ndcCharBuffer);

        stateObject.withAdditionalData(data);
    }

    private String readData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasRemaining());

        return builder.toString();
    }
}
