package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.util.Integers;

public final class CimRecycleCassette extends Cassette {

    public CimRecycleCassette(int cassetteType, int numberOfNotes, String additionalData) {
        super(cassetteType, numberOfNotes, additionalData);
    }

    public CimRecycleCassette(int cassetteType, int numberOfNotes, String additionalData, Void unused) {
        super(cassetteType, numberOfNotes, additionalData, unused);
    }


    public CimRecycleCassette(int cassetteType, int numberOfNotes) {
        super(cassetteType, numberOfNotes);
    }

    @Override
    protected int validateCassetteType(int cassetteType) {
        return Integers.validateRange(cassetteType, 0, 255, "'CIM Recycle NDC Cassette Type'");
    }
}
