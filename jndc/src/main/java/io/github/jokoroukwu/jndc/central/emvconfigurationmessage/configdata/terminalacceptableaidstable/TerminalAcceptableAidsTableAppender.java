package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerminalAcceptableAidsTableAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String TABLE_DATA_FIELD = "ICC Terminal Acceptable AIDs Table data";
    public static final int MAX_ENTRY_INDEX = 0xFF;

    private final TerminalAcceptableAidsTableMessageListener messageListener;
    private final NdcComponentAppender<TerminalApplicationIdEntryBuilder> fieldAppender;
    private final MacReader macReader;


    public TerminalAcceptableAidsTableAppender(TerminalAcceptableAidsTableMessageListener messageListener,
                                               NdcComponentAppender<TerminalApplicationIdEntryBuilder> fieldAppender,
                                               MacReader macReader) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.fieldAppender = ObjectUtils.validateNotNull(fieldAppender, "fieldAppender");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader");
    }

    public TerminalAcceptableAidsTableAppender(TerminalAcceptableAidsTableMessageListener messageListener) {
        this(messageListener, new EntryNumberAppender(), MacReaderBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        ObjectUtils.validateNotNull(ndcCharBuffer, "ndcCharBuffer");
        ObjectUtils.validateNotNull(stateObject, "messageMeta");

        final List<TerminalApplicationIdEntry> entries = readEntries(ndcCharBuffer);
        String mac = Strings.EMPTY_STRING;
        if (ndcCharBuffer.hasRemaining()) {
            mac = macReader.tryReadMac(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, MacReaderBase.FIELD_NAME, errorMessage, ndcCharBuffer));
        }
        final IccTerminalAcceptableAppIdsTable table = new IccTerminalAcceptableAppIdsTable(entries);
        final EmvConfigurationMessage<IccTerminalAcceptableAppIdsTable> message = EmvConfigurationMessage.<IccTerminalAcceptableAppIdsTable>builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withConfigurationData(table)
                .withMac(mac)
                .build();
        messageListener.onTerminalAcceptableAidsTableMessage(message);
    }

    private List<TerminalApplicationIdEntry> readEntries(NdcCharBuffer ndcCharBuffer) {
        final ArrayList<TerminalApplicationIdEntry> entryList = new ArrayList<>();
        entryList.add(readNextEntry(ndcCharBuffer));
        for (int i = 1; ndcCharBuffer.hasRemaining() && !ndcCharBuffer.hasFollowingFieldSeparator(); i++) {
            checkEntryIndex(i, ndcCharBuffer);
            ndcCharBuffer.trySkipGroupSeparator()
                    .ifPresent(errorMessage -> NdcMessageParseException.onNoGroupSeparator(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, TABLE_DATA_FIELD, errorMessage, ndcCharBuffer));
            entryList.add(readNextEntry(ndcCharBuffer));
        }
        entryList.trimToSize();
        return Collections.unmodifiableList(entryList);
    }

    private TerminalApplicationIdEntry readNextEntry(NdcCharBuffer tableData) {
        TerminalApplicationIdEntryBuilder entryBuilder = new TerminalApplicationIdEntryBuilder();
        fieldAppender.appendComponent(tableData, entryBuilder);
        return entryBuilder.buildWithNoValidation();
    }

    private void checkEntryIndex(int index, NdcCharBuffer ndcCharBuffer) {
        if (index > MAX_ENTRY_INDEX) {
            NdcMessageParseException.onMessageParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME,
                    String.format("table data exceeds max number of entries (%d)", MAX_ENTRY_INDEX + 1),
                    ndcCharBuffer);
        }
    }
}
