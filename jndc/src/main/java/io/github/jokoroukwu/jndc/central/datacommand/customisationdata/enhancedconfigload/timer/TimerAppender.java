package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.timer;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommand;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.enhancedconfigload.EnhancedConfigParamsLoadCommandBuilder;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Optional;

public class TimerAppender implements NdcComponentAppender<EnhancedConfigParamsLoadCommandBuilder> {
    public static final String TIMER_NUMBER_FIELD = "Timer Number";
    public static final String TIMER_DURATION_FIELD = "Timer Duration";

    public static final TimerAppender INSTANCE = new TimerAppender();

    private TimerAppender() {
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, EnhancedConfigParamsLoadCommandBuilder stateObject) {
        ObjectUtils.validateNotNull(ndcCharBuffer, "ndcCharBuffer");
        ObjectUtils.validateNotNull(stateObject, "commandBuilder");

        final int timerNumber = ndcCharBuffer.tryReadInt(2)
                .ifPresent(number -> validateTimerNumber(number)
                        .ifPresent(message -> NdcMessageParseException.onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, TIMER_NUMBER_FIELD,
                                message, ndcCharBuffer)))
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, TIMER_NUMBER_FIELD,
                        errorMessage, ndcCharBuffer))
                .get();

        final int timerDuration = ndcCharBuffer.tryReadInt(3)
                .ifPresent(duration -> validateDuration(duration)
                        .ifPresent(message -> NdcMessageParseException.onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, TIMER_DURATION_FIELD,
                                message, ndcCharBuffer)))
                .ifEmpty(message -> NdcMessageParseException.onFieldParseError(EnhancedConfigParamsLoadCommand.COMMAND_NAME, TIMER_DURATION_FIELD,
                        message, ndcCharBuffer))
                .get();

        stateObject.addTimer(new TimerBase(timerNumber, timerDuration, null));
    }

    public Optional<String> validateTimerNumber(Integer timerNumber) {
        if (timerNumber < 0 || timerNumber > 99) {
            return Optional.of(String.format("value '%d' is not within valid range of 'Timer Number' (0-99)", timerNumber));
        }
        if (isReservedTimerNumber(timerNumber)) {
            return Optional.of(String.format("value '%d' is a reserved 'Timer Number'", timerNumber));
        }
        return Optional.empty();
    }

    private boolean isReservedTimerNumber(int timerNumber) {
        return (timerNumber == 62 || timerNumber == 70 || timerNumber == 93) ||
                (timerNumber > 10 && timerNumber < 60) ||
                (timerNumber > 63 && timerNumber < 68) ||
                (timerNumber > 72 && timerNumber < 77) ||
                (timerNumber > 78 && timerNumber < 87) ||
                (timerNumber > 87 && timerNumber < 92) ||
                timerNumber > 96;
    }


    public Optional<String> validateDuration(Integer timerDuration) {
        if (timerDuration < TimerBase.MIN_DURATION || timerDuration > TimerBase.MAX_DURATION) {
            final String errorMessage = String.format("value %d is not within valid 'Timer Duration' range (%d-%d)",
                    timerDuration, TimerBase.MIN_DURATION, TimerBase.MAX_DURATION);
            return Optional.of(errorMessage);
        }
        return Optional.empty();
    }

}
