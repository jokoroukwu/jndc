package io.github.jokoroukwu.examples.terminal;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.TerminalMessagePreProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ProcessTerminalMessageExample {
    //  This is the main entrypoint for terminal originated messages.
    private static final TerminalMessagePreProcessor TERMINAL_MESSAGE_PRE_PROCESSOR
            = new TerminalMessagePreProcessor(new LoggingTerminalMessageListener(), new FakeDeviceConfigurationSupplier());

    //  Parse the actual message and log it to console.
    public static void main(String[] args) {
        final ByteBuffer messageBytes = ByteBuffer.wrap(getRawMessage());
        final NdcCharBuffer ndcCharBuffer = NdcCharBuffer.wrap(StandardCharsets.US_ASCII.decode(messageBytes));
        TERMINAL_MESSAGE_PRE_PROCESSOR.processMessage(ndcCharBuffer);
    }

    //  pretend this are message bytes received from some ATM.
    private static byte[] getRawMessage() {
        final String message = "11" +
                "\u001C444555444" +
                "\u001C\u001C0000014B" +
                "\u001C15" +
                "\u001C;9999999900000073=25081010445000000000?" +
                "\u001C\u001CI   A DC" +
                "\u001C000000000000" +
                "\u001C<0494<076329<849" +
                "\u001C\u001C\u001C1%B9999999900000073^1/SVCRD 0099 00007" +
                "\u001C20209100000000000000000000000000000000000000000000020000000000000000000000" +
                "\u001C3012080823763" +
                "\u001CB3B9AA54";
        return message.getBytes(StandardCharsets.US_ASCII);
    }
}
