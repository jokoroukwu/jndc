package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.genericfault;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.completiondata.CompletionDataAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldAppender;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus.DiagnosticStatusAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericErrorSeveritiesAppender extends DeviceFaultFieldAppender<GenericDeviceFaultBuilder> {
    private final String commandName;

    public GenericErrorSeveritiesAppender(String commandName, ConfigurableNdcComponentAppender<GenericDeviceFaultBuilder> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    public GenericErrorSeveritiesAppender(String commandName) {
        this(commandName, defaultNextAppender(commandName));
    }

    private static DeviceFaultFieldAppender<GenericDeviceFaultBuilder> defaultNextAppender(String commandName) {
        final GenericSuppliesStatusAppender suppliesStatusAppender = new GenericSuppliesStatusAppender();
        final CompletionDataAppender<GenericDeviceFaultBuilder> completionDataAppender
                = new CompletionDataAppender<>(commandName, suppliesStatusAppender);
        return new DiagnosticStatusAppender<>(commandName, completionDataAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, GenericDeviceFaultBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            //  at least field separator should be present
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> NdcMessageParseException.onNoFieldSeparator(commandName, "Error Severity", errorMessage, ndcCharBuffer));

            final List<ErrorSeverity> errorSeverities = readErrorSeverities(ndcCharBuffer, deviceConfiguration);
            stateObject.withErrorSeverities(errorSeverities);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private List<ErrorSeverity> readErrorSeverities(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        //  the field may be empty
        if (!ndcCharBuffer.hasFieldDataRemaining() || hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            return List.of();
        }
        final ArrayList<ErrorSeverity> errorSeverities = new ArrayList<>();
        do {
            ndcCharBuffer.tryReadNextChar()
                    .flatMapToObject(ErrorSeverity::forValue)
                    .resolve(errorSeverities::add, errorMessage
                            -> NdcMessageParseException.onFieldParseError(commandName, "Error Severity", errorMessage, ndcCharBuffer));
        } while (ndcCharBuffer.hasFieldDataRemaining());

        errorSeverities.trimToSize();
        return Collections.unmodifiableList(errorSeverities);
    }
}
