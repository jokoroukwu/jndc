package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.statusmessage.devicefault.ErrorSeverity;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedMessageAppenderTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessageBuilder;
import io.github.jokoroukwu.jndc.util.NdcConstants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class TimeOfDayClockAppenderTest extends UnsolicitedMessageAppenderTest {
    private TimeOfDayClockMessageAppender appender;
    private TimeOfDayClockFailureMessageListener messageListenerMock;
    private NdcCharBuffer buffer;

    @BeforeClass
    public void setUp() {
        messageListenerMock = mock(TimeOfDayClockFailureMessageListener.class);
        appender = new TimeOfDayClockMessageAppender(messageListenerMock, trailingDataCheckerMock);
        when(trailingDataCheckerMock.getErrorMessageOnTrailingData(any()))
                .thenReturn(Optional.empty());
    }

    @BeforeMethod
    public void beforeMethod() {
        buffer = NdcCharBuffer.wrap(ClockStatus.RESET.toNdcString() + NdcConstants.FIELD_SEPARATOR + ErrorSeverity.FATAL.toNdcString());
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);
    }

    @Test
    public void should_call_message_listener_with_expected_arg() {
        final UnsolicitedStatusMessage<TimeOfDayClock> expectedMessage = new UnsolicitedStatusMessageBuilder<TimeOfDayClock>()
                .withLuno(Luno.DEFAULT)
                .withStatusInformation(new TimeOfDayClock(ClockStatus.RESET, ErrorSeverity.FATAL))
                .build();

        verify(messageListenerMock, times(1))
                .onTimeOfDayClockStatusMessage(expectedMessage);
    }

    @Test
    public void should_not_interact_with_device_configuration() {
        verifyNoInteractions(deviceConfigurationMock);
    }

    @Test
    public void should_call_trailing_data_checker() {
        verify(trailingDataCheckerMock, times(1))
                .getErrorMessageOnTrailingData(buffer);
    }
}
