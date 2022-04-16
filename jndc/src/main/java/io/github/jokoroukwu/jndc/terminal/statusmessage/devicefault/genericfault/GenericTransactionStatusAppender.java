package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class GenericTransactionStatusAppender extends ChainedConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> {
    private final String commandName;

    public GenericTransactionStatusAppender(String commandName, ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    public GenericTransactionStatusAppender(String commandName) {
        this(commandName, new GenericErrorSeveritiesAppender(commandName));
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, GenericDeviceFaultBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        final String data = readTransactionStatus(ndcCharBuffer);
        stateObject.withTransactionStatus(data);

        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private String readTransactionStatus(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());

        return builder.toString();
    }
}
