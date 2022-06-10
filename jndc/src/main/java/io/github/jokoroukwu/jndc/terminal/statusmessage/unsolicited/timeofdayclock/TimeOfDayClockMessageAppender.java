package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusInformation;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.trailingdata.TrailingDataChecker;
import io.github.jokoroukwu.jndc.trailingdata.TrailingDataCheckerBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.*;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock.TimeOfDayClock.COMMAND_NAME;

public class TimeOfDayClockMessageAppender implements ConfigurableNdcComponentAppender<UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation>> {
    private final TimeOfDayClockFailureMessageListener messageListener;
    private final TrailingDataChecker trailingDataChecker;

    public TimeOfDayClockMessageAppender(TimeOfDayClockFailureMessageListener messageListener, TrailingDataChecker trailingDataChecker) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.trailingDataChecker = ObjectUtils.validateNotNull(trailingDataChecker, "trailingDataChecker");
    }

    public TimeOfDayClockMessageAppender(TimeOfDayClockFailureMessageListener messageListener) {
        this(messageListener, TrailingDataCheckerBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer,
                                UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> stateObject,
                                DeviceConfiguration deviceConfiguration) {

        final ClockDeviceStatus clockDeviceStatus = readDeviceStatus(ndcCharBuffer);
        final ErrorSeverity errorSeverity = readErrorSeverity(ndcCharBuffer);

        trailingDataChecker.getErrorMessageOnTrailingData(ndcCharBuffer)
                .ifPresent(errorMessage -> onMessageParseError(COMMAND_NAME, errorMessage, ndcCharBuffer));

        final UnsolicitedStatusMessageBuilder<? extends UnsolicitedStatusInformation> builder = stateObject
                .withStatusInformation(new TimeOfDayClock(clockDeviceStatus, errorSeverity, null));

        @SuppressWarnings("unchecked") final UnsolicitedStatusMessage<TimeOfDayClock> timeOfDayClockFailureMessage
                = (UnsolicitedStatusMessage<TimeOfDayClock>) builder.build();

        messageListener.onTimeOfDayClockStatusMessage(timeOfDayClockFailureMessage);
    }

    private ClockDeviceStatus readDeviceStatus(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ClockDeviceStatus::forValue)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "'Device Status'", errorMessage, ndcCharBuffer));
    }

    private ErrorSeverity readErrorSeverity(NdcCharBuffer ndcCharBuffer) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage -> onNoFieldSeparator(COMMAND_NAME, "'Error Severity'", errorMessage, ndcCharBuffer));
        return ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(ErrorSeverity::forValue)
                .getOrThrow(errorMessage -> withMessage(COMMAND_NAME, "'Error Severity'", errorMessage, ndcCharBuffer));
    }
}
