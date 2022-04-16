package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersbasic;

import io.github.jokoroukwu.jndc.field.fieldindicator.FieldPresenceIndicator;
import io.github.jokoroukwu.jndc.field.fieldmeta.FieldMetaSkipStrategy;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public abstract class SupplyCountersBasicAppenderTest {
    protected ConfigurableNdcComponentAppender<SupplyCountersBasicContext> nextAppenderMock;
    protected FieldMetaSkipStrategy fieldMetaSkipStrategyMock;
    protected FieldPresenceIndicator fieldPresenceIndicatorMock;
    protected SupplyCountersBasicContext context;
    protected DeviceConfiguration deviceConfigurationMock;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void baseSetUp() {
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        fieldMetaSkipStrategyMock = mock(FieldMetaSkipStrategy.class);
        fieldPresenceIndicatorMock = mock(FieldPresenceIndicator.class);
        context = new SupplyCountersBasicContext();
    }
}
