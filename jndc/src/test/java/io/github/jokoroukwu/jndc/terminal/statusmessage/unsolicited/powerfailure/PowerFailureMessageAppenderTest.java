package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.powerfailure;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedMessageAppenderTest;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class PowerFailureMessageAppenderTest extends UnsolicitedMessageAppenderTest {
    private final String configurationId = "0000";
    private PowerFailureMessageAppender appender;
    private PowerFailureMessageListener messageListenerMock;
    private NdcCharBuffer buffer;

    @BeforeClass
    public void beforeClass() {
        messageListenerMock = mock(PowerFailureMessageListener.class);
        appender = new PowerFailureMessageAppender(messageListenerMock, trailingDataCheckerMock);
    }

    @BeforeMethod
    public void setUp() {
        buffer = NdcCharBuffer.wrap(configurationId);
    }

    @Test
    public void should_append_expected_message() {
        when(trailingDataCheckerMock.getErrorMessageOnTrailingData(any()))
                .thenReturn(Optional.empty());
        appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock);

        final UnsolicitedStatusMessage<PowerFailure> expectedMessage = new UnsolicitedStatusMessage<>(Luno.DEFAULT,
                new PowerFailure(Integer.parseInt(configurationId)));

        verify(messageListenerMock, times(1))
                .onPowerFailureMessage(expectedMessage);

        verifyNoInteractions(deviceConfigurationMock);

        verify(trailingDataCheckerMock, times(1))
                .getErrorMessageOnTrailingData(buffer);
    }

    @Test
    public void should_throw_expected_exception_on_trailing_data_error() {
        final String trailingDataErrorMessage = BmpStringGenerator.ofCharacterRange('a', 'z').fixedLength(10);
        when(trailingDataCheckerMock.getErrorMessageOnTrailingData(any()))
                .thenReturn(Optional.of(trailingDataErrorMessage));

        Assertions.assertThatThrownBy(() -> appender.appendComponent(buffer, messageBuilder, deviceConfigurationMock))
                .isInstanceOf(NdcMessageParseException.class)
                .hasMessageContaining(trailingDataErrorMessage);
    }
}
