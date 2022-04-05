package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public abstract class TransactionReplyAppenderTest {
    protected TransactionReplyCommandBuilder builder;
    protected ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppenderMock;
    protected DeviceConfiguration deviceConfigurationMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void baseSetUp() {
        builder = new TransactionReplyCommandBuilder();
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        deviceConfigurationMock = mock(DeviceConfiguration.class);
    }
}
