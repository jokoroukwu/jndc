package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage;

import io.github.jokoroukwu.jndc.Luno;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.terminal.meta.TerminalMessageMeta;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.TimeVariantNumberAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.function.Supplier;

public class TransactionRequestMessageAppender implements NdcComponentAppender<TerminalMessageMeta> {
    private final TransactionRequestMessageListener messageListener;
    private final DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier;
    private final ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> fieldAppender;
    private final Supplier<? extends TransactionRequestMessageBuilder> messageBuilderFactory;

    public TransactionRequestMessageAppender(TransactionRequestMessageListener messageListener,
                                             DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier,
                                             ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> fieldAppender,
                                             Supplier<? extends TransactionRequestMessageBuilder> messageBuilderFactory) {

        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener cannot be null");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier,
                "deviceConfigurationSupplier cannot be null");
        this.fieldAppender = ObjectUtils.validateNotNull(fieldAppender, "fieldAppender cannot be null");
        this.messageBuilderFactory = ObjectUtils.validateNotNull(messageBuilderFactory, "messageBuilderSupplier cannot be null");
    }

    public TransactionRequestMessageAppender(TransactionRequestMessageListener messageListener,
                                             DeviceConfigurationSupplier<TerminalMessageMeta> deviceConfigurationSupplier) {
        this(messageListener, deviceConfigurationSupplier, new TimeVariantNumberAppender(), TransactionRequestMessageBuilder::new);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalMessageMeta stateObject) {
        final Luno luno = Luno.readLuno(ndcCharBuffer)
                .getOrThrow(errorMessage -> NdcMessageParseException.withComposedMessage(stateObject.getMessageSubClass().toString(), Luno.FIELD_NAME, errorMessage));

        final DeviceConfiguration deviceConfiguration = deviceConfigurationSupplier.getConfiguration(stateObject);

        final TransactionRequestMessageBuilder builder = messageBuilderFactory.get();

        fieldAppender.appendComponent(ndcCharBuffer, builder, deviceConfiguration);

        final TransactionRequestMessage message = builder.withLuno(luno).build();
        messageListener.onTransactionRequestMessage(message);
    }
}
