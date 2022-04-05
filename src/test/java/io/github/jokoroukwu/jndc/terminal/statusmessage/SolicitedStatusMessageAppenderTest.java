package io.github.jokoroukwu.jndc.terminal.statusmessage;

import io.github.jokoroukwu.jndc.ConfigurableNdcComponentAppenderFactory;
import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMetaBase;
import io.github.jokoroukwu.jndc.util.text.stringgenerator.BmpStringGenerator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor.DEVICE_FAULT;
import static io.github.jokoroukwu.jndc.terminal.statusmessage.StatusDescriptor.READY;
import static io.github.jokoroukwu.jndc.util.NdcConstants.FIELD_SEPARATOR;
import static io.github.jokoroukwu.jndc.util.NdcConstants.FIELD_SEPARATOR_STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SolicitedStatusMessageAppenderTest {
    private DeviceConfiguration deviceConfigurationMock;
    private DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplierMock;
    private ConfigurableNdcComponentAppender<SolicitedStatusMessageBuilder<SolicitedStatusInformation>> nextAppenderMock;
    private ConfigurableNdcComponentAppenderFactory<StatusDescriptor, SolicitedStatusMessageBuilder<SolicitedStatusInformation>> appenderFactoryMock;
    private SolicitedStatusMessageAppender appender;

    @BeforeMethod
    @SuppressWarnings("unchecked")
    public void setUp() {
        deviceConfigurationMock = mock(DeviceConfiguration.class);
        deviceConfigurationSupplierMock = mock(DeviceConfigurationSupplier.class);
        nextAppenderMock = mock(ConfigurableNdcComponentAppender.class);
        appenderFactoryMock = mock(ConfigurableNdcComponentAppenderFactory.class);
        when(appenderFactoryMock.getAppender(any()))
                .thenReturn(Optional.of(nextAppenderMock));
        when(deviceConfigurationSupplierMock.getConfiguration(any()))
                .thenReturn(deviceConfigurationMock);
        appender = new SolicitedStatusMessageAppender(appenderFactoryMock, deviceConfigurationSupplierMock);
    }

    @DataProvider
    public Object[][] validDataProvider() {
        final String timeVariantNumber = BmpStringGenerator.HEX.fixedLength(8);
        final long timeVariantNumberLong = Long.parseLong(timeVariantNumber, 16);
        final String withTimeVariantAndWithMac = FIELD_SEPARATOR
                + Luno.DEFAULT.toNdcString()
                + FIELD_SEPARATOR_STRING.repeat(2)
                + timeVariantNumber
                + FIELD_SEPARATOR
                + DEVICE_FAULT.toNdcString();

        final String withNoTimeVariant = FIELD_SEPARATOR
                + Luno.DEFAULT.toNdcString()
                + FIELD_SEPARATOR_STRING.repeat(2)
                + DEVICE_FAULT.toNdcString();

        final String withNoTimeVariantAndWithMac = FIELD_SEPARATOR
                + Luno.DEFAULT.toNdcString()
                + FIELD_SEPARATOR_STRING.repeat(3)
                + READY.toNdcString();

        return new Object[][]{
                {"with Time Variant and with MAC", withTimeVariantAndWithMac, Luno.DEFAULT, timeVariantNumberLong, DEVICE_FAULT, true},
                {"with no Time Variant and no MAC", withNoTimeVariant, Luno.DEFAULT, -1L, DEVICE_FAULT, false},
                {"with no Time Variant and with MAC", withNoTimeVariantAndWithMac, Luno.DEFAULT, -1L, READY, true},

        };
    }

    @Test(dataProvider = "validDataProvider")
    public void should_append_expected_value(String unusedDescription,
                                             String validData,
                                             Luno luno,
                                             long timeVariantNumber,
                                             StatusDescriptor statusDescriptor,
                                             boolean isMacEnabled) {
        when(deviceConfigurationMock.isMacEnabled())
                .thenReturn(isMacEnabled);

        final TerminalMessageMeta messageMeta = new TerminalMessageMetaBase(TerminalMessageClass.SOLICITED,
                TerminalMessageSubClass.STATUS_MESSAGE);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(validData);
        appender.appendComponent(buffer, messageMeta);

        final SolicitedStatusMessageBuilder<SolicitedStatusInformation> expectedStateObject = new SolicitedStatusMessageBuilder<>()
                .withLuno(luno)
                .withTimeVariantNumber(timeVariantNumber)
                .withStatusDescriptor(statusDescriptor);

        verify(deviceConfigurationSupplierMock, times(1))
                .getConfiguration(messageMeta);
        verify(deviceConfigurationMock, times(1))
                .isMacEnabled();
        verify(appenderFactoryMock, times(1))
                .getAppender(statusDescriptor);
        verify(nextAppenderMock, times(1))
                .appendComponent(buffer, expectedStateObject, deviceConfigurationMock);
    }
}
