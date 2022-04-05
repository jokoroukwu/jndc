package io.github.jokoroukwu.jndc.central.transactionreply;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.transactionreply.sequencetimevariant.TimeVariantSequenceNumberAppender;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.DeviceConfigurationSupplier;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class TransactionReplyCommandAppender implements NdcComponentAppender<CentralMessageMeta> {
    private final ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> fieldAppender;
    private final DeviceConfigurationSupplier<CentralMessageMeta> deviceConfigurationSupplier;
    private final TransactionReplyCommandListener commandListener;

    public TransactionReplyCommandAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> fieldAppender,
                                           DeviceConfigurationSupplier<CentralMessageMeta> deviceConfigurationSupplier,
                                           TransactionReplyCommandListener commandListener) {
        this.fieldAppender = ObjectUtils.validateNotNull(fieldAppender, "fieldAppender");
        this.deviceConfigurationSupplier = ObjectUtils.validateNotNull(deviceConfigurationSupplier, "deviceConfigurationSupplier");
        this.commandListener = ObjectUtils.validateNotNull(commandListener, "commandListener");
    }

    public TransactionReplyCommandAppender(TransactionReplyCommandListener commandListener,
                                           DeviceConfigurationSupplier<CentralMessageMeta> deviceConfigurationSupplier) {
        this(new TimeVariantSequenceNumberAppender(), deviceConfigurationSupplier, commandListener);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        final DeviceConfiguration deviceConfiguration = deviceConfigurationSupplier.getConfiguration(stateObject);
        final TransactionReplyCommandBuilder builder = new TransactionReplyCommandBuilder();

        try {
            fieldAppender.appendComponent(ndcCharBuffer, builder, deviceConfiguration);
        } catch (NdcMessageParseException e) {
            throw new NdcMessageParseException(e, CentralMessageClass.TRANSACTION_REPLY_COMMAND + ": " + e.getMessage());
        }

        final TransactionReplyCommand transactionReplyCommand = builder
                .withLuno(stateObject.getLuno())
                .withResponseFlag(stateObject.getResponseFlag())
                .build();
        commandListener.onTransactionReplyCommand(transactionReplyCommand);
    }
}
