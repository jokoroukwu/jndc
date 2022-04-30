package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.trailingdata.TrailingDataChecker;
import org.testng.annotations.BeforeClass;

import static org.mockito.Mockito.mock;

public abstract class UnsolicitedMessageAppenderTest {
    protected DeviceConfiguration deviceConfigurationMock;
    protected UnsolicitedStatusMessageBuilder<UnsolicitedStatusInformation> messageBuilder;
    protected TrailingDataChecker trailingDataCheckerMock;

    @BeforeClass
    public void baseSetUp() {
        trailingDataCheckerMock = mock(TrailingDataChecker.class);
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        messageBuilder = new UnsolicitedStatusMessageBuilder<>()
                .withLuno(Luno.DEFAULT);
    }
}
