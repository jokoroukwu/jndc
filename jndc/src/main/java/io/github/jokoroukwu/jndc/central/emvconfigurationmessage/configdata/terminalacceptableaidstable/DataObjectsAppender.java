package io.github.jokoroukwu.jndc.central.emvconfigurationmessage.configdata.terminalacceptableaidstable;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.exception.NdcMessageParseException;
import io.github.jokoroukwu.jndc.tlv.BerTlvReader;
import io.github.jokoroukwu.jndc.tlv.BerTlvReaderBase;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.function.BiConsumer;

public class DataObjectsAppender extends TerminalAcceptableAidsFieldAppender {
    public static final String TRANSACTION_RQ_OBJECTS = "Data Object(s) for Transaction Request";
    public static final String COMPLETION_DATA_OBJECTS = "Data Object(s) for Completion Data";

    private final BerTlvReader bertlvReader;
    private final BiConsumer<TerminalApplicationIdEntryBuilder, TagContainer> consumer;
    private final String fieldName;
    private final String numberOfObjectsField;

    public DataObjectsAppender(String fieldName,
                               BerTlvReader berTlvReader,
                               BiConsumer<TerminalApplicationIdEntryBuilder, TagContainer> consumer,
                               TerminalAcceptableAidsFieldAppender nextReader) {
        super(nextReader);
        this.bertlvReader = ObjectUtils.validateNotNull(berTlvReader, "berTlvReader");
        this.fieldName = ObjectUtils.validateNotNull(fieldName, "fieldName");
        this.consumer = ObjectUtils.validateNotNull(consumer, "consumer");
        numberOfObjectsField = "Number of " + fieldName;
    }

    public DataObjectsAppender(String fieldName,
                               BiConsumer<TerminalApplicationIdEntryBuilder, TagContainer> consumer,
                               TerminalAcceptableAidsFieldAppender nextReader) {
        this(fieldName, BerTlvReaderBase.INSTANCE, consumer, nextReader);
    }

    @Override
    public void appendComponent(NdcCharBuffer ndcCharBuffer, TerminalApplicationIdEntryBuilder stateObject) {
        final int numberOfDataObjects = ndcCharBuffer.tryReadHexInt(2)
                .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, numberOfObjectsField,
                        errorMessage, ndcCharBuffer))
                .get();
        final TagContainer dataObjects = readDataObjects(numberOfDataObjects, ndcCharBuffer);
        consumer.accept(stateObject, dataObjects);

        callNextAppender(ndcCharBuffer, stateObject);
    }

    private TagContainer readDataObjects(int count, NdcCharBuffer ndcCharBuffer) {
        if (count == 0) {
            return TagContainer.EMPTY;
        }
        final TagContainer tags = new TagContainer(count);
        for (int i = 0; i < count; i++) {
            final int tag = bertlvReader.tryReadTag(ndcCharBuffer)
                    .ifEmpty(errorMessage -> NdcMessageParseException.onFieldParseError(IccTerminalAcceptableAppIdsTable.COMMAND_NAME, fieldName, errorMessage,
                            ndcCharBuffer))
                    .get();

            tags.add(tag);
        }
        return new TagContainer(tags);
    }

}
