package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.languagesupporttable;

import io.github.jokoroukwu.jndc.CentralMessageListener;
import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigMessageSubClass;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IccLanguageSupportTableAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String COMMAND_NAME = EmvConfigurationMessage.COMMAND_NAME + ": " + EmvConfigMessageSubClass.LANGUAGE_SUPPORT;
    public static final String NUMBER_OF_ENTRIES_FIELD = "Number of Language Mapping Entries";
    private final LanguageSupportDataObjectsTableMessageListener messageListener;
    private final NdcComponentAppender<LanguageSupportTableEntryBuilder> entryFieldAppender;
    private final MacReader macReader;

    public IccLanguageSupportTableAppender(LanguageSupportDataObjectsTableMessageListener messageListener,
                                           NdcComponentAppender<LanguageSupportTableEntryBuilder> entryFieldAppender, MacReader macReader) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener");
        this.entryFieldAppender = ObjectUtils.validateNotNull(entryFieldAppender, "entryFieldAppender");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader");
    }

    public IccLanguageSupportTableAppender(CentralMessageListener centralMessageListener) {
        this(centralMessageListener, new LanguageCodeFieldAppender(), MacReaderBase.INSTANCE);
    }


    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {

        final List<LanguageSupportTableEntry> entries = readEntries(ndcCharBuffer);
        final IccLanguageSupportTable table = new IccLanguageSupportTable(entries);
        String mac = Strings.EMPTY_STRING;
        if (ndcCharBuffer.hasRemaining()) {
            mac = macReader.tryReadMac(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(COMMAND_NAME, MacReaderBase.FIELD_NAME, errorMessage, ndcCharBuffer));
        }

        final EmvConfigurationMessage<IccLanguageSupportTable> languageSupportTableMessage = EmvConfigurationMessage.<IccLanguageSupportTable>builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withConfigurationData(table)
                .withMac(mac)
                .build();

        messageListener.onLanguageSupportDataObjectsTableMessage(languageSupportTableMessage);
    }

    private List<LanguageSupportTableEntry> readEntries(NdcCharBuffer ndcCharBuffer) {
        final int numberOfEntries = ndcCharBuffer.tryReadHexInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(COMMAND_NAME, NUMBER_OF_ENTRIES_FIELD,
                        errorMessage, ndcCharBuffer))
                .get();

        final List<LanguageSupportTableEntry> entries = new ArrayList<>(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            final LanguageSupportTableEntryBuilder entryBuilder = new LanguageSupportTableEntryBuilder();
            entryFieldAppender.appendComponent(ndcCharBuffer, entryBuilder);
            entries.add(entryBuilder.buildWithNoValidation());
        }
        return Collections.unmodifiableList(entries);
    }
}
