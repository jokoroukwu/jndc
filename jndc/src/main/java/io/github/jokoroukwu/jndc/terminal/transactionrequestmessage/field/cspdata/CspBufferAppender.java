package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cspdata;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.function.BiConsumer;

public class CspBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    private final String fieldName;
    private final char id;
    private final BiConsumer<TransactionRequestMessageBuilder, CspData> dataConsumer;

    CspBufferAppender(String fieldName,
                      char id,
                      ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                      BiConsumer<TransactionRequestMessageBuilder, CspData> dataConsumer) {
        super(nextAppender);
        this.fieldName = fieldName;
        this.dataConsumer = dataConsumer;
        this.id = id;
    }

    public static CspBufferAppender confirmationCsp(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        return new CspBufferAppender("Confirmation CSP Data ID 'V'", CspData.CONFIRMATION_CSP_DATA_ID, nextAppender,
                TransactionRequestMessageBuilder::withConfirmationCspData);
    }

    public static CspBufferAppender confirmationCsp() {
        return confirmationCsp(null);
    }

    public static CspBufferAppender csp(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        return new CspBufferAppender("CSP Data ID 'U'", CspData.CSP_DATA_ID, nextAppender,
                TransactionRequestMessageBuilder::withCspData);
    }

    public static CspBufferAppender csp() {
        return csp(null);
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);
        // at least data id should be present
        final CspData cspData = readCspData(ndcCharBuffer);
        dataConsumer.accept(stateObject, cspData);

        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return id;
    }

    @Override
    protected String getFieldName() {
        return fieldName;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    private CspData readCspData(NdcCharBuffer ndcCharBuffer) {
        final String data = readData(ndcCharBuffer);
        return new CspData(id, data);
    }

    private String readData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final int maxDataLength = 16;
        int charsConsumed = 0;
        final char[] chars = new char[maxDataLength];
        do {
            chars[charsConsumed++] = ndcCharBuffer.readNextChar();
        } while (charsConsumed < maxDataLength && ndcCharBuffer.hasFieldDataRemaining());
        return String.valueOf(chars, 0, charsConsumed);
    }
}
