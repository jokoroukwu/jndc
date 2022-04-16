package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.documentdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

public class DocumentBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static String FIELD_NAME = "Document Buffer";

    public DocumentBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        super(nextAppender);
    }

    public DocumentBufferAppender() {
        super(null);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);
        final DocumentBuffer documentBuffer = readDocumentBuffer(ndcCharBuffer);
        stateObject.withDocumentBuffer(documentBuffer);
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return DocumentBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    private DocumentBuffer readDocumentBuffer(NdcCharBuffer ndcCharBuffer) {
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final SingleChequeDepositData singleChequeDepositData = readChequeDepositData(ndcCharBuffer);
            return new DocumentBuffer(singleChequeDepositData);
        }
        return DocumentBuffer.EMPTY;
    }

    private SingleChequeDepositData readChequeDepositData(NdcCharBuffer ndcCharBuffer) {
        final boolean codeLineDetected = ndcCharBuffer.tryReadNextChar()
                .flatMapToObject(this::toCodeLineDetectedBoolean)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(getCommandName(),
                        FIELD_NAME + ": Codeline detected", errorMessage, ndcCharBuffer))
                .get();

        return codeLineDetected
                ? new SingleChequeDepositData(readCodeLineValue(ndcCharBuffer), null)
                : SingleChequeDepositData.EMPTY;
    }

    private String readCodeLineValue(NdcCharBuffer ndcCharBuffer) {
        final int maxDataLength = 256;
        final char[] data = new char[256];
        int charsConsumed = 0;
        do {
            data[charsConsumed++] = ndcCharBuffer.readNextChar();
        } while (charsConsumed < maxDataLength && ndcCharBuffer.hasFieldDataRemaining());
        return String.valueOf(data, 0, charsConsumed);
    }

    private DescriptiveOptional<Boolean> toCodeLineDetectedBoolean(int value) {
        if (value == '1') {
            return DescriptiveOptional.of(Boolean.TRUE);
        }
        if (value == '0') {
            return DescriptiveOptional.of(Boolean.FALSE);
        }
        return DescriptiveOptional.empty(() -> "should be '1' or '0' but was: " + value);
    }
}
