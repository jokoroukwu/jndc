package io.github.jokoroukwu.jndc.central.transactionreply.notestodispense;

import io.github.jokoroukwu.jndc.ChainedConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.transactionreply.TransactionReplyCommandBuilder;
import io.github.jokoroukwu.jndc.central.transactionreply.coinstodispense.CoinsToDispenseAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor.NdcComponentReaderFactory;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

public class NotesToDispenseAppender extends ChainedConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> {
    private final NdcComponentReaderFactory<ConfigurationOptions, NotesToDispense> notesReaderFactory;

    public NotesToDispenseAppender(ConfigurableNdcComponentAppender<TransactionReplyCommandBuilder> nextAppender,
                                   NdcComponentReaderFactory<ConfigurationOptions, NotesToDispense> notesReaderFactory) {
        super(nextAppender);
        this.notesReaderFactory = ObjectUtils.validateNotNull(notesReaderFactory, "Reader Factory");
    }

    public NotesToDispenseAppender() {
        this(new CoinsToDispenseAppender(), NotesToDispenseReaderFactory.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionReplyCommandBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        //  field may be empty
        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final NdcComponentReader<NotesToDispense> notesReader = notesReaderFactory.getNoteReader(deviceConfiguration.getConfigurationOptions());
            final NotesToDispense notesToDispense = notesReader.readComponent(ndcCharBuffer);
            stateObject.withNotesToDispense(notesToDispense);
        }
        callNextAppender(ndcCharBuffer, stateObject, deviceConfiguration);
    }
}
