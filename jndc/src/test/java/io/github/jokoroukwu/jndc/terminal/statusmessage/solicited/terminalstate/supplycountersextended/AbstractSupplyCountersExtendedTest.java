package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public abstract class AbstractSupplyCountersExtendedTest {
    protected ConfigurableNdcComponentAppender<SupplyCountersExtendedBuilder> nextAppenderMock;
    protected DeviceConfiguration deviceConfigurationMock;
    protected SupplyCountersExtendedBuilder builder;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    protected void baseSetUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        builder = new SupplyCountersExtendedBuilder();
    }
}
