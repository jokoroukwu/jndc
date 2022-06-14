package io.github.jokoroukwu.jndc.terminal.statusmessage.cardreader;

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

public class CardReaderWriterErrorSeverityAppender extends DeviceFaultFieldAppender<CardReaderStatusInfoBuilder> {
    public static final int MAX_FIELD_LENGTH = 2;
    private final String commandName;

    public CardReaderWriterErrorSeverityAppender(String commandName, ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    public CardReaderWriterErrorSeverityAppender(String commandName) {
        this(commandName, defaultAppender(commandName));
    }

    private static ConfigurableNdcComponentAppender<CardReaderStatusInfoBuilder> defaultAppender(String commandName) {
        final CardReaderWriterSuppliesStatusAppender suppliesStatusAppender
                = new CardReaderWriterSuppliesStatusAppender(commandName);
        final CompletionDataAppender<CardReaderStatusInfoBuilder> completionDataAppender
                = new CompletionDataAppender<>(commandName, suppliesStatusAppender);
        return new DiagnosticStatusAppender<>(commandName, completionDataAppender);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CardReaderStatusInfoBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            //  at least field separator should be present
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage -> NdcMessageParseException.onNoFieldSeparator(commandName, "Error Severity", errorMessage, ndcCharBuffer));

            final List<ErrorSeverity> errorSeverities = readErrorSeverities(ndcCharBuffer, deviceConfiguration);
            stateObject.withErrorSeverities(errorSeverities);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private List<ErrorSeverity> readErrorSeverities(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        if (!ndcCharBuffer.hasFieldDataRemaining() || hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            return List.of();
        }
        final ArrayList<ErrorSeverity> errorSeverities = new ArrayList<>(MAX_FIELD_LENGTH);
        int charsConsumed = 0;
        do {
            ndcCharBuffer.tryReadNextChar()
                    .flatMapToObject(ErrorSeverity::forValue)
                    .resolve(errorSeverities::add, errorMessage
                            -> NdcMessageParseException.onFieldParseError(commandName, "Error Severity", errorMessage, ndcCharBuffer));
            ++charsConsumed;
        } while (ndcCharBuffer.hasFieldDataRemaining() && charsConsumed < MAX_FIELD_LENGTH);

        errorSeverities.trimToSize();
        return Collections.unmodifiableList(errorSeverities);
    }

}
