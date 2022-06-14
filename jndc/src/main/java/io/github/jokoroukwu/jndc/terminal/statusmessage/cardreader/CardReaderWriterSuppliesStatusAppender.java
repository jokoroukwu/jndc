package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class CardReaderWriterSuppliesStatusAppender extends DeviceFaultFieldAppender<CardReaderStatusInfoBuilder> {
    private final String commandName;

    public CardReaderWriterSuppliesStatusAppender(String commandName, ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    public CardReaderWriterSuppliesStatusAppender(String commandName) {
        this(commandName, null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CardReaderStatusInfoBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            //  at least the field separator should be present
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> NdcMessageParseException.onNoFieldSeparator(commandName, "Supplies Status", errorMessage, ndcCharBuffer));
            if (ndcCharBuffer.hasFieldDataRemaining()) {
                final SuppliesStatus suppliesStatus = readSuppliesStatus(ndcCharBuffer);
                stateObject.withSuppliesStatus(suppliesStatus);
            }
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private SuppliesStatus readSuppliesStatus(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(SuppliesStatus::forValue)
                .filter(this::isSuppliesStatusValid, value -> () -> String.format("should be '%s', '%s' or '%s' but was '%s'",
                        SuppliesStatus.NO_NEW_STATE, SuppliesStatus.GOOD_STATE, SuppliesStatus.OVERFILL, value))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(commandName, "Supplies Status", errorMessage, ndcCharBuffer));
    }

    private boolean isSuppliesStatusValid(SuppliesStatus suppliesStatus) {
        switch (suppliesStatus) {
            case NO_NEW_STATE:
            case GOOD_STATE:
            case OVERFILL: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
