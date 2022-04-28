package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedMessageAppenderTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class TimeOfDayClockAppenderTest extends UnsolicitedMessageAppenderTest {
    private TimeOfDayClockFailureAppender appender;
    private TimeOfDayClockFailureMessageListener messageListenerMock;

    @BeforeClass
    public void setUp() {
        messageListenerMock = mock(TimeOfDayClockFailureMessageListener.class);
        appender = new TimeOfDayClockFailureAppender(messageListenerMock);
    }

    @Test
    public void should_call_message_listener_with_expected_arg() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(ClockDeviceStatus.RESET.toNdcString() + ErrorSeverity.FATAL.toNdcString());
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        final UnsolicitedStatusMessage<TimeOfDayClockFailure> expectedMessage = new UnsolicitedStatusMessageBuilder<TimeOfDayClockFailure>()
                .withLuno(Luno.DEFAULT)
                .withStatusInformation(new TimeOfDayClockFailure(ClockDeviceStatus.RESET, ErrorSeverity.FATAL))
                .build();

        verify(messageListenerMock, times(1))
                .onTimeOfDayClockFailureMessage(expectedMessage);
        verifyNoInteractions(deviceConfigurationMock);
    }
}
