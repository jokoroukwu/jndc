package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.currencydataobjectstable;

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
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyCode;
import io.github.jokoroukwu.jndc.tlv.TransactionCurrencyExponent;
import io.github.jokoroukwu.jndc.util.Integers;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IccCurrencyDataObjectsTableAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.CURRENCY;
    public static final String NUMBER_OF_ENTRIES_FIELD = "Number of Currency Type Mapping Entries";


    private final CurrencyDataObjectsTableMessageListener messageListener;
    private final NdcComponentReader<DescriptiveOptional<CurrencyTableEntry>> entryReader;
    private final MacReader macReader;

    public IccCurrencyDataObjectsTableAppender(CurrencyDataObjectsTableMessageListener messageListener,
                                               NdcComponentReader<DescriptiveOptional<CurrencyTableEntry>> entryReader,
                                               MacReader macReader) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener cannot be null");
        this.entryReader = ObjectUtils.validateNotNull(entryReader, "entryAppender cannot be null");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader cannot be null");
    }

    public IccCurrencyDataObjectsTableAppender(CurrencyDataObjectsTableMessageListener messageListener) {
        this(messageListener, new DataObjectsTableEntryReader<>(new ResponseFormat2Reader(List.of(TransactionCurrencyCode.READER,
                TransactionCurrencyExponent.READER)), CurrencyTableEntry::new), MacReaderBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        final int numberOfEntries = ndcCharBuffer
                .tryReadHexInt(2)
                .filter(Integers::isPositive, actual -> () -> String.format("value should be in range 0x01-0xFF but was %#X", actual))
                .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, NUMBER_OF_ENTRIES_FIELD,
                        errorMessage, ndcCharBuffer));

        final List<CurrencyTableEntry> entries = readEntries(ndcCharBuffer, numberOfEntries);
        final IccCurrencyDataObjectsTable table = new IccCurrencyDataObjectsTable(entries);
        String mac = Strings.EMPTY_STRING;
        if (ndcCharBuffer.hasRemaining()) {
            mac = macReader.tryReadMac(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, MacReaderBase.FIELD_NAME, errorMessage, ndcCharBuffer));
        }

        final EmvConfigurationMessage<IccCurrencyDataObjectsTable> message = EmvConfigurationMessage.<IccCurrencyDataObjectsTable>builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withConfigurationData(table)
                .withMac(mac)
                .build();

        messageListener.onCurrencyDataObjectsTableMessage(message);
    }

    private List<CurrencyTableEntry> readEntries(NdcCharBuffer buffer, int numberOfEntries) {
        final List<CurrencyTableEntry> entries = new ArrayList<>(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            entryReader.readComponent(buffer)
                    .resolve(entries::add, errorMessage -> NdcMessageParseException.onFieldParseError(COMMAND_NAME,
                            "'Table data'", errorMessage, buffer));
        }
        return Collections.unmodifiableList(entries);
    }
}
