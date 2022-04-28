package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClockFailure.COMMAND_NAME;

public class TimeOfDayClockFailureAppender implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {
    private final TimeOfDayClockFailureMessageListener messageListener;

    public TimeOfDayClockFailureAppender(TimeOfDayClockFailureMessageListener messageListener) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final ClockDeviceStatus clockDeviceStatus = readDeviceStatus(ndcCharBuffer);
        final ErrorSeverity errorSeverity = readErrorSeverity(ndcCharBuffer);

        final UnsolicitedStatusMessageBuilder<? extends UnsolicitedStatusInformation> builder = stateObject
                .withStatusInformation(new TimeOfDayClockFailure(clockDeviceStatus, errorSeverity, null));

        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<TimeOfDayClockFailure> timeOfDayClockFailureMessage
                = (UnsolicitedStatusMessage<TimeOfDayClockFailure>) builder.build();

        messageListener.onTimeOfDayClockFailureMessage(timeOfDayClockFailureMessage);
    }

    private ClockDeviceStatus readDeviceStatus(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ClockDeviceStatus::forValue)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "'Device Status'", errorMessage, ndcCharBuffer));
    }

    private ErrorSeverity readErrorSeverity(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ErrorSeverity::forValue)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "'Error Severity'", errorMessage, ndcCharBuffer));
    }
}
