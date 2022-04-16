package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

public enum BaseAidReader implements AidReader {
    INSTANCE;

    public DescriptiveOptional<Aid> readAid(NdcCharBuffer ndcCharBuffer) {
        return ndcCharBuffer.tryReadHexInt(2)
                .filter(this::isWithinAidLengthRange, length -> () -> "length should be in range 0x01-0x10 hex but was 0x%X " + length)
                .flatMapToObject((length) -> doReadAid(ndcCharBuffer, length));
    }

    private DescriptiveOptional<Aid> doReadAid(NdcCharBuffer ndcCharBuffer, int aidLength) {
        return ndcCharBuffer.tryReadCharSequence(aidLength * 2)
                .filter(Strings::isHex, val -> () -> String.format("should be hexadecimal but was '%s'", val))
                .map(val -> new Aid(val, aidLength));
    }

    private boolean isWithinAidLengthRange(int value) {
        return value > 0 && value <= 16;
    }

}
