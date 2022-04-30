package io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.diagnosticstatus;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceFaultFieldReader;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.DeviceStatusInformation;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.Optional;

public class DiagnosticStatusReader extends DeviceFaultFieldReader<Optional<DiagnosticStatus>> {
    @Override
    public Optional<DiagnosticStatus> readComponent(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        if (!ndcCharBuffer.hasRemaining() || hasFollowingMac(deviceConfiguration, ndcCharBuffer)) {
            return Optional.empty();
        }
        //  at least the field separator should be present
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(DeviceStatusInformation.COMMAND_NAME, "Diagnostic Status", errorMessage, ndcCharBuffer));
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Optional.empty();
        }
        return Optional.of(readDiagnosticStatus(ndcCharBuffer));
    }

    private DiagnosticStatus readDiagnosticStatus(NdcCharBuffer ndcCharBuffer) {
        final int mainErrorStatus = ndcCharBuffer.tryReadInt(2)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(DeviceStatusInformation.COMMAND_NAME, "Diagnostic Status: M‐Status",
                        errorMessage, ndcCharBuffer));

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
            throw NdcMessageParseException.withMessage(DeviceStatusInformation.COMMAND_NAME, "Diagnostic Status: Diagnostic Information",
                    "value length should be even but was " + value.length(), ndcCharBuffer);
        }
        if (!Strings.isHex(value)) {
            throw NdcMessageParseException.withMessage(DeviceStatusInformation.COMMAND_NAME, "Diagnostic Status: Diagnostic Information",
                    String.format("value should be hexadecimal but was '%s'", value), ndcCharBuffer);
        }
    }
}
