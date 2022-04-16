package io.github.jokoroukwu.examples.central;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.central.CentralMessagePreProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ProcessCentralMessageExample {
    //  This is the main entrypoint for central originated messages.
    private static final CentralMessagePreProcessor CENTRAL_MESSAGE_PRE_PROCESSOR
            = new CentralMessagePreProcessor(new LoggingCentralMessageListener(), new FakeDeviceConfigurationSupplier());


    //  Parse the actual message and log it to console.
    public static void main(String[] args) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(getRawMessage());
        final NdcCharBuffer ndcCharBuffer = NdcCharBuffer.wrap(StandardCharsets.US_ASCII.decode(byteBuffer));
        CENTRAL_MESSAGE_PRE_PROCESSOR.processMessage(ndcCharBuffer);
    }

    //  pretend this are message bytes received from some ATM
    private static byte[] getRawMessage() {
        return "80\u001C000\u001C4\u001C77099F1A0206439F350114".getBytes(StandardCharsets.US_ASCII);
    }
}
