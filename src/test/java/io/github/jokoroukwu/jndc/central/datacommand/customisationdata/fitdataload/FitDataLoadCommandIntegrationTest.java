package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.CentralMessageListener;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessagePreProcessor;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommand;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationBase;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

public class FitDataLoadCommandIntegrationTest implements CentralMessageListener, DeviceConfigurationSupplier<CentralMessageMeta> {
    private final String message = "30" +
            "\u001C000" +
            "\u001C000" +
            "\u001C15" +
            "\u001C516000255049244099099000000132000015000016000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private DataCommand<FitDataLoadCommand> fitDataLoadCommand;
    private CentralMessagePreProcessor messagePreProcessor;
    private DeviceConfiguration deviceConfiguration;
    private NdcCharBuffer messageBuffer;

    @BeforeMethod
    public void setUp() {
        deviceConfiguration = new DeviceConfigurationBase(false,
                Set.of(),
                ConfigurationOptions.EMPTY);
        messagePreProcessor = new CentralMessagePreProcessor(this, this);

        messageBuffer = NdcCharBuffer.wrap(message);
        messagePreProcessor.processMessage(messageBuffer);
    }


    @Override
    public void onFitDataLoadCommand(DataCommand<FitDataLoadCommand> message) {
        this.fitDataLoadCommand = message;
    }

    @Override
    public DeviceConfiguration getConfiguration(CentralMessageMeta meta) {
        return deviceConfiguration;
    }

    @Test
    public void should_parse_message() {
        Assertions.assertThat(messageBuffer.remaining())
                .as("zero characters should remain in message buffer")
                .isZero();
    }

    @Test
    public void encoded_message_should_be_equal_to_original() {
        final String actualEncoded = fitDataLoadCommand.toNdcString();
        Assertions.assertThat(actualEncoded)
                .isEqualTo(message);
    }
}
