package io.github.jokoroukwu.jndc.terminal.statusmessage.readyb.transactiondata;

import io.github.jokoroukwu.jndc.util.Integers;

public final class CdmRecycleCassette extends Cassette {
    public CdmRecycleCassette(int cassetteType, int numberOfNotes, String additionalData) {
        super(cassetteType, numberOfNotes, additionalData);
    }

    CdmRecycleCassette(int cassetteType, int numberOfNotes, String additionalData, Void unused) {
        super(cassetteType, numberOfNotes, additionalData, unused);
    }

    public CdmRecycleCassette(int cassetteType, int numberOfNotes) {
        super(cassetteType, numberOfNotes);
    }

    @Override
    protected int validateCassetteType(int cassetteType) {
        return Integers.validateRange(cassetteType, 1, 7, "'CDM Recycle NDC Cassette Type'");
    }
}
