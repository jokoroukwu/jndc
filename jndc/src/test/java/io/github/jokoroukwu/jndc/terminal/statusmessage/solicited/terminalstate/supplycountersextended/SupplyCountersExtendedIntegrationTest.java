package io.github.jokoroukwu.jndc.terminal.statusmessage.solicited.terminalstate.supplycountersextended;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.statusmessage.SolicitedStatusMessage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Set;

public class SupplyCountersExtendedIntegrationTest implements TerminalMessageListener {
    private SolicitedStatusMessage<SupplyCountersExtended> extendedSupplyCountersMessage;

    @DataProvider
    public static Object[][] configurationProvider() {
        final String messageWithNoMac = "22" +
                "\u001C031600160" +
                "\u001C\u001CF" +
                "\u001C7A08690000684" +
                "\u001DB00019" +
                "\u001DC0010000000000000000000000000002000000000000000000000000000300099000010000000000000000040009900001000000000000000" +
                "\u001DI0010000000000200000000003000990010005000990040009900100060009900500000000" +
                "\u001DK00000000000000000000";
        final String messageWithMacAndTimeVariant = "22" +
                "\u001C031600160" +
                "\u001C\u001CEA12CDFA" +
                "\u001CF" +
                "\u001C7A08690000684" +
                "\u001DB00019" +
                "\u001DC0010000000000000000000000000002000000000000000000000000000300099000010000000000000000040009900001000000000000000" +
                "\u001DI0010000000000200000000003000990010005000990040009900100060009900500000000" +
                "\u001DK00000000000000000000\u001CFA7ED3C3";

        return new Object[][]{
                {messageWithNoMac, new FakeConfigSupplier(new FakeDeviceConfig(false))},
                {messageWithMacAndTimeVariant, new FakeConfigSupplier(new FakeDeviceConfig(true))},
        };
    }

    @Override
    public void onSupplyCountersExtendedMessage(SolicitedStatusMessage<SupplyCountersExtended> message) {
        this.extendedSupplyCountersMessage = message;
    }

    @Test(dataProvider = "configurationProvider")
    public void should_parse_message(String messageString, DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        final TerminalMessagePreProcessor terminalMessageProcessor
                = new TerminalMessagePreProcessor(this, deviceConfigurationSupplier);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(messageString);
        Assertions.assertThatCode(() -> terminalMessageProcessor.processMessage(buffer))
                .doesNotThrowAnyException();
        Assertions.assertThat(buffer.remaining())
                .as("no characters should remain in the buffer")
                .isZero();
    }

    @Test(dataProvider = "configurationProvider")
    public void encoded_message_should_be_equal_to_original(String messageString,
                                                            DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        final TerminalMessagePreProcessor terminalMessageProcessor
                = new TerminalMessagePreProcessor(this, deviceConfigurationSupplier);

        final NdcCharBuffer buffer = NdcCharBuffer.wrap(messageString);
        Assertions.assertThatCode(() -> terminalMessageProcessor.processMessage(buffer))
                .doesNotThrowAnyException();

        Assertions.assertThat(extendedSupplyCountersMessage.toNdcString())
                .isEqualTo(messageString);
    }


    private static final class FakeConfigSupplier implements DeviceConfigurationSupplier<TerminalMessageMeta> {
        private final DeviceConfiguration deviceConfiguration;

        private FakeConfigSupplier(DeviceConfiguration deviceConfiguration) {
            this.deviceConfiguration = deviceConfiguration;
        }

        @Override
        public DeviceConfiguration getConfiguration(TerminalMessageMeta meta) {
            return deviceConfiguration;
        }
    }

    private static final class FakeDeviceConfig implements DeviceConfiguration {
        private final boolean isMacEnabled;

        private FakeDeviceConfig(boolean isMacEnabled) {
            this.isMacEnabled = isMacEnabled;
        }

        @Override
        public boolean isMacEnabled() {
            return isMacEnabled;
        }

        @Override
        public Set<Character> getTransactionRequestOptionalDataFieldsIds() {
            return Set.of();
        }

        @Override
        public ConfigurationOptions getConfigurationOptions() {
            return ConfigurationOptions.EMPTY;
        }
    }

}
