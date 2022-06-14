package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

public class TimeOfDayClockIntegrationTest implements TerminalMessageListener, DeviceConfigurationSupplier<TerminalMessageMeta> {
    private final TerminalMessagePreProcessor messagePreProcessor = new TerminalMessagePreProcessor(this, this);
    private final String rawMessage = "12\u001C324123456\u001C\u001CA2\u001C4";
    private UnsolicitedStatusMessage<TimeOfDayClock> message;
    private NdcCharBuffer buffer;

    @BeforeClass
    public void beforeClass() {
        buffer = NdcCharBuffer.wrap(rawMessage);
        messagePreProcessor.processMessage(buffer);
    }

    @Override
    public void onTimeOfDayClockStatusMessage(UnsolicitedStatusMessage<TimeOfDayClock> message) {
        this.message = message;
    }

    @Override
    public DeviceConfiguration getConfiguration(TerminalMessageMeta meta) {
        return new DeviceConfigurationBase(true, Set.of(), ConfigurationOptions.EMPTY);
    }

    @Test
    public void buffer_should_have_no_remaining_data() {
        Assertions.assertThat(buffer.remaining())
                .isZero();
    }

    @Test
    public void encoded_message_should_be_equal_to_original() {
        Assertions.assertThat(message.toNdcString())
                .isEqualTo(rawMessage);
    }
}
