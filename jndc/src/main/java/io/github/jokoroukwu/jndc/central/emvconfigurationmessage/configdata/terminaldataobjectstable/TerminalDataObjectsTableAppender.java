package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminaldataobjectstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.NdcComponentAppender;
import io.github.jokoroukwu.jndc.NdcComponentReader;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.CentralMessageMeta;
import io.github.jokoroukwu.jndc.central.emvconfigurationmessage.EmvConfigurationMessage;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.mac.MacReader;
import io.github.jokoroukwu.jndc.mac.MacReaderBase;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2;
import io.github.jokoroukwu.jndc.tlv.ResponseFormat2Reader;
import io.github.jokoroukwu.jndc.tlv.TerminalCountryCode;
import io.github.jokoroukwu.jndc.tlv.TerminalType;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.List;

public class TerminalDataObjectsTableAppender implements NdcComponentAppender<CentralMessageMeta> {
    public static final String FIELD_NAME = "BER‐TLV Data Objects";

    private final TerminalDataObjectsTableMessageListener messageListener;
    private final NdcComponentReader<DescriptiveOptional<ResponseFormat2>> dataObjectsReader;
    private final MacReader macReader;

    public TerminalDataObjectsTableAppender(TerminalDataObjectsTableMessageListener messageListener,
                                            NdcComponentReader<DescriptiveOptional<ResponseFormat2>> dataObjectsReader,
                                            MacReader macReader) {
        this.messageListener = ObjectUtils.validateNotNull(messageListener, "messageListener cannot be null");
        this.dataObjectsReader = ObjectUtils.validateNotNull(dataObjectsReader, "dataObjectsReader cannot be null");
        this.macReader = ObjectUtils.validateNotNull(macReader, "macReader cannot be null");
    }

    public TerminalDataObjectsTableAppender(TerminalDataObjectsTableMessageListener messageListener) {
        this(messageListener, new ResponseFormat2Reader(List.of(TerminalCountryCode.READER, TerminalType.READER)),
                MacReaderBase.INSTANCE);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, CentralMessageMeta stateObject) {
        final ResponseFormat2 responseFormat2 = dataObjectsReader.readComponent(ndcCharBuffer)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(FIELD_NAME, errorMessage, ndcCharBuffer))
                .get();

        String mac = Strings.EMPTY_STRING;
        if (ndcCharBuffer.hasRemaining()) {
            mac = macReader.tryReadMac(ndcCharBuffer)
                    .getOrThrow(errorMessage -> NdcMessageParseException.withMessage(IccTerminalDataObjectsTable.COMMAND_NAME, MacReaderBase.FIELD_NAME, errorMessage, ndcCharBuffer));
        }

        final IccTerminalDataObjectsTable configData = new IccTerminalDataObjectsTable(responseFormat2);
        final EmvConfigurationMessage<IccTerminalDataObjectsTable> message = EmvConfigurationMessage.<IccTerminalDataObjectsTable>builder()
                .withResponseFlag(stateObject.getResponseFlag())
                .withLuno(stateObject.getLuno())
                .withConfigurationData(configData)
                .withMac(mac)
                .build();
        messageListener.onTerminalDataObjectsTableMessage(message);
    }
}
