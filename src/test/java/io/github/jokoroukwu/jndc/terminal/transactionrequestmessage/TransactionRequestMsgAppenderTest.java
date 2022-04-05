package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public abstract class TransactionRequestMsgAppenderTest {
    protected ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppenderMock;
    protected DeviceConfiguration deviceConfigurationMock;
    protected TransactionRequestMessageBuilder messageBuilder;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void baseSetUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        messageBuilder = new TransactionRequestMessageBuilder();
    }
}
