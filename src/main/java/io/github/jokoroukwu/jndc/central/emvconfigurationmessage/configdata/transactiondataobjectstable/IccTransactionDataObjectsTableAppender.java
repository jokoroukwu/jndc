package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.transactiondataobjectstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.DataObjectsTableEntryReader;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2Reader;
import io.github.jokoroukwu.jndc.tlv.TransactionCategoryCode;
import io.github.jokoroukwu.jndc.tlv.TransactionType;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IccTransactionDataObjectsTableAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.TRANSACTION;
    public static final String NUMBER_OF_ENTRIES_FIELD = "Number of Type Mapping Entries";

    private final TransactionDataObjectsTableMessageListener messageListener;
    private final NdcComponentReader<DescriptiveOptional<TransactionTableEntry>> transactionEntryReader;
    private final MacReader macReader;

    public IccTransactionDataObjectsTableAppender(TransactionDataObjectsTableMessageListener messageListener,
                                                  NdcComponentReader<DescriptiveOptional<TransactionTableEntry>> transactionEntryReader,
                                                  MacReader macReader) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.transactionEntryReader = ObjectUtils.validateNotNull(transactionEntryReader, "messageBuilderFactory");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader");
    }

    public IccTransactionDataObjectsTableAppender(TransactionDataObjectsTableMessageListener ndcMessageListener) {
        this(ndcMessageListener, new DataObjectsTableEntryReader<>(new ResponseFormat2Reader(List.of(TransactionType.READER,
                TransactionCategoryCode.READER)), TransactionTableEntry::new), MacReaderBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        final int numberOfEntries = ndcCharBuffer.tryReadHexInt(2)
                .filter(Integers::isPositive, value -> () -> "number of entries should be in range 0x01-0xFF but was: " + value)
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, NUMBER_OF_ENTRIES_FIELD, errorMessage, ndcCharBuffer));

        final List<TransactionTableEntry> entries = readEntries(ndcCharBuffer, numberOfEntries);
        final IccTransactionDataObjectsTable table = new IccTransactionDataObjectsTable(entries);
        String mac = Strings.EMPTY_STRING;
        if (ndcCharBuffer.hasRemaining()) {
            mac = macReader.tryReadMac(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, MacReaderBase.FIELD_NAME, errorMessage, ndcCharBuffer));
        }

        final EmvConfigurationMessage<IccTransactionDataObjectsTable> message = EmvConfigurationMessage.<IccTransactionDataObjectsTable>builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withConfigurationData(table)
                .withMac(mac)
                .build();

        messageListener.onTransactionDataObjectsTableMessage(message);
    }

    private List<TransactionTableEntry> readEntries(NdcCharBuffer buffer, int numberOfEntries) {
        final List<TransactionTableEntry> entries = new ArrayList<>(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            transactionEntryReader.readComponent(buffer)
                    .resolve(entries::add, errorMessage -> NdcMessageParseException.onFieldParseError(COMMAND_NAME,
                            "Table Data", errorMessage, buffer));
        }
        return Collections.unmodifiableList(entries);
    }

}
