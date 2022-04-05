package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.barcodedata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.text.Strings;

public class BarcodeBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Barcode Data";

    public BarcodeBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public BarcodeBufferAppender() {
        super(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        final BarCodeBuffer barCodeBuffer = readBuffer(ndcCharBuffer);
        stateObject.withBarCodeBuffer(barCodeBuffer);
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return BarCodeBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    private BarCodeBuffer readBuffer(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return BarCodeBuffer.EMPTY;
        }
        final int barcodeId = ndcCharBuffer.tryReadHexInt(4)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": Barcode Format Identifier", errorMessage, ndcCharBuffer))
                .get();

        ndcCharBuffer.trySkip(2)
                .ifPresent(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": Reserved Field", errorMessage, ndcCharBuffer));

        final String barcodeData = readBarcodeData(ndcCharBuffer);
        return new BarCodeBuffer(new BarcodeData(barcodeId, barcodeData, null));
    }

    private String readBarcodeData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(ndcCharBuffer.readNextChar());
        } while (ndcCharBuffer.hasFieldDataRemaining());

        return builder.toString();
    }
}
