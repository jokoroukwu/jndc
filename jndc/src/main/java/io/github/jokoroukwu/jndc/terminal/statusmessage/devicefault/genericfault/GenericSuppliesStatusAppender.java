package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.SuppliesStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericSuppliesStatusAppender extends DeviceFaultFieldAppender<GenericDeviceFaultBuilder> {

    public GenericSuppliesStatusAppender(ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> nextAppender) {
        super(nextAppender);
    }

    public GenericSuppliesStatusAppender() {
        super(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, GenericDeviceFaultBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            //  at least the field separator should be present
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(DeviceStatusInformation.COMMAND_NAME, "Supplies Status",
                            errorMessage, ndcCharBuffer));
            final List<SuppliesStatus> suppliesStatuses = readSuppliesStatuses(ndcCharBuffer);
            stateObject.withSuppliesStatuses(suppliesStatuses);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private List<SuppliesStatus> readSuppliesStatuses(NdcCharBuffer ndcCharBuffer) {
        //  the field may be empty
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return List.of();
        }
        final ArrayList<SuppliesStatus> suppliesStatuses = new ArrayList<>();
        do {
            ndcCharBuffer.tryReadNextChar()
                    .flatMapToObject(SuppliesStatus::forValue)
                    .resolve(suppliesStatuses::add, errorMessage
                            -> NdcMessageParseException.onFieldParseError(DeviceStatusInformation.COMMAND_NAME, "Supplies Status", errorMessage, ndcCharBuffer));
        } while (ndcCharBuffer.hasFieldDataRemaining());

        suppliesStatuses.trimToSize();
        return Collections.unmodifiableList(suppliesStatuses);
    }
}
