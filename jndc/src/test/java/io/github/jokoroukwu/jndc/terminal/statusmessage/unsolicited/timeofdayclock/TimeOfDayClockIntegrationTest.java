package io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.timeofdayclock;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.*;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.statusmessage.unsolicited.UnsolicitedStatusMessage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Set;

public class TimeOfDayClockIntegrationTest implements TerminalMessageListener, DeviceConfigurationSupplier<TerminalMessageMeta> {
    private final TerminalMessagePreProcessor messagePreProcessor = new TerminalMessagePreProcessor(this, this);
    private final String rawMessage = "12\u001C324123456\u001C\u001CA24";
    private UnsolicitedStatusMessage<TimeOfDayClockFailure> message;

    @Override
    public void onTimeOfDayClockFailureMessage(UnsolicitedStatusMessage<TimeOfDayClockFailure> message) {
        this.message = message;
    }

    @Override
    public DeviceConfiguration getConfiguration(TerminalMessageMeta meta) {
        return new DeviceConfigurationBase(true, Set.of(), ConfigurationOptions.EMPTY);
    }

    @Test
    public void should_read_whole_message_normally() {
        final NdcCharBuffer buffer = NdcCharBuffer.wrap(rawMessage);
        Assertions.assertThatCode(() -> messagePreProcessor.processMessage(buffer))
                .doesNotThrowAnyException();
    }

    @Test(dependsOnMethods = "should_read_whole_message_normally")
    public void encoded_message_should_be_equal_to_original() {
        Assertions.assertThat(message.toNdcString())
                .isEqualTo(rawMessage);
    }
}
