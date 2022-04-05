package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

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

public class LanguageSupportMessageIntegrationTest implements CentralMessageListener {
    private final String originalMessage = "80\u001C000\u001C3\u001C02ru000001012@A@en000001012@A@";

    private final CentralMessagePreProcessor centralMessageProcessor = new CentralMessagePreProcessor(
            new AppenderFactoryBase<>(Map.of(CentralMessageClass.EMV_CONFIGURATION,
                    new EmvConfigurationMessageAppender(this))));
    private EmvConfigurationMessage<IccLanguageSupportTable> receivedMessage;

    @Override
    public void onLanguageSupportDataObjectsTableMessage(EmvConfigurationMessage<IccLanguageSupportTable> message) {
        receivedMessage = message;
    }

    @Test
    public void encoded_message_should_be_equal_to_original() {
        centralMessageProcessor.processMessage(NdcCharBuffer.wrap(originalMessage));
        Assertions.assertThat(receivedMessage.toNdcString())
                .isEqualTo(originalMessage);
    }
}
