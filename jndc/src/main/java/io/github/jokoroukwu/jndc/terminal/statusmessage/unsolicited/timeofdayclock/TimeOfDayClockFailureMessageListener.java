package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;

public interface TimeOfDayClockFailureMessageListener {

    void onTimeOfDayClockFailureMessage(UnsolicitedStatusMessage<TimeOfDayClockFailure> timeOfDayClockFailureMessage);
}
