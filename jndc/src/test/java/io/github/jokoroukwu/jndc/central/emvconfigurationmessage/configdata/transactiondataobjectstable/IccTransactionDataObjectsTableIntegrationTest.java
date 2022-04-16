package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

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

public class IccTransactionDataObjectsTableIntegrationTest implements CentralMessageListener {
    private final String originalMessage = "80\u001C000\u001C2\u001C090177079C01019F53015A0277079C01019F53015A0377079C01" +
            "009F5301550477079C01309F53015A0577079C01009F53015A0677079C01019F5301440777079C01289F53015A0877079C01709F53015A0977079C01729F53015A";
    private final CentralMessagePreProcessor centralMessageProcessor = new CentralMessagePreProcessor(new AppenderFactoryBase<>(
            Map.of(CentralMessageClass.EMV_CONFIGURATION, new EmvConfigurationMessageAppender(this))
    ));
    private EmvConfigurationMessage<?> receivedMessage;

    @Override
    public void onTransactionDataObjectsTableMessage(EmvConfigurationMessage<IccTransactionDataObjectsTable> message) {
        receivedMessage = message;
    }

    @Test
    public void encoded_message_should_be_equal_to_original() {
        centralMessageProcessor.processMessage(NdcCharBuffer.wrap(originalMessage));

        Assertions.assertThat(receivedMessage.toNdcString())
                .isEqualTo(originalMessage);
    }
}
