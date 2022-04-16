package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldAppender;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class DiagnosticStatusAppender<T extends DiagnosticStatusAcceptor<?>> extends DeviceFaultFieldAppender<T> {
    private final String commandName;

    public DiagnosticStatusAppender(String commandName, ConfigurableNdcComponentAppender<T> nextAppender) {
        super(nextAppender);
        this.commandName = ObjectUtils.validateNotNull(commandName, "commandName");
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, T stateObject, DeviceConfiguration deviceConfiguration) {
        if (ndcCharBuffer.hasRemaining() && !hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            //  at least the field separator should be present
            ndcCharBuffer.trySkipFieldSeparator()
                    .ifPresent(errorMessage
                            -> NdcMessageParseException.onNoFieldSeparator(commandName, "Diagnostic Status", errorMessage, ndcCharBuffer));

            final DiagnosticStatus diagnosticStatus = readDiagnosticStatus(ndcCharBuffer);
            stateObject.withDiagnosticStatus(diagnosticStatus);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }


    private DiagnosticStatus readDiagnosticStatus(NdcCharBuffer ndcCharBuffer) {
        //  completion data may follow so should check
        //  if the preceding group separator is present
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return null;
        }
        final int mainErrorStatus = ndcCharBuffer.tryReadInt(2)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(commandName,
                        "Diagnostic Status: M‐Status", errorMessage, ndcCharBuffer));
        final StringBuilder builder = new StringBuilder();
        while (ndcCharBuffer.hasFieldDataRemaining()) {
            builder.append(ndcCharBuffer.readNextChar());
        }
        final String data = builder.toString();
        validateDiagnosticStatus(data, ndcCharBuffer);
        return new DiagnosticStatus(mainErrorStatus, data, null);
    }

    private void validateDiagnosticStatus(String value, NdcCharBuffer ndcCharBuffer) {
        if (!Integers.isEven(value.length())) {
            throw NdcMessageParseException.withMessage(commandName, "Diagnostic Status: Diagnostic Information",
                    "value length should be even but was " + value.length(), ndcCharBuffer);
        }
        if (!Strings.isHex(value)) {
            throw NdcMessageParseException.withMessage(commandName, "Diagnostic Status: Diagnostic Information",
                    String.format("value should be hexadecimal but was '%s'", value), ndcCharBuffer);
        }
    }
}
