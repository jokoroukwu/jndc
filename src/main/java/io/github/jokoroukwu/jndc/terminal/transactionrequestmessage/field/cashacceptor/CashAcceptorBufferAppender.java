package io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.cashacceptor;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.terminal.ConfigurableNdcComponentAppender;
import io.github.jokoroukwu.jndc.terminal.ConfigurationOptions;
import io.github.jokoroukwu.jndc.terminal.DeviceConfiguration;
import io.github.jokoroukwu.jndc.terminal.TerminalMessageSubClass;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.TransactionRequestMessageBuilder;
import io.github.jokoroukwu.jndc.terminal.transactionrequestmessage.field.IdentifiableBufferAppender;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashAcceptorBufferAppender extends IdentifiableBufferAppender<TransactionRequestMessageBuilder> {
    public static final String FIELD_NAME = "Cash Acceptor Buffer";
    private final NdcComponentReaderFactory<ConfigurationOptions, CashAcceptorNote> cashAcceptorNoteReaderFactory;

    public CashAcceptorBufferAppender(ConfigurableNdcComponentAppender<TransactionRequestMessageBuilder> nextAppender,
                                      NdcComponentReaderFactory<ConfigurationOptions, CashAcceptorNote> cashAcceptorNoteReaderFactory) {
        super(nextAppender);
        this.cashAcceptorNoteReaderFactory = ObjectUtils.validateNotNull(cashAcceptorNoteReaderFactory, "Reader Factory");
    }

    public CashAcceptorBufferAppender() {
        this(null, BaseNoteReaderFactory.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TransactionRequestMessageBuilder stateObject, DeviceConfiguration deviceConfiguration) {
        skipBufferMeta(ndcCharBuffer);

        if (ndcCharBuffer.hasFieldDataRemaining()) {
            final List<CashAcceptorNote> cashAcceptorNotes = readCashAcceptorNotes(ndcCharBuffer, deviceConfiguration);
            stateObject.withCashAcceptorBuffer(new CashAcceptorBuffer(cashAcceptorNotes));
        } else {
            stateObject.withCashAcceptorBuffer(CashAcceptorBuffer.EMPTY);
        }
        callNextAppenderIfDataRemains(ndcCharBuffer, stateObject, deviceConfiguration);
    }

    @Override
    protected char getBufferId() {
        return CashAcceptorBuffer.ID;
    }

    @Override
    protected String getFieldName() {
        return FIELD_NAME;
    }

    @Override
    protected String getCommandName() {
        return TerminalMessageSubClass.TRANSACTION_REQUEST_MESSAGE.toString();
    }

    private List<CashAcceptorNote> readCashAcceptorNotes(NdcCharBuffer ndcCharBuffer, DeviceConfiguration deviceConfiguration) {
        final NdcComponentReader<CashAcceptorNote> noteReader =
                cashAcceptorNoteReaderFactory.getNoteReader(deviceConfiguration.getConfigurationOptions());
        final ArrayList<CashAcceptorNote> notes = new ArrayList<>();
        do {
            final CashAcceptorNote note = noteReader.readComponent(ndcCharBuffer);
            notes.add(note);
        } while (ndcCharBuffer.hasFieldDataRemaining());

        notes.trimToSize();
        return Collections.unmodifiableList(notes);
    }
}
