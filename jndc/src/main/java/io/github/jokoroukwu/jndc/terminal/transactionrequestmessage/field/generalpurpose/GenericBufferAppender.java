package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.generalpurpose;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.function.BiConsumer;

public class GenericBufferAppender extends ChainedConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> {
    private static final int maxDataLength = 32;
    private final int minDataLength;
    private final String fieldName;
    private final BiConsumer<TransactionRequestMessageBuilder, String> dataConsumer;

    public GenericBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                 int minDataLength,
                                 String fieldName,
                                 BiConsumer<TransactionRequestMessageBuilder, String> dataConsumer) {
        super(nextAppender);
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.dataConsumer = ObjectUtils.validateNotNull(dataConsumer, "dataConsumer");
        this.minDataLength = validateMinDataLength(minDataLength);
    }

    public GenericBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender, String fieldName,
                                 BiConsumer<TransactionRequestMessageBuilder, String> dataConsumer) {
        this(nextAppender, 0, fieldName, dataConsumer);
    }

    public static GeneralPurposeBufferAppenderBuilder builder() {
        return new GeneralPurposeBufferAppenderBuilder();
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        ndcCharBuffer.trySkipFieldSeparator()
                .ifPresent(errorMessage
                        -> NdcMessageParseException.onNoFieldSeparator(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), fieldName, errorMessage, ndcCharBuffer));

        final String data = readData(ndcCharBuffer);
        dataConsumer.accept(stateObject, data);
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    private String readData(NdcCharBuffer ndcCharBuffer) {
        if (!ndcCharBuffer.hasFieldDataRemaining()) {
            return Strings.EMPTY_STRING;
        }
        final char[] chars = new char[maxDataLength];
        int charsConsumed = 0;
        do {
            chars[charsConsumed++] = ndcCharBuffer.readNextChar();
        } while (charsConsumed < maxDataLength && ndcCharBuffer.hasFieldDataRemaining());

        checkLength(charsConsumed, ndcCharBuffer);
        return String.valueOf(chars, 0, charsConsumed);
    }

    private void checkLength(int length, NdcCharBuffer ndcCharBuffer) {
        if (minDataLength > 0 && length < minDataLength) {
            final String errorMessage = String.format("'%s' must contain at least %d characters but had: %d",
                    fieldName, minDataLength, length);
            NdcMessageParseException.onFieldParseError(TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString(), fieldName, errorMessage, ndcCharBuffer);
        }
    }

    private int validateMinDataLength(int minDataLength) {
        if (minDataLength > maxDataLength) {
            throw new IllegalArgumentException(String.format("min data length (%d) exceeds max data length (%d)",
                    minDataLength, maxDataLength));
        }
        return minDataLength;
    }

}
