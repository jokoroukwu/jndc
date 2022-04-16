package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

public interface CassetteFactory {

    Cassette getCassette(int cassetteType, int numberOfNotes, String additionalData);
}
