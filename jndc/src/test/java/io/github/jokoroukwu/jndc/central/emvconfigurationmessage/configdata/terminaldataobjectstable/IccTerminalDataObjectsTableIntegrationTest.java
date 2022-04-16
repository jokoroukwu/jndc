package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

import io.github.jokoroukwu.jndc.AppenderFactoryBase;
import io.github.jokoroukwu.jndc.CentralMessageListener;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.CentralMessagePreProcessor;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessageAppender;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

public class IccTerminalDataObjectsTableIntegrationTest implements CentralMessageListener {
    private final String originalMessage = "80\u001C000\u001C4\u001C77099F1A0206439F350114";
    private final CentralMessagePreProcessor centralMessageProcessor = new CentralMessagePreProcessor(
            new AppenderFactoryBase<>(Map.of(CentralMessageClass.EMV_CONFIGURATION,
                    new EmvConfigurationMessageAppender(this)))
    );
    private EmvConfigurationMessage<IccTerminalDataObjectsTable> receivedMessage;

    @Override
    public void onTerminalDataObjectsTableMessage(EmvConfigurationMessage<IccTerminalDataObjectsTable> message) {
        receivedMessage = message;
    }

    @Test
    public void encoded_message_should_be_equal_to_original() {
        centralMessageProcessor.processMessage(NdcCharBuffer.wrap(originalMessage));

        Assertions.assertThat(receivedMessage.toNdcString())
                .isEqualTo(originalMessage);
    }
}
