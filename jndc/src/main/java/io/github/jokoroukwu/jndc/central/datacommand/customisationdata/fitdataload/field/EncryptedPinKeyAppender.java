package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.field;

import io.github.jokoroukwu.jndc.ChainedNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitBuilder;
import io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload.FitDataLoadCommand;
import io.github.jokoroukwu.jndc.util.ByteUtils;

import static io.github.jokoroukwu.jndc.exception.NdcMessageParseException.withMessage;

public class EncryptedPinKeyAppender extends ChainedNdcComponentAppender<FitBuilder> {
    public static final String FIELD_NAME = "PEKEY (Encrypted PIN Key)";

    protected EncryptedPinKeyAppender(NdcComponentAppender<FitBuilder> nextReader) {
        super(nextReader);
    }

    public EncryptedPinKeyAppender() {
        this(new IndexReferencePointAppender());
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, FitBuilder stateObject) {
        final byte[] encryptedPinKeyArray = new byte[8];
        for (int i = 0; i < encryptedPinKeyArray.length; i++) {
            final byte nextByte = (byte) ndcCharBuffer.tryReadInt(3)
                    .filter(ByteUtils::isWithinUnsignedRange, val -> ()
                            -> String.format("each digit should be in range 0x00-0xFF but found 0x%X at position %d", val,
                            ndcCharBuffer.position() - 3))
                    .getOrThrow(errorMessage
                            -> withMessage(FitDataLoadCommand.COMMAND_NAME, FIELD_NAME, errorMessage, ndcCharBuffer));
            encryptedPinKeyArray[i] = nextByte;
        }
        stateObject.withEncryptedPinKey(encryptedPinKeyArray);
        callNextAppender(ndcCharBuffer, stateObject);
    }
}
