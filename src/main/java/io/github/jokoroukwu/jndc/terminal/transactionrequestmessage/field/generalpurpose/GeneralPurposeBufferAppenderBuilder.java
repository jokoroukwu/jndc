package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.generalpurpose;

import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;

import java.util.function.BiConsumer;

public class GeneralPurposeBufferAppenderBuilder {
    private int minDataLength;
    private String fieldName;
    private BiConsumer<TransactionRequestMessageBuilder, String> dataConsumer;
    private ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender;


    public GeneralPurposeBufferAppenderBuilder withMinDataLength(int minDataLength) {
        this.minDataLength = minDataLength;
        return this;
    }

    public GeneralPurposeBufferAppenderBuilder withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public GeneralPurposeBufferAppenderBuilder withDataConsumer(BiConsumer<TransactionRequestMessageBuilder, String> dataConsumer) {
        this.dataConsumer = dataConsumer;
        return this;
    }

    public GeneralPurposeBufferAppenderBuilder withNextAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender) {
        this.nextAppender = nextAppender;
        return this;
    }

    public GenericBufferAppender build() {
        return new GenericBufferAppender(nextAppender, minDataLength, fieldName, dataConsumer);
    }
}
